package validators;

import models.Tournament;

public class TournamentValidator {

    public static boolean isValidTournament(Tournament tournament) {
        if (tournament == null) return false;
        return isValidTournamentId(tournament.getTournamentId()) &&
                isValidName(tournament.getName()) &&
                isValidStatus(tournament.getStatus());
    }

    public static boolean isValidTournamentId(int tournamentId) {
        return tournamentId > 0;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidStatus(String status) {
        return status != null && (status.equals("UPCOMING") || status.equals("ONGOING") || status.equals("FINISHED"));
    }

    public static boolean isValid(Tournament tournament) {
        return tournament != null &&
                tournament.getName() != null && !tournament.getName().trim().isEmpty() &&
                tournament.getStartDate() != null &&
                tournament.getEndDate() != null &&
                tournament.getStartDate().isBefore(tournament.getEndDate());
    }

    public static void validateTournament(Tournament tournament) {
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament cannot be null.");
        }
        if (tournament.getName() == null || tournament.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be empty.");
        }
        if (tournament.getStartDate() == null || tournament.getEndDate() == null) {
            throw new IllegalArgumentException("Tournament dates cannot be null.");
        }
        if (tournament.getStartDate().isAfter(tournament.getEndDate())) {
            throw new IllegalArgumentException("Tournament start date must be before end date.");
        }
    }
}
