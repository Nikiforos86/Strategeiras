package gr.stratego.patrastournament.me.Models;

public class PastBattle {

    private String player1;
    private String player2;
    private String resultPlayer1;
    private String resultplayer2;

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

    public String getResultplayer2() {
        return resultplayer2;
    }

    public void setResultplayer2(String resultplayer2) {
        this.resultplayer2 = resultplayer2;
    }

    @Override
    public String toString(){
        return player1+" "+resultPlayer1+" VS "+player2+" "+resultplayer2;
    }
}
