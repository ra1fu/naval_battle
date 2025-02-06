package models;

public class Ship {
    private int shipId;
    private int gameId;
    private int playerId;
    private String type;
    private int size;
    private int startX, startY;
    private String orientation;
    private boolean sunk;
    private int hits;

    public Ship(int shipId, int gameId, int playerId, String type, int size, int startX, int startY, String orientation, boolean sunk) {
        this.shipId = shipId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.type = type;
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.orientation = orientation;
        this.sunk = sunk;
        this.hits = 0;
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

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void hit() {
        if (!sunk) {
            hits++;
            if (hits >= size) {
                sunk = true;
            }
        }
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipId=" + shipId +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", startX=" + startX +
                ", startY=" + startY +
                ", orientation='" + orientation + '\'' +
                ", sunk=" + sunk +
                ", hits=" + hits +
                '}';
    }
}