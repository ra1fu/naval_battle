package controllers.interfaces;

import models.Game;

public interface IGameController {
    Game startGame(int player1Id, int player2Id);
    boolean makeMove(int gameId, int playerId, int x, int y);
    Game getGameDetails(int gameId);
}
