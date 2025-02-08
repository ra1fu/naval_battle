package repositories;

import data.interfaces.IDB;
import models.*;
import repositories.interfaces.IGameRepository;
import factories.EntityFactory;
import validators.GameValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PGobject;

public class GameRepository implements IGameRepository {
    private final IDB db;

    public GameRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean isGameOver(int gameId) {
        if (!GameValidator.isValidGameId(gameId)) return false;

        String sql = "SELECT COUNT(*) FROM ships WHERE game_id = ? AND sunk = FALSE";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, gameId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (isGameOver): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean checkHit(int gameId, int x, int y) {
        if (!GameValidator.isValidGameId(gameId) || !GameValidator.isValidMove(x, y)) return false;

        String sql = "SELECT ship_id FROM ships WHERE game_id = ? AND x = ? AND y = ? AND sunk = FALSE";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, gameId);
            st.setInt(2, x);
            st.setInt(3, y);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("SQL Error (checkHit): " + e.getMessage());
        }
        return false;
    }

    @Override
    public void updateShipStatus(int gameId, int x, int y) {
        if (!GameValidator.isValidGameId(gameId) || !GameValidator.isValidMove(x, y)) return;

        String sql = "UPDATE ships SET sunk = TRUE WHERE game_id = ? AND x = ? AND y = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, gameId);
            st.setInt(2, x);
            st.setInt(3, y);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error (updateShipStatus): " + e.getMessage());
        }
    }

    @Override
    public boolean createGame(Game game) {
        if (!GameValidator.isValidGame(game)) return false;

        String sql = "INSERT INTO games (player1_id, player2_id, current_turn, status, winner_id) " +
                "VALUES (?, ?, ?, ?::game_status, ?) RETURNING game_id";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, game.getPlayer1Id());
            st.setInt(2, game.getPlayer2Id());
            st.setInt(3, game.getCurrentTurn());

            PGobject statusObj = new PGobject();
            statusObj.setType("game_status");
            statusObj.setValue(game.getStatus());
            st.setObject(4, statusObj);

            if (game.getWinnerId() != null) {
                st.setInt(5, game.getWinnerId());
            } else {
                st.setNull(5, Types.INTEGER);
            }

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (createGame): " + e.getMessage());
        }
        return false;
    }

    @Override
    public Game getGame(int id) {
        if (!GameValidator.isValidGameId(id)) return null;

        String sql = "SELECT game_id, player1_id, player2_id, current_turn, status, winner_id FROM games WHERE game_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return EntityFactory.createGame(
                        rs.getInt("game_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("current_turn"),
                        rs.getString("status"),
                        rs.getObject("winner_id") != null ? rs.getInt("winner_id") : null
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getGame): " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateGame(Game game) {
        if (!GameValidator.isValidGame(game)) return false;

        String sql = "UPDATE games SET current_turn = ?, status = ?, winner_id = ? WHERE game_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, game.getCurrentTurn());

            PGobject statusObj = new PGobject();
            statusObj.setType("game_status");
            statusObj.setValue(game.getStatus());
            st.setObject(2, statusObj);

            if (game.getWinnerId() != null) {
                st.setInt(3, game.getWinnerId());
            } else {
                st.setNull(3, Types.INTEGER);
            }

            st.setInt(4, game.getGameId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (updateGame): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGame(int id) {
        if (!GameValidator.isValidGameId(id)) return false;

        String sql = "DELETE FROM games WHERE game_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (deleteGame): " + e.getMessage());
            return false;
        }
    }

    @Override
    public GameDetails getFullGameDetails(int gameId) {
        String sql = "SELECT g.game_id, g.status, g.winner_id, " +
                "p1.name AS player1_name, p2.name AS player2_name, " +
                "s.ship_id, s.player_id AS ship_player_id, s.x AS ship_x, s.y AS ship_y, s.type AS ship_type, s.size, s.orientation, s.sunk, " +
                "m.move_id, m.player_id AS move_player_id, m.x AS move_x, m.y AS move_y, m.result, m.move_time " +
                "FROM games g " +
                "JOIN players p1 ON g.player1_id = p1.player_id " +
                "JOIN players p2 ON g.player2_id = p2.player_id " +
                "LEFT JOIN ships s ON g.game_id = s.game_id " +
                "LEFT JOIN moves m ON g.game_id = m.game_id " +
                "WHERE g.game_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, gameId);
            ResultSet rs = st.executeQuery();

            List<Ship> ships = new ArrayList<>();
            List<Move> moves = new ArrayList<>();
            GameDetails gameDetails = null;

            while (rs.next()) {
                if (gameDetails == null) {
                    gameDetails = EntityFactory.createGameDetails(
                            rs.getInt("game_id"),
                            rs.getString("status"),
                            rs.getObject("winner_id") != null ? rs.getInt("winner_id") : null,
                            rs.getString("player1_name"),
                            rs.getString("player2_name"),
                            ships,
                            moves
                    );
                }
                ships.add(EntityFactory.createShip(
                        rs.getInt("ship_id"),
                        rs.getInt("game_id"),
                        rs.getInt("ship_player_id"),
                        rs.getString("ship_type"),
                        rs.getInt("size"),
                        rs.getInt("ship_x"),
                        rs.getInt("ship_y"),
                        rs.getString("orientation"),
                        rs.getBoolean("sunk")
                ));

                if (!rs.wasNull() && rs.getObject("move_id") != null) {
                    moves.add(EntityFactory.createMove(
                            rs.getInt("move_id"),
                            rs.getInt("game_id"),
                            rs.getInt("move_player_id"),
                            rs.getInt("move_x"),
                            rs.getInt("move_y"),
                            rs.getString("result"),
                            rs.getTimestamp("move_time") != null ? rs.getTimestamp("move_time").toLocalDateTime() : null
                    ));
                }
            }
            return gameDetails;
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getFullGameDetails): " + e.getMessage());
            return null;
        }
    }
}
