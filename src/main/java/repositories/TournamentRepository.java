package repositories;

import data.interfaces.IDB;
import models.Player;
import models.Tournament;
import repositories.interfaces.ITournamentRepository;
import validators.TournamentValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TournamentRepository implements ITournamentRepository {
    private final IDB db;

    public TournamentRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createTournament(Tournament tournament) {
        if (!TournamentValidator.isValidTournament(tournament)) {
            System.out.println("Validation Error: Invalid tournament data.");
            return false;
        }

        String sql = "INSERT INTO tournaments(name, start_date, end_date, status) VALUES (?, ?, ?, 'PLANNED') RETURNING tournament_id";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, tournament.getName());
            st.setTimestamp(2, Timestamp.valueOf(tournament.getStartDate()));
            st.setTimestamp(3, Timestamp.valueOf(tournament.getEndDate()));

            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (createTournament): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addPlayerToTournament(int tournamentId, int playerId) {
        if (!TournamentValidator.isValidTournamentId(tournamentId) || playerId <= 0) {
            System.out.println("Validation Error: Invalid tournament ID or player ID.");
            return false;
        }

        String checkTournamentSql = "SELECT COUNT(*) FROM tournaments WHERE tournament_id = ?";
        String checkPlayerSql = "SELECT COUNT(*) FROM tournament_players WHERE tournament_id = ? AND player_id = ?";
        String insertSql = "INSERT INTO tournament_players(tournament_id, player_id) VALUES (?, ?)";

        try (Connection con = db.getConnection()) {
            try (PreparedStatement checkTournamentSt = con.prepareStatement(checkTournamentSql)) {
                checkTournamentSt.setInt(1, tournamentId);
                ResultSet rsTournament = checkTournamentSt.executeQuery();
                if (rsTournament.next() && rsTournament.getInt(1) == 0) {
                    System.out.println("Error: Tournament ID " + tournamentId + " does not exist.");
                    return false;
                }
            }

            try (PreparedStatement checkPlayerSt = con.prepareStatement(checkPlayerSql)) {
                checkPlayerSt.setInt(1, tournamentId);
                checkPlayerSt.setInt(2, playerId);
                ResultSet rsPlayer = checkPlayerSt.executeQuery();
                if (rsPlayer.next() && rsPlayer.getInt(1) > 0) {
                    System.out.println("Error: Player is already in the tournament.");
                    return false;
                }
            }

            try (PreparedStatement insertSt = con.prepareStatement(insertSql)) {
                insertSt.setInt(1, tournamentId);
                insertSt.setInt(2, playerId);
                return insertSt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (addPlayerToTournament): " + e.getMessage());
            return false;
        }
    }


    @Override
    public Tournament getTournament(int tournamentId) {
        if (!TournamentValidator.isValidTournamentId(tournamentId)) {
            System.out.println("Validation Error: Invalid tournament ID.");
            return null;
        }

        String sql = "SELECT * FROM tournaments WHERE tournament_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, tournamentId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("name"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getTournament): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Tournament> getAllTournaments() {
        String sql = "SELECT tournament_id, name, start_date, end_date FROM tournaments";
        List<Tournament> tournaments = new ArrayList<>();

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int tournamentId = rs.getInt("tournament_id");
                String name = rs.getString("name");

                Timestamp startTimestamp = rs.getTimestamp("start_date");
                Timestamp endTimestamp = rs.getTimestamp("end_date");

                LocalDateTime startDate = (startTimestamp != null) ? startTimestamp.toLocalDateTime() : null;
                LocalDateTime endDate = (endTimestamp != null) ? endTimestamp.toLocalDateTime() : null;

                tournaments.add(new Tournament(tournamentId, name, startDate, endDate));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getAllTournaments): " + e.getMessage());
        }
        return tournaments;
    }


    @Override
    public List<Player> getPlayersByTournament(int tournamentId) {
        String sql = "SELECT p.player_id, p.name, p.rating FROM players p " +
                "JOIN tournament_players tp ON p.player_id = tp.player_id " +
                "WHERE tp.tournament_id = ?";
        List<Player> players = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, tournamentId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                players.add(new Player(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getInt("rating")
                ));
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (getPlayersByTournament): " + e.getMessage());
        }
        return players;
    }

    @Override
    public boolean updateTournamentStatus(int tournamentId, String status) {
        if (!TournamentValidator.isValidTournamentId(tournamentId) || !TournamentValidator.isValidStatus(status)) {
            System.out.println("Validation Error: Invalid tournament ID or status.");
            return false;
        }

        String sql = "UPDATE tournaments SET status = ?::tournament_status WHERE tournament_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, tournamentId);

            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (updateTournamentStatus): " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteTournament(int tournamentId) {
        if (!TournamentValidator.isValidTournamentId(tournamentId)) {
            System.out.println("Validation Error: Invalid tournament ID.");
            return false;
        }

        String deletePlayersSql = "DELETE FROM tournament_players WHERE tournament_id = ?";
        String deleteTournamentSql = "DELETE FROM tournaments WHERE tournament_id = ?";

        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stPlayers = con.prepareStatement(deletePlayersSql)) {
                stPlayers.setInt(1, tournamentId);
                stPlayers.executeUpdate();
            }

            try (PreparedStatement stTournament = con.prepareStatement(deleteTournamentSql)) {
                stTournament.setInt(1, tournamentId);
                int affectedRows = stTournament.executeUpdate();
                con.commit();
                return affectedRows > 0;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL Error (deleteTournament): " + e.getMessage());
            return false;
        }
    }

}
