package services;

import exceptions.GameNotFoundException;
import exceptions.InvalidDataException;
import models.Game;
import models.Move;
import models.Player;
import repositories.interfaces.IGameRepository;
import repositories.interfaces.IMoveRepository;
import repositories.interfaces.IPlayerRepository;
import validators.GameValidator;
import validators.PlayerValidator;

import java.time.LocalDateTime;

public class GameService {
    private final IGameRepository gameRepository;
    private final IMoveRepository moveRepository;

    public GameService(IGameRepository gameRepository, IMoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
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

    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepository.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game with ID " + gameId + " not found.");
        }

        if (!game.isPlayerTurn(playerId)) {
            throw new InvalidDataException("It's not player " + playerId + "'s turn.");
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
            throw new RuntimeException("Failed to create move.");
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
}
