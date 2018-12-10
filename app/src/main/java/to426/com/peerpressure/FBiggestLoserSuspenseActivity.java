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

public class FBiggestLoserSuspenseActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_fbiggest_loser_suspense);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Adjusts Scores & Launches Final Activities
        setFinalDareScore();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public void setFinalDareScore(){

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    final GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    Dare finalDare = dataSnapshot.child("Dares").child("Final Dare").getValue(Dare.class);

                    //Launches The Tie LeaderBoard Screen if amount of Dares are tied
                    if ((finalDare.getVoteCount() == finalDare.getVoteCountExtra())) {

                        new CountDownTimer(4000, 1000) {
                            public void onFinish() {

                                Intent FBiggestLoserSuspenseToFLeaderboardSplashTie = new Intent(
                                        FBiggestLoserSuspenseActivity.this, FLeaderboardSplashTieActivity.class);
                                FBiggestLoserSuspenseToFLeaderboardSplashTie.putExtra("lobbyCode", lobbyCode);
                                startActivity(FBiggestLoserSuspenseToFLeaderboardSplashTie);

                                finish();
                            }

                            public void onTick(long millisUntilFinished) {

                            }
                        }.start();

                    } else {

                        //Only Allow Host Phone To Edit the Score!
                        if (currentPlayer.getIsHost()){

                            for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                                Player loopingPlayer = data.getValue(Player.class);

                                // Change Score of the Player if UID matches loser 1
                                if (data.getKey().equals(properties.getFinalDareLoserOne())) {

                                    if (finalDare.getVoteCount() > finalDare.getVoteCountExtra()) {

                                        loopingPlayer.setScore(currentPlayer.getScore() + 2000);
                                        lobbyRef.child("Players").child(data.getKey()).setValue(loopingPlayer);

                                    } else if (finalDare.getVoteCount() < finalDare.getVoteCountExtra()) {

                                        loopingPlayer.setScore(currentPlayer.getScore() - 2000);
                                        lobbyRef.child("Players").child(data.getKey()).setValue(loopingPlayer);
                                    }

                                // Change Score of the Player if UID matches loser 2
                                } else if (data.getKey().equals(properties.getFinalDareLoserTwo())) {

                                    if (finalDare.getVoteCount() < finalDare.getVoteCountExtra()) {

                                        loopingPlayer.setScore(currentPlayer.getScore() + 2000);
                                        lobbyRef.child("Players").child(data.getKey()).setValue(loopingPlayer);

                                    } else if (finalDare.getVoteCount() > finalDare.getVoteCountExtra()) {

                                        loopingPlayer.setScore(currentPlayer.getScore() - 2000);
                                        lobbyRef.child("Players").child(data.getKey()).setValue(loopingPlayer);
                                    }
                                }
                            }
                        }

                        new CountDownTimer(4000, 1000) {
                            public void onFinish() {

                                Intent FBiggestLoserSuspenseToFLeaderboardSplash = new Intent(
                                        FBiggestLoserSuspenseActivity.this, FLeaderboardSplash.class);
                                FBiggestLoserSuspenseToFLeaderboardSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(FBiggestLoserSuspenseToFLeaderboardSplash);

                                finish();
                            }

                            public void onTick(long millisUntilFinished) {

                            }

                        }.start();
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



