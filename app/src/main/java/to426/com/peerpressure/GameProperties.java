package to426.com.peerpressure;

public class GameProperties {

    private String gameProgression = "";
    private String gameType = "";
    private boolean dareRoundRandomized;
    private boolean roundOneComplete;
    private boolean roundTwoComplete;

    private String finalDareLoserOne = "";
    private String finalDareLoserTwo = "";

    private int numVoted = 0;

    public GameProperties(){

    }

    public GameProperties(String inGameProgression, String inGameType, boolean inDareRoundRandomized, int inNumVoted, boolean inRoundOneComplete, boolean inRoundTwoComplete){
        gameProgression = inGameProgression;
        gameType = inGameType;
        dareRoundRandomized = inDareRoundRandomized;
        numVoted = inNumVoted;
        roundOneComplete = inRoundOneComplete;
        roundTwoComplete = inRoundTwoComplete;
    }

    //Final Dare Properties
    public GameProperties(String inGameProgression, String inGameType, int inNumVoted, boolean inRoundOneComplete, boolean inRoundTwoComplete, String inFinalDareLoserOne, String inFinalDareLoserTwo){
        gameProgression = inGameProgression;
        gameType = inGameType;
        numVoted = inNumVoted;
        roundOneComplete = inRoundOneComplete;
        roundTwoComplete = inRoundTwoComplete;
        finalDareLoserOne = inFinalDareLoserOne;
        finalDareLoserTwo = inFinalDareLoserTwo;

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

    public void setNumVoted(int inNumVoted) {
        numVoted = inNumVoted;
    }

    public int getNumVoted(){
        return numVoted;
    }

    public boolean getRoundOneComplete() {
        return roundOneComplete;
    }

    public void setRoundOneComplete(boolean inRoundOneComplete) {
        roundOneComplete = inRoundOneComplete;
    }

    public boolean getRoundTwoComplete() {
        return roundTwoComplete;
    }

    public void setRoundTwoComplete(boolean inRoundTwoComplete) {
        roundTwoComplete = inRoundTwoComplete;
    }

    public String getFinalDareLoserOne() {
        return finalDareLoserOne;
    }

    public String getFinalDareLoserTwo() {
        return finalDareLoserTwo;
    }


}
