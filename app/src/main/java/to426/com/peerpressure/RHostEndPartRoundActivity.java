package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class RHostEndPartRoundActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rhost_end_part_round);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                        int dareUnusedCounter = 0;

                        for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                            Dare checkDare = data.getValue(Dare.class);

                            String dareStatus =  checkDare.getDareUsed();

                            //Setup Dare Random Selection
                           if (dareStatus.equals("selectOne") || dareStatus.equals("selectTwo")) {

                               checkDare.setDareUsed("Used");

                               currentLobby.child("Dares").child(data.getKey()).setValue(checkDare);

                           } else if (dareStatus.equals("Unused")) {

                               dareUnusedCounter+= 1;
                           }
                        }

                        if (dareUnusedCounter > 1){

                            properties.setNumVoted(0);

                            currentLobby.child("Properties").setValue(properties);

                            //In round randomize
                            randomizeRemainingRoundDares();


                        } else if (dareUnusedCounter <= 1) {

                            if (properties.getGameProgression().equals("Round 1")) {

                                properties.setNumVoted(0);
                                properties.setRoundOneComplete(true);
                                properties.setDareRoundRandomized(false);

                                currentLobby.child("Properties").setValue(properties);
                                currentLobby.child("Dares").setValue("");


                                Intent RHostEndPartRoundAToRoundSplash = new Intent(RHostEndPartRoundActivity.this,
                                        RoundSplashActivity.class);
                                RHostEndPartRoundAToRoundSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(RHostEndPartRoundAToRoundSplash);
                                finish();

                            } else if (properties.getGameProgression().equals("Round 2")) {

                                properties.setGameProgression("Final Round");
                                properties.setNumVoted(0);
                                properties.setRoundTwoComplete(true);

                                currentLobby.child("Properties").setValue(properties);
                                currentLobby.child("Dares").setValue("");

                                Intent RHostEndPartRoundAToFFinalDareLoadingHost = new Intent(RHostEndPartRoundActivity.this,
                                        FFinalDareLoadingHostActivity.class);
                                RHostEndPartRoundAToFFinalDareLoadingHost.putExtra("lobbyCode", lobbyCode);
                                startActivity(RHostEndPartRoundAToFFinalDareLoadingHost);
                                finish();

                            }
                        }
                    currentLobby.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    private void randomizeRemainingRoundDares() {

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final ArrayList<String> dareUIDS = new ArrayList();

                    for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                        final Dare currentDare = data.getValue(Dare.class);

                        if (currentDare.getDareUsed().equals("Unused")) {

                            dareUIDS.add(data.getKey());
                        }
                    }

                    try {

                        String randomDare1 = "";
                        String randomDare2 = "";

                        int dareCount = dareUIDS.size();

                        int randomVal1 = new Random().nextInt(dareCount);

                        randomDare1 = dareUIDS.get(randomVal1);

                        dareUIDS.remove(randomVal1);

                        if (dareUIDS.size() == 1){
                            randomDare2 = dareUIDS.get(0);

                        } else {
                            randomDare2 = dareUIDS.get(new Random().nextInt(dareCount - 1));

                        }

                        currentLobby.child("Dares").child(randomDare1).child("dareUsed").setValue("selectOne");
                        currentLobby.child("Dares").child(randomDare2).child("dareUsed").setValue("selectTwo");

                        final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                        currentProperties.setDareRoundRandomized(true);

                        currentLobby.child("Properties").setValue(currentProperties);

                        if (UIDPLAYER.equals(randomDare1) || UIDPLAYER.equals(randomDare2)) {

                            Intent RReadVoteToRVoteWait = new Intent(RHostEndPartRoundActivity.this, RVoteWaitActivity.class);
                            RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteWait);

                            finish();

                        } else {

                            Intent RReadVoteToRVoteActive = new Intent(RHostEndPartRoundActivity.this,
                                                    RVoteActiveActivity.class);
                            RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteActive);

                            finish();

                        }

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
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
