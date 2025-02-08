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
}
