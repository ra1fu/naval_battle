package repositories;

import data.interfaces.IDB;
import models.Ship;
import repositories.interfaces.IShipRepository;
import factories.EntityFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipRepository implements IShipRepository {
    private final IDB db;

    public ShipRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createShip(Ship ship) {
        String sql = "INSERT INTO ships(game_id, player_id, size, start_x, start_y, orientation, sunk) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, ship.getGameId());
            st.setInt(2, ship.getPlayerId());
            st.setInt(3, ship.getSize());
            st.setInt(4, ship.getStartX());
            st.setInt(5, ship.getStartY());
            st.setString(6, ship.getOrientation());
            st.setBoolean(7, ship.isSunk());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Ship creation failed, nothing changed");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ship.setShipId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Ship creation error, the ID cannot be received.");
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error (createShip): " + e.getMessage());
            return false;
        }
    }

    @Override
    public Ship getShip(int id) {
        String sql = "SELECT ship_id, game_id, player_id, size, start_x, start_y, orientation, sunk FROM ships WHERE ship_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return EntityFactory.createShip(
                        rs.getInt("ship_id"),
                        rs.getInt("game_id"),
                        rs.getInt("player_id"),
                        rs.getInt("size"),
                        rs.getInt("start_x"),
                        rs.getInt("start_y"),
                        rs.getString("orientation"),
                        rs.getBoolean("sunk")
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getShip): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Ship> getShipsByGameAndPlayer(int gameId, int playerId) {
        String sql = "SELECT ship_id, game_id, player_id, size, start_x, start_y, orientation, sunk FROM ships WHERE game_id = ? AND player_id = ?";
        List<Ship> ships = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, gameId);
            st.setInt(2, playerId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ships.add(EntityFactory.createShip(
                        rs.getInt("ship_id"),
                        rs.getInt("game_id"),
                        rs.getInt("player_id"),
                        rs.getInt("size"),
                        rs.getInt("start_x"),
                        rs.getInt("start_y"),
                        rs.getString("orientation"),
                        rs.getBoolean("sunk")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error (getShipsByGameAndPlayer): " + e.getMessage());
        }
        return ships;
    }

    @Override
    public boolean updateShip(Ship ship) {
        String sql = "UPDATE ships SET game_id = ?, player_id = ?, size = ?, start_x = ?, start_y = ?, orientation = ?, sunk = ? WHERE ship_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, ship.getGameId());
            st.setInt(2, ship.getPlayerId());
            st.setInt(3, ship.getSize());
            st.setInt(4, ship.getStartX());
            st.setInt(5, ship.getStartY());
            st.setString(6, ship.getOrientation());
            st.setBoolean(7, ship.isSunk());
            st.setInt(8, ship.getShipId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (updateShip): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteShip(int id) {
        String sql = "DELETE FROM ships WHERE ship_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error (deleteShip): " + e.getMessage());
            return false;
        }
    }
}
