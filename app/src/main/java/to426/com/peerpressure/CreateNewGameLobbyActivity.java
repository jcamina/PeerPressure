package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateNewGameLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startGameButton;
    public TextView lobbyCodeTextView;
    public TextView lobbyPlayersTextView;
    public String lobbyCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_game_lobby);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        startGameButton = (Button) findViewById(R.id.startGameButton);
        lobbyCodeTextView = (TextView) findViewById(R.id.lobbyCode);
        lobbyPlayersTextView = (TextView) findViewById(R.id.lobbyPlayersTextView);
        startGameButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if(bundle != null)
        {
            lobbyCode = (String) bundle.get("lobbyCode");
            lobbyCodeTextView.setText(lobbyCode);
        }

    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lobbyPlayersTextView.setText("");

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {
                        //If Borrow Byy Value Matches, Add to Array List "Books"
                        Player currentPlayer = data.getValue(Player.class);
                        lobbyPlayersTextView.append(currentPlayer.getNickname() + "\n\n");

                    }

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


    }


    @Override
    public void onClick(View v) {


        if (v == startGameButton) {

            startGameButton.setEnabled(false);

            final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                    .child("Games").child(lobbyCode).child("Properties");

            lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        GameProperties properties = dataSnapshot.getValue(GameProperties.class);

                            if (properties.getGameProgression().equals("Lobby")){

                                properties.setGameProgression("Round 1");

                                lobbyRef.setValue(properties);

                                Intent newGameLobbyToRoundSplash = new Intent(CreateNewGameLobbyActivity.this,
                                        RoundSplashActivity.class);

                                newGameLobbyToRoundSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(newGameLobbyToRoundSplash);
                                finish();

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
}
