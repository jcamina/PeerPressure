package to426.com.peerpressure;

public class Dare {

    private String UIDAssociated;
    private String dareMessage;
    private int voteCount;


    public Dare () {

    }

    public Dare (String inUIDAssociated, String inDareMessage, int inVoteCount){
        UIDAssociated = inUIDAssociated;
        dareMessage = inDareMessage;
        voteCount = inVoteCount;
    }

    public String getUIDAssociated() {
        return UIDAssociated;
    }

    public String getDareMessage() {
        return dareMessage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int inVoteCount) {
        voteCount = inVoteCount;
    }
}
