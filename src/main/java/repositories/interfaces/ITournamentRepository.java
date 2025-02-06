package repositories.interfaces;

import models.Tournament;
import models.Player;

import java.util.List;

public interface ITournamentRepository {
    boolean createTournament(Tournament tournament);
    boolean addPlayerToTournament(int tournamentId, int playerId);
    Tournament getTournament(int tournamentId);
    List<Tournament> getAllTournaments();
    List<Player> getPlayersByTournament(int tournamentId);
    boolean updateTournamentStatus(int tournamentId, String status);
    boolean deleteTournament(int tournamentId);
}
