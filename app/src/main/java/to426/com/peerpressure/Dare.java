package to426.com.peerpressure;

public class Dare {

    private String UIDAssociated;
    private String dareMessage;
    private int voteCount;
    private String dareUsed;
    public int voteCountExtra;


    public Dare () {

    }

    public Dare (String inUIDAssociated, String inDareMessage, int inVoteCount, String inDareUsed){
        UIDAssociated = inUIDAssociated;
        dareMessage = inDareMessage;
        voteCount = inVoteCount;
        dareUsed = inDareUsed;
    }

    public Dare (String inUIDAssociated, String inDareMessage, int inVoteCount, String inDareUsed, int inVoteCountExtra){
        UIDAssociated = inUIDAssociated;
        dareMessage = inDareMessage;
        voteCount = inVoteCount;
        dareUsed = inDareUsed;
        voteCountExtra = inVoteCountExtra;
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

    public void setDareUsed(String inDareUsed){
        dareUsed = inDareUsed;
    }

    public String getDareUsed(){
        return dareUsed;
    }

    public int getVoteCountExtra() {
        return voteCountExtra;
    }

    public void setVoteCountExtra(int inVoteCountExtra) {
        voteCountExtra = inVoteCountExtra;
    }
}
