package repositories.interfaces;

import models.TournamentResult;
import java.util.List;

public interface ITournamentResultRepository {
    boolean recordMatchResult(TournamentResult result);
    List<TournamentResult> getResultsByTournament(int tournamentId);
    List<TournamentResult> getResultsByPlayer(int playerId);
    TournamentResult getMatchResult(int resultId);
}
