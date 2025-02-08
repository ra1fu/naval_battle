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
}
