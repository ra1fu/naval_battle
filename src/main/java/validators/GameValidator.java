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
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public static boolean validatePlayers(int player1Id, int player2Id) {
        if (player1Id <= 0 || player2Id <= 0) {
            System.out.println("Validation Error: Player IDs must be greater than zero.");
            return false;
        }
        if (player1Id == player2Id) {
            System.out.println("Validation Error: Players must have different IDs.");
            return false;
        }
        return true;
    }

    public static boolean validateMove(Game game, int playerId, int x, int y) {
        if (game == null) {
            System.out.println("Validation Error: Game does not exist.");
            return false;
        }
        if (game.getStatus().equals("FINISHED")) {
            System.out.println("Validation Error: The game is already finished.");
            return false;
        }
        if (!game.isPlayerTurn(playerId)) {
            System.out.println("Validation Error: It is not this player's turn.");
            return false;
        }
        if (x < 0 || y < 0) {
            System.out.println("Validation Error: Coordinates must be non-negative.");
            return false;
        }
        return true;
    }
}
