package services;

import models.Tournament;
import repositories.interfaces.ITournamentRepository;
import validators.TournamentValidator;

import java.util.List;

public class TournamentService {
    private final ITournamentRepository tournamentRepository;

    public TournamentService(ITournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public boolean createTournament(Tournament tournament) {
        if (!TournamentValidator.isValid(tournament)) {
            throw new IllegalArgumentException("Invalid tournament data.");
        }

        return tournamentRepository.createTournament(tournament);
    }

    public Tournament getTournament(int tournamentId) {
        return tournamentRepository.getTournament(tournamentId);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.getAllTournaments();
    }

    public boolean addPlayerToTournament(int tournamentId, int playerId) {
        return tournamentRepository.addPlayerToTournament(tournamentId, playerId);
    }

    public boolean updateTournamentStatus(int tournamentId, String status) {
        return tournamentRepository.updateTournamentStatus(tournamentId, status);
    }

    public boolean deleteTournament(int tournamentId) {
        return tournamentRepository.deleteTournament(tournamentId);
    }
}
