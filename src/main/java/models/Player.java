package models;

public class Player {
    private int playerId;
    private String name;
    private int rating;
    private int gamesPlayed;
    private int wins;
    private int losses;

    public Player(String name) {
        this.name = name;
        this.rating = 1000;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
    }

    public Player(int playerId, String name, int rating, int gamesPlayed, int wins, int losses) {
        this.playerId = playerId;
        this.name = name;
        this.rating = rating;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
    }

    public int getPlayerId() { return playerId; }
    public String getName() { return name; }
    public int getRating() { return rating; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    public void setName(String name) { this.name = name; }
    public void setRating(int rating) { this.rating = rating; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }

    public void incrementGamesPlayed() { this.gamesPlayed++; }
    public void incrementWins() { this.wins++; }
    public void incrementLosses() { this.losses++; }
    public void updateRating(int delta) { this.rating += delta; }
}
