package validators;

import models.Game;
import models.Move;

public class GameValidator {

    public static boolean isValid(Game game) {
        return game != null &&
                game.getPlayer1Id() > 0 &&
                game.getPlayer2Id() > 0 &&
                game.getPlayer1Id() != game.getPlayer2Id() &&
                game.getCurrentTurn() > 0 &&
                (game.getStatus().equals("IN_PROGRESS") || game.getStatus().equals("FINISHED"));
    }

    public static boolean isValidGameId(int gameId) {
        return gameId > 0;
    }

    public static boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0;
    }

    public static boolean isValidGame(Game game) {
        return game != null &&
                isValidGameId(game.getGameId()) &&
                game.getPlayer1Id() > 0 &&
                game.getPlayer2Id() > 0 &&
                game.getCurrentTurn() > 0 &&
                game.getStatus() != null && !game.getStatus().trim().isEmpty();
    }

    public static boolean validateMove(Move move) {
        return move != null &&
                move.getGameId() > 0 &&
                move.getPlayerId() > 0 &&
                move.getX() >= 0 && move.getY() >= 0 &&
                (move.getResult().equals("HIT") || move.getResult().equals("MISS")) &&
                move.getMoveTime() != null;
    }

    public static boolean validatePlayers(boolean player1Exists, boolean player2Exists) {
        return player1Exists && player2Exists;
    }
}
