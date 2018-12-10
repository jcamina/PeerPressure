package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateNewGameLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startGameButton;
    public TextView lobbyCodeTextView;
    public TextView lobbyPlayersTextView;
    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_create_new_game_lobby);

        startGameButton = (Button) findViewById(R.id.startGameButton);
        lobbyCodeTextView = (TextView) findViewById(R.id.lobbyCode);
        lobbyPlayersTextView = (TextView) findViewById(R.id.lobbyPlayersTextView);
        startGameButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if(bundle != null)
        {
            lobbyCode = (String) bundle.get("lobbyCode");
            lobbyCodeTextView.append(" " + lobbyCode);
        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
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

                            //Start the Game With "Round One"
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

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    //Info Button OnClick
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
