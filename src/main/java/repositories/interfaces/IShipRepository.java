package repositories.interfaces;

import models.Ship;
import java.util.List;

public interface IShipRepository {
    boolean createShip(Ship ship);
    Ship getShip(int id);
    List<Ship> getShipsByGameAndPlayer(int gameId, int playerId);
    boolean updateShip(Ship ship);
    boolean deleteShip(int shipId);

}