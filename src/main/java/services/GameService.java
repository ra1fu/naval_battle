package services;

import models.Game;
import repositories.interfaces.IGameRepository;
import validators.GameValidator;

public class GameService {
    private final IGameRepository gameRepository;

    public GameService(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startGame(int player1Id, int player2Id) {
        if (!GameValidator.validatePlayers(player1Id, player2Id)) {
            throw new IllegalArgumentException("Invalid players for the game.");
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);
        boolean success = gameRepository.createGame(game);
        if (!success) {
            throw new RuntimeException("Failed to create the game.");
        }

        return game;
    }

    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepository.getGame(gameId);
        if (!GameValidator.validateMove(game, playerId, x, y)) {
            throw new IllegalArgumentException("Invalid move.");
        }

        boolean hit = gameRepository.checkHit(gameId, x, y);
        if (hit) {
            gameRepository.updateShipStatus(gameId, x, y);
        }

        if (gameRepository.isGameOver(gameId)) {
            game.setStatus("FINISHED");
            game.setWinnerId(playerId);
            gameRepository.updateGame(game);
            return true;
        }

        game.switchTurn();
        return gameRepository.updateGame(game);
    }

    public Game getGame(int gameId) {
        return gameRepository.getGame(gameId);
    }
}
