package validators;

import models.Game;

public class GameValidator {

    public static boolean isValidGame(Game game) {
        if (game == null) return false;
        return isValidGameId(game.getGameId()) &&
                isValidPlayerId(game.getPlayer1Id()) &&
                isValidPlayerId(game.getPlayer2Id()) &&
                game.getStatus() != null && !game.getStatus().isEmpty();
    }

    public static boolean isValidGameId(int gameId) {
        return gameId > 0;
    }

    public static boolean isValidPlayerId(int playerId) {
        return playerId > 0;
    }

    public static boolean isValidMove(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10; // Assuming 10x10 board
    }
}
