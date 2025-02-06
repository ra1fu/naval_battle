package models;

public class Result {

    private Player player1;
    private Player player2;
    private Player winner;

    public Result(Player player1, Player player2, Player winner) {
        setPlayer1(player1);
        setPlayer2(player2);
        setWinner(winner);
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
