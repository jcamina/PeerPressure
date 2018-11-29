package to426.com.peerpressure;

public class GameProperties {

    private String gameProgression = "";
    private String gameType = "";
    private boolean dareRoundRandomized;
    private String currentWinner = "";

    public GameProperties(){

    }

    public GameProperties(String inGameProgression, String inGameType, boolean inDareRoundRandomized, String inCurrentWinner){
        gameProgression = inGameProgression;
        gameType = inGameType;
        dareRoundRandomized = inDareRoundRandomized;
        currentWinner = inCurrentWinner;
    }

    public void setGameProgression(String inGameProgression) {
        gameProgression = inGameProgression;
    }

    public boolean getDareRoundRandomized(){
        return dareRoundRandomized;
    }

    public void setDareRoundRandomized(boolean inDareRoundRandomized){
        dareRoundRandomized = inDareRoundRandomized;
    }

    public String getGameProgression() {
        return gameProgression;
    }

    public String getGameType() {
        return gameType;
    }

    public void setCurrentWinner(String inCurrentWinner) {
        currentWinner = inCurrentWinner;
    }

    public String getCurrentWinner(){
        return currentWinner;
    }
}
