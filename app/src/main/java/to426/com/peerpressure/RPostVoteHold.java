package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RPostVoteHold extends AppCompatActivity {

    String lobbyCode = "";
    String dareOneUID = "";
    String dareTwoUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpost_vote_hold);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

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

        //TEMPORARY!!! JUST WANT TO GET THROUGH THE LOGIC WORK!!

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addValueEventListener(new ValueEventListener() {
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

                    if ((voteTwo + voteOne + 2) == (dataSnapshot.child("Players").getChildrenCount()))
                    {
                        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        Toast.makeText(RPostVoteHold.this, "Everybody Has Voted!",
                                Toast.LENGTH_LONG).show();

                        if (voteOne > voteTwo) {
                            Toast.makeText(RPostVoteHold.this, "Dare 1 Won " + dareOneUID,
                                    Toast.LENGTH_LONG).show();

                        if (dareOneUID.equals(currentUID)){
                            Intent RPostVoteToRDareWinner = new Intent(RPostVoteHold.this,RDareWinnerActivity.class);
                            RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                            startActivity(RPostVoteToRDareWinner);
                            finish();

                            currentLobby.removeEventListener(this);
                            finish();

                        } else if (dareTwoUID.equals(currentUID)){
                            Intent RPostVoteToRDareLoser = new Intent(RPostVoteHold.this,RDareLoserActivity.class);
                            RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                            startActivity(RPostVoteToRDareLoser);

                            currentLobby.removeEventListener(this);
                            finish();

                        } else {
                            Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RPostVoteHold.this,RVoteActiveWinnerSplash.class);
                            RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                            startActivity(RPostVoteToRVoteActiveWinnerSplash);

                            currentLobby.removeEventListener(this);
                            finish();
                        }



                        } else if (voteTwo > voteOne){

                            Toast.makeText(RPostVoteHold.this, "Dare 2 Won " + dareTwoUID,
                                    Toast.LENGTH_LONG).show();

                            Toast.makeText(RPostVoteHold.this, "Dare 1 Won " + dareOneUID,
                                    Toast.LENGTH_LONG).show();

                            if (dareTwoUID.equals(currentUID)){
                                Intent RPostVoteToRDareWinner = new Intent(RPostVoteHold.this,RDareWinnerActivity.class);
                                RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareWinner);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else if (dareOneUID.equals(currentUID)){
                                Intent RPostVoteToRDareLoser = new Intent(RPostVoteHold.this,RDareLoserActivity.class);
                                RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareLoser);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else {
                                Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RPostVoteHold.this,RVoteActiveWinnerSplash.class);
                                RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRVoteActiveWinnerSplash);

                                currentLobby.removeEventListener(this);
                                finish();

                            }

                        } else if (voteOne == voteTwo){

                            Toast.makeText(RPostVoteHold.this, "Dare Tie!",
                                    Toast.LENGTH_LONG).show();

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



}
