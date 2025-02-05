package models;

import java.time.LocalDateTime;

public class Move {
    private int moveId;
    private int gameId;
    private int playerId;
    private int x;
    private int y;
    private String result;
    private LocalDateTime moveTime;

    public Move(int gameId, int playerId, int x, int y, String result, LocalDateTime moveTime) {
        setMoveId(gameId);
        setPlayerId(playerId);
        setX(x);
        setY(y);
        setResult(result);
        setMoveTime(moveTime);
    }

    public Move(int moveId, int gameId, int playerId, int x, int y, String result, LocalDateTime moveTime) {
        setMoveId(moveId);
        setGameId(gameId);
        setPlayerId(playerId);
        setX(x);
        setY(y);
        setResult(result);
        setMoveTime(moveTime);
    }

    public int getMoveId() {
        return moveId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getMoveTime() {
        return moveTime;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }

    public void setMoveTime(LocalDateTime moveTime) {
        this.moveTime = moveTime;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
