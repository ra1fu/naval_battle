package repositories;

import data.interfaces.IDB;
import models.Game;
import models.GameDetails;
import models.Move;
import models.Ship;
import repositories.interfaces.IGameRepository;

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
    public boolean createGame(Game game) {
        String sql = "INSERT INTO games(player1_id, player2_id, current_turn, status, winner_id) VALUES (?, ?, ?, ?, ?)";

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

            if (affectedRows == 0) {
                throw new SQLException("The creation of the game failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("The creation of the game failed, and the ID could not be obtained.");
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error (createGame): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Game getGame(int id) {
        String sql = "SELECT game_id, player1_id, player2_id, current_turn, status, winner_id FROM games WHERE game_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Integer winnerId = rs.getInt("winner_id");
                if (rs.wasNull()) {
                    winnerId = null;
                }

                return new Game(
                        rs.getInt("game_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("current_turn"),
                        rs.getString("status"),
                        winnerId
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getGame): " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Game> getAllGames() {
        String sql = "SELECT game_id, player1_id, player2_id, current_turn, status, winner_id FROM games";
        List<Game> games = new ArrayList<>();

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Integer winnerId = rs.getInt("winner_id");
                if (rs.wasNull()) {
                    winnerId = null;
                }

                Game game = new Game(
                        rs.getInt("game_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("current_turn"),
                        rs.getString("status"),
                        winnerId
                );
                games.add(game);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getAllGames): " + e.getMessage());
        }

        return games;
    }

    @Override
    public boolean updateGame(Game game) {
        String sql = "UPDATE games SET player1_id = ?, player2_id = ?, current_turn = ?, status = ?, winner_id = ? WHERE game_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, game.getPlayer1Id());
            st.setInt(2, game.getPlayer2Id());
            st.setInt(3, game.getCurrentTurn());

            // Приведение ENUM-значения
            PGobject statusObj = new PGobject();
            statusObj.setType("game_status");
            statusObj.setValue(game.getStatus());
            st.setObject(4, statusObj);

            if (game.getWinnerId() != null) {
                st.setInt(5, game.getWinnerId());
            } else {
                st.setNull(5, Types.INTEGER);
            }

            st.setInt(6, game.getGameId());

            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (updateGame): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGame(int id) {
        String sql = "DELETE FROM games WHERE game_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (deleteGame): " + e.getMessage());
            return false;
        }
    }

    @Override
    public GameDetails getFullGameDetails(int gameId) {
        String sql = "SELECT g.game_id, g.status, g.winner_id, " +
                "p1.name AS player1_name, p2.name AS player2_name, " +
                "s.ship_id, s.player_id AS ship_player_id, s.x AS ship_x, s.y AS ship_y, s.type AS ship_type, s.sunk, " +
                "m.move_id, m.player_id AS move_player_id, m.x AS move_x, m.y AS move_y, m.result " +
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
                    gameDetails = new GameDetails(
                            rs.getInt("game_id"),
                            rs.getString("status"),
                            rs.getInt("winner_id"),
                            rs.getString("player1_name"),
                            rs.getString("player2_name"),
                            ships,
                            moves
                    );
                }

                // Check if ship_id is not NULL before adding
                int shipId = rs.getInt("ship_id");
                if (!rs.wasNull()) {
                    ships.add(new Ship(
                            shipId,
                            rs.getInt("game_id"),
                            rs.getInt("ship_player_id"),
                            rs.getInt("ship_x"),
                            rs.getInt("ship_y"),
                            rs.getString("ship_type"),
                            rs.getBoolean("sunk")
                    ));
                }

                // Check if move_id is not NULL before adding
                int moveId = rs.getInt("move_id");
                if (!rs.wasNull()) {
                    moves.add(new Move(
                            moveId,
                            rs.getInt("game_id"),
                            rs.getInt("move_player_id"),
                            rs.getInt("move_x"),
                            rs.getInt("move_y"),
                            rs.getString("result")
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
