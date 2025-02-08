package factories;

import models.*;

import java.time.LocalDateTime;
import java.util.List;

public class EntityFactory {
    public static Player createPlayer(int playerId, String name, int rating, int gamesPlayed, int wins, int losses) {
        return new Player(playerId, name, rating, gamesPlayed, wins, losses);
    }

    public static Ship createShip(int shipId, int gameId, int playerId, int size, int startX, int startY,
                                  String orientation, boolean sunk) {
        return new Ship(shipId, gameId, playerId,size, startX, startY, orientation, sunk);
    }

    public static Move createMove(int moveId, int gameId, int playerId, int x, int y, String result, LocalDateTime moveTime) {
        return new Move(moveId, gameId, playerId, x, y, result, moveTime);
    }

    public static GameDetails createGameDetails(int gameId, String status, Integer winnerId, String player1Name, String player2Name, List<Ship> ships, List<Move> moves) {
        return new GameDetails(gameId, status, winnerId, player1Name, player2Name, ships, moves);
    }

    public static Game createGame(int gameId, int player1Id, int player2Id, int currentTurn, String status, Integer winnerId) {
        return new Game(gameId, player1Id, player2Id, currentTurn, status, winnerId);
    }


}
