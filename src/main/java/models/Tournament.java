package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tournament {

    private int tournamentId;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private List<Player> participants;
    private List<Result> results;

    public Tournament(int tournamentId, String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "planned";
        this.participants = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void createTournament() {
        System.out.println("Tournament " + name + " created.");
    }

    public boolean registerPlayer(Player player) {
        if (!participants.contains(player)) {
            participants.add(player);
            System.out.println("Player " + player.getName() + " registered for the tournament.");
            return true;
        } else {
            System.out.println("Player " + player.getName() + " is already registered.");
            return false;
        }
    }

    public void startTournament() {
        if (participants.size() < 2) {
            System.out.println("Not enough participants to start the tournament.");
            return;
        }
        status = "in progress";
        System.out.println("Tournament " + name + " started.");
    }

    public void conductMatch(Player player1, Player player2) {
        if (!participants.contains(player1) || !participants.contains(player2)) {
            System.out.println("Both players must be registered to conduct the match.");
            return;
        }

        System.out.println("Match between " + player1.getName() + " and " + player2.getName() + " started.");

        Player winner = simulateMatch(player1, player2);

        Result result = new Result(player1, player2, winner);
        results.add(result);
        System.out.println("Match concluded. " + winner.getName() + " wins!");
    }

    private Player simulateMatch(Player player1, Player player2) {
        Random random = new Random();
        return random.nextBoolean() ? player1 : player2;
    }

    public void determineWinner() {
        if (!results.isEmpty()) {
            Result lastResult = results.get(results.size() - 1);
            System.out.println("Tournament winner: " + lastResult.getWinner().getName());
            status = "completed";
        } else {
            System.out.println("Tournament results are not yet determined.");
        }
    }

    public void updateTournamentStatus(String status) {
        this.status = status;
        System.out.println("Tournament status updated to: " + status);
    }
}
