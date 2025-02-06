package repositories;

import data.interfaces.IDB;
import models.Move;
import repositories.interfaces.IMoveRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository implements IMoveRepository {
    private final IDB db;

    public MoveRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createMove(Move move) {

        String checkGameSql = "SELECT COUNT(*) FROM games WHERE game_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement checkStmt = con.prepareStatement(checkGameSql)) {
            checkStmt.setInt(1, move.getGameId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Game ID " + move.getGameId() + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (createMove - game check): " + e.getMessage());
            return false;
        }

        String sql = "INSERT INTO moves(game_id, player_id, x, y, result, move_time) " +
                "VALUES (?, ?, ?, ?, ?::move_result, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, move.getGameId());
            st.setInt(2, move.getPlayerId());
            st.setInt(3, move.getX());
            st.setInt(4, move.getY());
            st.setString(5, move.getResult());
            st.setTimestamp(6, Timestamp.valueOf(move.getMoveTime()));

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("The move creation failed and no rows were affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    move.setMoveId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Move creation failed, failed to get ID.");
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error (createMove): " + e.getMessage());
            return false;
        }
    }


    @Override
    public Move getMove(int id) {
        String sql = "SELECT move_id, game_id, player_id, x, y, result, move_time FROM moves WHERE move_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Move(
                        rs.getInt("move_id"),
                        rs.getInt("game_id"),
                        rs.getInt("player_id"),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getString("result"),
                        rs.getTimestamp("move_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getMove): " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Move> getMovesByGameId(int gameId) {
        String sql = "SELECT move_id, game_id, player_id, x, y, result, move_time FROM moves WHERE game_id = ?";
        List<Move> moves = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, gameId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Move move = new Move(
                        rs.getInt("move_id"),
                        rs.getInt("game_id"),
                        rs.getInt("player_id"),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getString("result"),
                        rs.getTimestamp("move_time").toLocalDateTime()
                );
                moves.add(move);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getMovesByGameId): " + e.getMessage());
        }

        return moves;
    }

    @Override
    public boolean updateMove(Move move) {
        String sql = "UPDATE moves SET game_id = ?, player_id = ?, x = ?, y = ?, result = ?::move_result, move_time = ? WHERE move_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, move.getGameId());
            st.setInt(2, move.getPlayerId());
            st.setInt(3, move.getX());
            st.setInt(4, move.getY());
            st.setString(5, move.getResult()); // Приведение типа в SQL
            st.setTimestamp(6, Timestamp.valueOf(move.getMoveTime()));
            st.setInt(7, move.getMoveId());

            int affectedRows = st.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (updateMove): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteMove(int id) {
        String sql = "DELETE FROM moves WHERE move_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            int affectedRows = st.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (deleteMove): " + e.getMessage());
            return false;
        }
    }
}