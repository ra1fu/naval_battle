package repositories;

import data.interfaces.IDB;
import exceptions.PlayerNotFoundException;
import models.Player;
import repositories.interfaces.IPlayerRepository;
import factories.EntityFactory;
import validators.PlayerValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository implements IPlayerRepository {
    private final IDB db;

    public PlayerRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createPlayer(Player player) {
        if (!PlayerValidator.isValid(player)) {
            throw new IllegalArgumentException("Invalid player data! Please check name, rating, and stats.");
        }

        String sql = "INSERT INTO players(name, rating, games_played, wins, losses) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, player.getName());
            st.setInt(2, player.getRating());
            st.setInt(3, player.getGamesPlayed());
            st.setInt(4, player.getWins());
            st.setInt(5, player.getLosses());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Player creation failed, no rows were affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    player.setPlayerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Player creation failed, ID could not be obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error (createPlayer): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Player getPlayer(int playerId) {
        String sql = "SELECT * FROM players WHERE player_id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, playerId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Player(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getInt("games_played"),
                        rs.getInt("wins"),
                        rs.getInt("losses")
                );
            } else {
                throw new PlayerNotFoundException("Player with ID " + playerId + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Error (getPlayer): " + e.getMessage());
        }
    }

    @Override
    public List<Player> getAllPlayers() {
        String sql = "SELECT player_id, name, rating, games_played, wins, losses FROM players";
        List<Player> players = new ArrayList<>();

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                players.add(EntityFactory.createPlayer(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getInt("games_played"),
                        rs.getInt("wins"),
                        rs.getInt("losses")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getAllPlayers): " + e.getMessage());
        }
        return players;
    }

    @Override
    public boolean updatePlayer(Player player) {
        String sql = "UPDATE players SET name = ?, rating = ?, games_played = ?, wins = ?, losses = ? WHERE player_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, player.getName());
            st.setInt(2, player.getRating());
            st.setInt(3, player.getGamesPlayed());
            st.setInt(4, player.getWins());
            st.setInt(5, player.getLosses());
            st.setInt(6, player.getPlayerId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (updatePlayer): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePlayer(int id) {
        String sql = "DELETE FROM players WHERE player_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (deletePlayer): " + e.getMessage());
            return false;
        }
    }
}
