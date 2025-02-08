package controllers;

import controllers.interfaces.ITournamentController;
import repositories.TournamentRepository;
import repositories.TournamentResultRepository;
import models.Tournament;
import models.TournamentResult;

import java.time.LocalDateTime;
import java.util.List;

public class TournamentController implements ITournamentController {
    private final TournamentRepository tournamentRepo;
    private final TournamentResultRepository resultRepo;

    public TournamentController(TournamentRepository tournamentRepo, TournamentResultRepository resultRepo) {
        this.tournamentRepo = tournamentRepo;
        this.resultRepo = resultRepo;
    }

    @Override
    public boolean createTournament(String name, LocalDateTime startDate, LocalDateTime endDate) {
        Tournament tournament = new Tournament(0, name, startDate, endDate);
        return tournamentRepo.createTournament(tournament);
    }

    @Override
    public boolean addPlayerToTournament(int tournamentId, int playerId) {
        return tournamentRepo.addPlayerToTournament(tournamentId, playerId);
    }

    @Override
    public Tournament getTournament(int tournamentId) {
        return tournamentRepo.getTournament(tournamentId);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepo.getAllTournaments();
    }

    @Override
    public List<TournamentResult> getTournamentResults(int tournamentId) {
        return resultRepo.getResultsByTournament(tournamentId);
    }
}
