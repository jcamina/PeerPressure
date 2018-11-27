package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGameLobbyActivity extends AppCompatActivity {

    public TextView lobbyPlayersJoinTextView;
    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game_lobby);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        lobbyPlayersJoinTextView = (TextView) findViewById(R.id.lobbyPlayersJoinTextView);

        c

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lobbyPlayersJoinTextView.setText("");

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {
                        //If Borrow Byy Value Matches, Add to Array List "Books"
                        Player currentPlayer = data.getValue(Player.class);
                        lobbyPlayersJoinTextView.append(currentPlayer.getNickname() + "\n\n");

                    }

                    GameProperties properties = dataSnapshot.child("Properties")
                            .getValue(GameProperties.class);

                    if (properties.getGameProgression().equals("Round 1")){

                        lobbyCheckRef.removeEventListener(this);

                        Intent joinGameLobbyToRoundSplash = new Intent(JoinGameLobbyActivity.this,
                                RoundSplashActivity.class);

                        joinGameLobbyToRoundSplash.putExtra("lobbyCode", lobbyCode);


                        startActivity(joinGameLobbyToRoundSplash);

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
