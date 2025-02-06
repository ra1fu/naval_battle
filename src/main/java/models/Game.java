package models;

public class Game {
    private int gameId;
    private int player1Id;
    private int player2Id;
    private int currentTurn;
    private String status;
    private Integer winnerId;

    public Game(int player1Id, int player2Id) {
        setPlayer1Id(player1Id);
        setPlayer2Id(player2Id);
        setCurrentTurn(player1Id);
        setStatus("in_progress");
        setWinnerId(null);
    }

    public Game(int gameId, int player1Id, int player2Id, int currentTurn, String status, Integer winnerId) {
        setGameId(gameId);
        setPlayer1Id(player1Id);
        setPlayer2Id(player2Id);
        setCurrentTurn(currentTurn);
        setStatus(status);
        setWinnerId(winnerId);
    }

    public Game(int player1Id, int player2Id, int currentTurn, String status, Integer winnerId) {
        setPlayer1Id(player1Id);
        setPlayer2Id(player2Id);
        setCurrentTurn(currentTurn);
        setStatus(status);
        setWinnerId(winnerId);

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    public void switchTurn() {
        if (this.currentTurn == this.player1Id) {
            this.currentTurn = this.player2Id;
        } else {
            this.currentTurn = this.player1Id;
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", player1Id=" + player1Id +
                ", player2Id=" + player2Id +
                ", currentTurn=" + currentTurn +
                ", status='" + status + '\'' +
                ", winnerId=" + winnerId +
                '}';
    }
}
