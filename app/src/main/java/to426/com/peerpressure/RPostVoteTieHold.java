package to426.com.peerpressure;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RPostVoteTieHold extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rpost_vote_tie_hold);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
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

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_two) {

            Intent toRules = new Intent(this, RulesActivity.class);
            this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
