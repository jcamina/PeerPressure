package to426.com.peerpressure;


import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RDareWinnerActivity extends AppCompatActivity {

    String lobbyCode = "";

    public TextView namePlaceholderTextView;
    public TextView darePlaceholderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdare_winner);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        namePlaceholderTextView = findViewById(R.id.namePlaceholderTextView);
        darePlaceholderTextView = findViewById(R.id.darePlaceholderTextView);

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Scales the Scoring Based on the Numbers of Players in the Lobby and Round
                    double scaleFactorPlayers = 0.0;
                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (dataSnapshot.child("Players").getChildrenCount() <= 4) {
                        scaleFactorPlayers = 1.0;
                    } else if (dataSnapshot.child("Players").getChildrenCount() > 4 && dataSnapshot.child("Players").getChildrenCount() <= 6) {
                        scaleFactorPlayers = 1.7;
                    } else if (dataSnapshot.child("Players").getChildrenCount() > 6 && dataSnapshot.child("Players").getChildrenCount() <= 8){
                        scaleFactorPlayers = 2.5;
                    }

                    double scaleFactorRounds = 1.0;
                    if (currentProperties.getRoundOneComplete()){
                        scaleFactorRounds = 2.0;
                    }

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    namePlaceholderTextView.setText(currentPlayer.getNickname());

                    currentPlayer.setScore(currentPlayer.getScore() + (int) (1000 * scaleFactorPlayers * scaleFactorRounds));

                    currentLobby.child("Players").child(UIDCLIENT).setValue(currentPlayer);

                    Dare winningDare = dataSnapshot.child("Dares").child(UIDCLIENT).getValue(Dare.class);

                    darePlaceholderTextView.setText(winningDare.getDareMessage());

                    currentLobby.removeEventListener(this);


                    // Need This For Later Game Progression ~ Couldn't find better spot
                    currentProperties.setDareRoundRandomized(false);
                    currentLobby.child("Properties").setValue(currentProperties);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        new CountDownTimer(11000, 1000) {
            public void onFinish() {

                Intent RDareWinnerSplashToRVotePerformance = new Intent(RDareWinnerActivity.this,RVotePerformanceActivity.class);
                RDareWinnerSplashToRVotePerformance.putExtra("lobbyCode", lobbyCode);
                startActivity(RDareWinnerSplashToRVotePerformance);
                finish();

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
