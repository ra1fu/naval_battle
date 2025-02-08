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
        Player p1 = playerRepo.getPlayer(player1Id);
        Player p2 = playerRepo.getPlayer(player2Id);
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Both players must exist to start a game!");
        }

        Game game = new Game(0, player1Id, player2Id, player1Id, "IN_PROGRESS", null);
        gameRepo.createGame(game);
        return game;
    }

    @Override
    public boolean makeMove(int gameId, int playerId, int x, int y) {
        Game game = gameRepo.getGame(gameId);
        if (game == null || !game.isPlayerTurn(playerId)) {
            System.out.println("Not your turn!");
            return false;
        }

        boolean hit = gameRepo.checkHit(gameId, x, y);
        moveRepo.createMove(new Move(0, gameId, playerId, x, y, hit ? "HIT" : "MISS"));

        if (gameRepo.isGameOver(gameId)) {
            game.setStatus("FINISHED");
            game.setWinnerId(playerId);
            gameRepo.updateGame(game);
        }

        return true;
    }

    @Override
    public Game getGameDetails(int gameId) {
        return gameRepo.getGame(gameId);
    }
}
