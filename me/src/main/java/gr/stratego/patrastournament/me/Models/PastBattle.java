package gr.stratego.patrastournament.me.Models;

public class PastBattle {

    private String tournament;
    private String player1;
    private String player2;
    private String resultPlayer1;
    private String resultPlayer2;

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getResultPlayer1() {
        return resultPlayer1;
    }

    public void setResultPlayer1(String resultPlayer1) {
        this.resultPlayer1 = resultPlayer1;
    }

    public String getResultPlayer2() {
        return resultPlayer2;
    }

    public void setResultPlayer2(String resultPlayer2) {
        this.resultPlayer2 = resultPlayer2;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    @Override
    public String toString(){
        return tournament+": "+player1+" "+resultPlayer1+" VS "+player2+" "+ resultPlayer2;
    }
}
