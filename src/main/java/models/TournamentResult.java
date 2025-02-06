package models;

import java.time.LocalDateTime;

public class TournamentResult {
    private int resultId;
    private int tournamentId;
    private int player1Id;
    private int player2Id;
    private int winnerId;
    private LocalDateTime matchTime;

    public TournamentResult(int resultId, int tournamentId, int player1Id, int player2Id, int winnerId, LocalDateTime matchTime) {
        this.resultId = resultId;
        this.tournamentId = tournamentId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.winnerId = winnerId;
        this.matchTime = matchTime;
    }

    public int getResultId() {
        return resultId;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    @Override
    public String toString() {
        return "TournamentResult{" +
                "resultId=" + resultId +
                ", tournamentId=" + tournamentId +
                ", player1Id=" + player1Id +
                ", player2Id=" + player2Id +
                ", winnerId=" + winnerId +
                ", matchTime=" + matchTime +
                '}';
    }
}
