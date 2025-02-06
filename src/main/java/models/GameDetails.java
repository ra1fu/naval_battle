package models;

import java.util.List;

public class GameDetails {
    private int gameId;
    private String status;
    private int winnerId;
    private String player1Name;
    private String player2Name;
    private List<Ship> ships;
    private List<Move> moves;

    public GameDetails(int gameId, String status, int winnerId,
                       String player1Name, String player2Name,
                       List<Ship> ships, List<Move> moves) {
        this.gameId = gameId;
        this.status = status;
        this.winnerId = winnerId;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.ships = ships;
        this.moves = moves;
    }

    public int getGameId() {
        return gameId;
    }

    public String getStatus() {
        return status;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "GameDetails{" +
                "gameId=" + gameId +
                ", status='" + status + '\'' +
                ", winnerId=" + winnerId +
                ", player1Name='" + player1Name + '\'' +
                ", player2Name='" + player2Name + '\'' +
                ", ships=" + ships +
                ", moves=" + moves +
                '}';
    }
}
