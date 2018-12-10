package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_join_game_lobby);

        lobbyPlayersJoinTextView = findViewById(R.id.lobbyPlayersJoinTextView);

        //Get Lobby Code
        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if(bundle != null)
        {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener Allows You to "Backout" of A Lobby"
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                        .child("Games").child(lobbyCode);

                lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        lobbyPlayersJoinTextView.setText("");

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.child("Players").child(UIDCLIENT).exists()) {

                                lobbyRef.child("Players").child(UIDCLIENT).removeValue();

                                Intent backOutOfLobby = new Intent(JoinGameLobbyActivity.this, JoinGameActivity.class);
                                startActivity(backOutOfLobby);
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
        });

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

                lobbyPlayersJoinTextView.setText("");

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

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
