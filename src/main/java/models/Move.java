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
        this.gameId = gameId;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.result = result;
        this.moveTime = moveTime;
    }

    public Move(int moveId, int gameId, int playerId, int x, int y, String result, LocalDateTime moveTime) {
        this.moveId = moveId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.result = result;
        this.moveTime = moveTime;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(LocalDateTime moveTime) {
        this.moveTime = moveTime;
    }

    @Override
    public String toString() {
        return "Move{" +
                "moveId=" + moveId +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", x=" + x +
                ", y=" + y +
                ", result='" + result + '\'' +
                ", moveTime=" + moveTime +
                '}';
    }
}