package controllers.interfaces;

import models.Tournament;
import models.TournamentResult;

import java.time.LocalDateTime;
import java.util.List;

public interface ITournamentController {
    boolean createTournament(String name, LocalDateTime startDate, LocalDateTime endDate);
    boolean addPlayerToTournament(int tournamentId, int playerId);
    Tournament getTournament(int tournamentId);
    List<Tournament> getAllTournaments();
    List<TournamentResult> getTournamentResults(int tournamentId);
}
