package controllers;

import controllers.interfaces.IPlayerController;
import repositories.PlayerRepository;
import models.Player;

import java.util.List;

public class PlayerController implements IPlayerController {
    private final PlayerRepository playerRepo;

    public PlayerController(PlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    public boolean createPlayer(String name, int rating) {
        Player player = new Player(0, name, rating, 0, 0, 0);
        return playerRepo.createPlayer(player);
    }

    @Override
    public Player getPlayer(int playerId) {
        return playerRepo.getPlayer(playerId);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepo.getAllPlayers();
    }

    @Override
    public boolean updatePlayer(Player player) {
        return playerRepo.updatePlayer(player);
    }

    @Override
    public boolean deletePlayer(int playerId) {
        return playerRepo.deletePlayer(playerId);
    }
}
