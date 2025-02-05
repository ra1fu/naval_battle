package repositories;

import data.interfaces.IDB;
import models.Game;
import repositories.interfaces.IGameRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.util.PGobject; // Импорт для работы с ENUM

public class GameRepository implements IGameRepository {
    private final IDB db;  // Dependency Injection

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

            // Приведение ENUM к PostgreSQL game_status
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
}
