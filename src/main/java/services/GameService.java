package services;

import exceptions.GameNotFoundException;
import exceptions.InvalidDataException;
import models.Game;
import models.Move;
import models.Ship;
import repositories.interfaces.IGameRepository;
import repositories.interfaces.IMoveRepository;
import repositories.interfaces.IShipRepository;
import validators.GameValidator;

import java.time.LocalDateTime;
import java.util.List;

public class GameService {
    private final IGameRepository gameRepository;
    private final IMoveRepository moveRepository;
    private final IShipRepository shipRepository;

    public GameService(IGameRepository gameRepository, IMoveRepository moveRepository, IShipRepository shipRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
        this.shipRepository = shipRepository;
    }

    public Game startGame(int player1Id, int player2Id) {
        if (player1Id <= 0 || player2Id <= 0) {
            throw new InvalidDataException("Invalid player IDs provided.");
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);

        if (!GameValidator.isValidGame(game)) {
            throw new InvalidDataException("Invalid game data.");
        }

        boolean success = gameRepository.createGame(game);
        if (!success) {
            throw new RuntimeException("Failed to create game.");
        }

        return game;
    }

    public Game getGameById(int gameId) {
        if (!GameValidator.isValidGameId(gameId)) {
            throw new InvalidDataException("Invalid game ID: " + gameId);
        }

        Game game = gameRepository.getGameById(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game with ID " + gameId + " not found.");
        }

        return game;
    }

    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepository.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException("Игра с ID " + gameId + " не найдена.");
        }

        if (!game.isPlayerTurn(playerId)) {
            throw new InvalidDataException("It's not the player's turn right now " + playerId + ".");
        }

        boolean hit = gameRepository.checkHit(gameId, x, y);
        if (hit) {
            gameRepository.updateShipStatus(gameId, x, y);
        }

        Move move = new Move(0, gameId, playerId, x, y, hit ? "HIT" : "MISS", LocalDateTime.now());

        if (!GameValidator.validateMove(move)) {
            throw new InvalidDataException("Invalid move data.");
        }

        boolean moveCreated = moveRepository.createMove(move);
        if (!moveCreated) {
            throw new RuntimeException("Error when creating a move.");
        }

        if (gameRepository.isGameOver(gameId)) {
            game.setStatus("FINISHED");
            game.setWinnerId(playerId);
            gameRepository.updateGame(game);
            return true;
        }

        game.switchTurn();
        gameRepository.updateGame(game);
        return true;
    }

    public boolean isGameOver(int gameId) {
        if (!GameValidator.isValidGameId(gameId)) {
            return false;
        }
        return gameRepository.isGameOver(gameId);
    }
}
