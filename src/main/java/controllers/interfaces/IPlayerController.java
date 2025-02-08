package controllers.interfaces;

import models.Player;

import java.util.List;

public interface IPlayerController {
    boolean createPlayer(String name, int rating);
    Player getPlayer(int playerId);
    List<Player> getAllPlayers();
    boolean updatePlayer(Player player);
    boolean deletePlayer(int playerId);
}
