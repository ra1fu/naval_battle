package validators;

import models.Game;
import models.Move;
import models.Ship;

import java.util.List;

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

    public static boolean isPlacementValid(int x, int y, int size, String orientation, List<Ship> existingShips) {
        if (x < 0 || y < 0 || x >= 10 || y >= 10) {
            return false;
        }

        if (orientation.equals("HORIZONTAL") && (x + size > 10)) {
            return false;
        }
        if (orientation.equals("VERTICAL") && (y + size > 10)) {
            return false;
        }

        for (Ship ship : existingShips) {
            for (int i = 0; i < ship.getSize(); i++) {
                int shipX = ship.getStartX();
                int shipY = ship.getStartY();

                if (ship.getOrientation().equals("HORIZONTAL")) {
                    shipX += i;
                } else {
                    shipY += i;
                }

                for (int j = 0; j < size; j++) {
                    int newX = x;
                    int newY = y;

                    if (orientation.equals("HORIZONTAL")) {
                        newX += j;
                    } else {
                        newY += j;
                    }

                    if (shipX == newX && shipY == newY) {
                        return false;
                    }
                }
            }
        }

        return isSurroundingCellsAvailable(x, y, size, orientation, existingShips);
    }

    private static boolean isSurroundingCellsAvailable(int x, int y, int size, String orientation, List<Ship> existingShips) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int j = 0; j < size; j++) {
            int checkX = x;
            int checkY = y;

            if (orientation.equals("HORIZONTAL")) {
                checkX += j;
            } else {
                checkY += j;
            }

            for (int i = 0; i < 8; i++) {
                int nx = checkX + dx[i];
                int ny = checkY + dy[i];

                if (nx >= 0 && nx < 10 && ny >= 0 && ny < 10) {
                    for (Ship ship : existingShips) {
                        for (int k = 0; k < ship.getSize(); k++) {
                            int shipX = ship.getStartX();
                            int shipY = ship.getStartY();

                            if (ship.getOrientation().equals("HORIZONTAL")) {
                                shipX += k;
                            } else {
                                shipY += k;
                            }

                            if (shipX == nx && shipY == ny) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean validatePlayers(boolean player1Exists, boolean player2Exists) {
        return player1Exists && player2Exists;
    }
}
