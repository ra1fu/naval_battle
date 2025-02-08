package controllers;

import controllers.interfaces.IGameController;
import repositories.GameRepository;
import repositories.MoveRepository;
import repositories.PlayerRepository;
import models.Game;
import models.Move;
import models.Player;

public class GameController implements IGameController {
    private final GameRepository gameRepo;
    private final MoveRepository moveRepo;
    private final PlayerRepository playerRepo;

    public GameController(GameRepository gameRepo, MoveRepository moveRepo, PlayerRepository playerRepo) {
        this.gameRepo = gameRepo;
        this.moveRepo = moveRepo;
        this.playerRepo = playerRepo;
    }

    @Override
    public Game startGame(int player1Id, int player2Id) {
        System.out.println("Starting game with player1Id=" + player1Id + ", player2Id=" + player2Id);

        Player player1 = playerRepo.getPlayer(player1Id);
        Player player2 = playerRepo.getPlayer(player2Id);

        if (player1 == null || player2 == null) {
            System.out.println("Error: One of the players was not found.");
            return null;
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);
        boolean success = gameRepo.createGame(game);

        if (!success) {
            System.out.println("Error: Failed to create the game in the database.");
            return null;
        }

        System.out.println("Game successfully created with ID: " + game.getGameId());
        return game;
    }

    @Override
    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepo.getGame(gameId);
        if (game == null) {
            System.out.println("Error: Game not found.");
            return false;
        }

        if (!game.isPlayerTurn(playerId)) {
            System.out.println("Error: It is not your turn.");
            return false;
        }

        boolean hit = gameRepo.checkHit(gameId, x, y);
        if (hit) {
            gameRepo.updateShipStatus(gameId, x, y);
        }

        if (gameRepo.isGameOver(gameId)) {
            game.setStatus("FINISHED");
            game.setWinnerId(playerId);
            gameRepo.updateGame(game);
            System.out.println("Game over. Winner: " + game.getWinnerId());
            return true;
        }

        game.switchTurn();
        gameRepo.updateGame(game);
        return true;
    }

    @Override
    public Game getGameDetails(int gameId) {
        return gameRepo.getGame(gameId);
    }
}
