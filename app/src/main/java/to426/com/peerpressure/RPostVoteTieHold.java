package to426.com.peerpressure;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RPostVoteTieHold extends AppCompatActivity {

    String lobbyCode = "";
    String dareOneUID = "";
    String dareTwoUID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpost_vote_tie_hold);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
            dareOneUID = (String) bundle.get("dareOneUID");
            dareTwoUID = (String) bundle.get("dareTwoUID");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference hostLobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        hostLobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    if (currentPlayer.getIsHost()) {

                        String dareOneUID = "";
                        String dareTwoUID = "";

                        //Determines What the UID of the Selected Phones Are, Saves For Incrementing & Assignment
                        for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                            Dare currentDare = data.getValue(Dare.class);

                            if (currentDare.getDareUsed().equals("selectOne")) {
                                dareOneUID = data.getKey();

                            } else if (currentDare.getDareUsed().equals("selectTwo")) {
                                dareTwoUID = data.getKey();

                            }
                        }

                        //Host Phone Randomizes The Players & Adds a Vote
                        int randomVal = new Random().nextInt(2);

                        if (randomVal == 1) {

                            Dare winnerDare = dataSnapshot.child("Dares").child(dareOneUID).getValue(Dare.class);

                            winnerDare.setVoteCount(winnerDare.getVoteCount() + 1);

                            hostLobbyRef.child("Dares").child(dareOneUID).setValue(winnerDare);

                        } else if (randomVal == 0) {

                            Dare winnerDare = dataSnapshot.child("Dares").child(dareTwoUID).getValue(Dare.class);

                            winnerDare.setVoteCount(winnerDare.getVoteCount() + 1);

                            hostLobbyRef.child("Dares").child(dareTwoUID).setValue(winnerDare);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });



        new CountDownTimer(4000, 1000) {
            public void onFinish() {

                final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                        .child("Games").child(lobbyCode);

                currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            int voteOne = 0;
                            String dareOneUID = "";

                            int voteTwo = 0;
                            String dareTwoUID = "";

                            for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                                Dare currentDare = data.getValue(Dare.class);

                                if (currentDare.getDareUsed().equals("selectOne")) {
                                    voteOne = currentDare.getVoteCount();
                                    dareOneUID = data.getKey();

                                } else if (currentDare.getDareUsed().equals("selectTwo")) {
                                    voteTwo = currentDare.getVoteCount();
                                    dareTwoUID = data.getKey();

                                }
                            }

                            String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            if (voteOne > voteTwo) {

                                if (dareOneUID.equals(currentUID)){
                                    Intent RPostVoteToRDareWinner = new Intent(RPostVoteTieHold.this,RDareWinnerActivity.class);
                                    RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRDareWinner);
                                    finish();

                                    currentLobby.removeEventListener(this);
                                    finish();

                                } else if (dareTwoUID.equals(currentUID)){
                                    Intent RPostVoteToRDareLoser = new Intent(RPostVoteTieHold.this,RDareLoserActivity.class);
                                    RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRDareLoser);

                                    currentLobby.removeEventListener(this);
                                    finish();

                                } else {
                                    Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RPostVoteTieHold.this,RVoteActiveWinnerSplash.class);
                                    RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRVoteActiveWinnerSplash);

                                    currentLobby.removeEventListener(this);
                                    finish();
                                }


                            } else if (voteTwo > voteOne){

                                if (dareTwoUID.equals(currentUID)){
                                    Intent RPostVoteToRDareWinner = new Intent(RPostVoteTieHold.this,RDareWinnerActivity.class);
                                    RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRDareWinner);

                                    currentLobby.removeEventListener(this);
                                    finish();

                                } else if (dareOneUID.equals(currentUID)){
                                    Intent RPostVoteToRDareLoser = new Intent(RPostVoteTieHold.this,RDareLoserActivity.class);
                                    RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRDareLoser);

                                    currentLobby.removeEventListener(this);
                                    finish();

                                } else {
                                    Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RPostVoteTieHold.this,RVoteActiveWinnerSplash.class);
                                    RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                                    startActivity(RPostVoteToRVoteActiveWinnerSplash);

                                    currentLobby.removeEventListener(this);
                                    finish();

                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });



            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();



    }
}
