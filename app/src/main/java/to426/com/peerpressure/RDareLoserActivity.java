package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RDareLoserActivity extends AppCompatActivity {

    String lobbyCode = "";

    public TextView loserNamePlaceholderTextView;
    public TextView loserDarePlaceholderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdare_loser);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        loserNamePlaceholderTextView = findViewById(R.id.loserNamePlaceholderTextView);
        loserDarePlaceholderTextView = findViewById(R.id.loserDarePlaceholderTextView);

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    loserNamePlaceholderTextView.setText(currentPlayer.getNickname());

                    currentPlayer.setScore(currentPlayer.getScore() - 500);

                    currentLobby.child("Players").child(UIDCLIENT).setValue(currentPlayer);

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    currentLobby.removeEventListener(this);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


        //Sets the dare to text field
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

                    if (voteOne > voteTwo) {

                        Dare winningDare = dataSnapshot.child("Dares").child(dareOneUID).getValue(Dare.class);

                        loserDarePlaceholderTextView.setText(winningDare.getDareMessage());

                    } else if (voteTwo > voteOne) {

                        Dare winningDare = dataSnapshot.child("Dares").child(dareTwoUID).getValue(Dare.class);

                        loserDarePlaceholderTextView.setText(winningDare.getDareMessage());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);


                    if (properties.getNumVoted() == (dataSnapshot.child("Players").getChildrenCount() - 1) ) {

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);


                        if(currentPlayer.getIsHost()){

                            Intent RDareLoserToRHostEndPartRound = new Intent(RDareLoserActivity.this,RHostEndPartRoundActivity.class);
                            RDareLoserToRHostEndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToRHostEndPartRound);

                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        } else {
                            Intent RDareLoserToREndPartRound = new Intent(RDareLoserActivity.this,REndPartRoundActivity.class);
                            RDareLoserToREndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToREndPartRound);
                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        }

                        currentLobby.removeEventListener(this);
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
