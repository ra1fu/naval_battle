package repositories;

import data.interfaces.IDB;
import models.TournamentResult;
import repositories.interfaces.ITournamentResultRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TournamentResultRepository implements ITournamentResultRepository {
    private final IDB db;

    public TournamentResultRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean recordMatchResult(TournamentResult result) {
        String sql = "INSERT INTO tournament_results (tournament_id, player1_id, player2_id, winner_id) VALUES (?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, result.getTournamentId());
            st.setInt(2, result.getPlayer1Id());
            st.setInt(3, result.getPlayer2Id());
            st.setInt(4, result.getWinnerId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (recordMatchResult): " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<TournamentResult> getResultsByTournament(int tournamentId) {
        String sql = "SELECT * FROM tournament_results WHERE tournament_id = ?";
        List<TournamentResult> results = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, tournamentId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                results.add(new TournamentResult(
                        rs.getInt("result_id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("winner_id"),
                        rs.getTimestamp("match_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getResultsByTournament): " + e.getMessage());
        }
        return results;
    }

    @Override
    public List<TournamentResult> getResultsByPlayer(int playerId) {
        String sql = "SELECT * FROM tournament_results WHERE player1_id = ? OR player2_id = ?";
        List<TournamentResult> results = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, playerId);
            st.setInt(2, playerId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                results.add(new TournamentResult(
                        rs.getInt("result_id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("winner_id"),
                        rs.getTimestamp("match_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getResultsByPlayer): " + e.getMessage());
        }
        return results;
    }

    @Override
    public TournamentResult getMatchResult(int resultId) {
        String sql = "SELECT * FROM tournament_results WHERE result_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, resultId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new TournamentResult(
                        rs.getInt("result_id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        rs.getInt("winner_id"),
                        rs.getTimestamp("match_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getMatchResult): " + e.getMessage());
        }
        return null;
    }
}
