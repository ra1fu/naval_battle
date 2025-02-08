package validators;

import models.Player;

public class PlayerValidator {

    public static boolean isValid(Player player) {
        return player != null &&
                player.getName() != null && !player.getName().trim().isEmpty() &&
                player.getRating() >= 0 &&
                player.getGamesPlayed() >= 0 &&
                player.getWins() >= 0 &&
                player.getLosses() >= 0 &&
                player.getWins() <= player.getGamesPlayed() &&
                player.getLosses() <= player.getGamesPlayed();
    }

    public static void validatePlayer(Player player) throws IllegalArgumentException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (player.getName() == null || player.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }
        if (player.getRating() < 0) {
            throw new IllegalArgumentException("Player rating must be non-negative.");
        }
        if (player.getGamesPlayed() < 0) {
            throw new IllegalArgumentException("Games played must be non-negative.");
        }
        if (player.getWins() < 0) {
            throw new IllegalArgumentException("Wins must be non-negative.");
        }
        if (player.getLosses() < 0) {
            throw new IllegalArgumentException("Losses must be non-negative.");
        }
        if (player.getWins() > player.getGamesPlayed()) {
            throw new IllegalArgumentException("Wins cannot be greater than games played.");
        }
        if (player.getLosses() > player.getGamesPlayed()) {
            throw new IllegalArgumentException("Losses cannot be greater than games played.");
        }
    }
}
