package to426.com.peerpressure;

public class Player {

    private String UID;
    private String nickname;
    private int score;
    private boolean isHost;




    public Player () {

    }

    public Player (String inUID, String inNickname, int inScore, boolean inIsHost){
        UID = inUID;
        nickname = inNickname;
        score = inScore;
        isHost = inIsHost;

    }

    public String getUID() {
        return UID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int inScore) {
        score = inScore;
    }

    public boolean getIsHost() {
        return isHost;
    }

}


