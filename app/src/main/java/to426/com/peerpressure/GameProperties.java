package to426.com.peerpressure;

public class GameProperties {

    private String gameProgression = "";
    private String gameType = "";

    public GameProperties(){

    }

    public GameProperties(String inGameProgression, String inGameType){
        gameProgression = inGameProgression;
        gameType = inGameType;
    }

    public void setGameProgression(String inGameProgression) {
        gameProgression = inGameProgression;
    }

    public String getGameProgression() {
        return gameProgression;
    }

    public String getGameType() {
        return gameType;
    }
}
