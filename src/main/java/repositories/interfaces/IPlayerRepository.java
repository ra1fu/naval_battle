package repositories.interfaces;

import models.Player;
import java.util.List;

public interface IPlayerRepository {
    boolean createPlayer(Player player);
    Player getPlayer(int id);
    List<Player> getAllPlayers();
    boolean updatePlayer(Player player);
    boolean deletePlayer(int id);
}

