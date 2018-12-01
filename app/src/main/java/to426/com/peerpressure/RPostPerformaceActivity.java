package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RPostPerformaceActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpost_performace);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (properties.getNumVoted() == (dataSnapshot.child("Players").getChildrenCount() - 1) ) {

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                        if(currentPlayer.getIsHost()){

                            Intent RDareLoserToRHostEndPartRound = new Intent(RPostPerformaceActivity.this,RHostEndPartRoundActivity.class);
                            RDareLoserToRHostEndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToRHostEndPartRound);

                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        } else {
                            Intent RDareLoserToREndPartRound = new Intent(RPostPerformaceActivity.this,REndPartRoundActivity.class);
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
