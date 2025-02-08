package services;

import exceptions.InvalidDataException;
import exceptions.PlayerNotFoundException;
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

    public Player getPlayerById(int playerId) {
        if (!PlayerValidator.isValidPlayerId(playerId)) {
            throw new InvalidDataException("Invalid player ID: " + playerId);
        }

        Player player = playerRepository.getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " not found.");
        }
        return player;
    }

    public boolean deletePlayer(int playerId) {
        return playerRepository.deletePlayer(playerId);
    }
}
