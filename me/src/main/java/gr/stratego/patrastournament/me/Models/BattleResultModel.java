package gr.stratego.patrastournament.me.Models;

public class BattleResultModel {
    private String leftName;
    private String rightName;
    private String score;

    public BattleResultModel() {
    }

    public BattleResultModel(String leftName, String rightName, String score) {
        this.leftName = leftName;
        this.rightName = rightName;
        this.score = score;
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
