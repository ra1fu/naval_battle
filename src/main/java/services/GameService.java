package services;

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
        boolean player1Exists = gameRepository.getGame(player1Id) != null;
        boolean player2Exists = gameRepository.getGame(player2Id) != null;

        if (!GameValidator.validatePlayers(player1Exists, player2Exists)) {
            return null;
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);

        if (!GameValidator.isValidGame(game)) {
            return null;
        }

        boolean success = gameRepository.createGame(game);
        return success ? game : null;
    }

    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepository.getGame(gameId);
        if (game == null || !game.isPlayerTurn(playerId)) {
            return false;
        }

        boolean hit = gameRepository.checkHit(gameId, x, y);
        if (hit) {
            gameRepository.updateShipStatus(gameId, x, y);
        }

        Move move = new Move(0, gameId, playerId, x, y, hit ? "HIT" : "MISS", LocalDateTime.now());

        if (!GameValidator.validateMove(move)) {
            return false;
        }

        boolean moveCreated = moveRepository.createMove(move);
        if (!moveCreated) {
            return false;
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
