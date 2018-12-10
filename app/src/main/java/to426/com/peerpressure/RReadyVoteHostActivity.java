package to426.com.peerpressure;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Random;

public class RReadyVoteHostActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rready_vote_host);

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

        final DatabaseReference lobbyHoldRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyHoldRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("Players").getChildrenCount() == dataSnapshot.child("Dares").getChildrenCount()) {

                        final ArrayList<String> dareUIDS = new ArrayList();

                        for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                            Dare currentDare = data.getValue(Dare.class);

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

                            randomDare2 = dareUIDS.get(new Random().nextInt(dareCount - 1));

                            lobbyHoldRef.child("Dares").child(randomDare1).child("dareUsed").setValue("selectOne");
                            lobbyHoldRef.child("Dares").child(randomDare2).child("dareUsed").setValue("selectTwo");

                            final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                            currentProperties.setDareRoundRandomized(true);

                            //Assigns the Dares Based on the Round & also sends the phones to correct activities
                            if (currentProperties.getGameProgression().equals("Round 1") && currentProperties.getRoundOneComplete()) {
                                currentProperties.setGameProgression("Round 2");
                            }

                            lobbyHoldRef.child("Properties").setValue(currentProperties);

                            lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected


                            if (UIDPLAYER.equals(randomDare1) || UIDPLAYER.equals(randomDare2)) {

                                Intent RReadVoteToRVoteWait = new Intent(RReadyVoteHostActivity.this, RVoteWaitActivity.class);
                                RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                                startActivity(RReadVoteToRVoteWait);

                                lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected

                                finish();

                            } else {

                                Intent RReadVoteToRVoteActive = new Intent(RReadyVoteHostActivity.this, RVoteActiveActivity.class);
                                RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                                startActivity(RReadVoteToRVoteActive);

                                lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected

                                finish();

                            }

                            } catch (Exception e) {

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

