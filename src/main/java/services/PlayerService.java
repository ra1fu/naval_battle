package services;

import models.Player;
import repositories.interfaces.IPlayerRepository;
import validators.PlayerValidator;

import java.util.List;

public class PlayerService {
    private final IPlayerRepository playerRepository;

    public PlayerService(IPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public boolean createPlayer(Player player) {
        if (!PlayerValidator.isValid(player)) {
            throw new IllegalArgumentException("Invalid player data.");
        }

        return playerRepository.createPlayer(player);
    }

    public Player getPlayer(int playerId) {
        return playerRepository.getPlayer(playerId);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    public boolean updatePlayer(Player player) {
        if (!PlayerValidator.isValid(player)) {
            throw new IllegalArgumentException("Invalid player data.");
        }

        return playerRepository.updatePlayer(player);
    }

    public boolean deletePlayer(int playerId) {
        return playerRepository.deletePlayer(playerId);
    }
}
