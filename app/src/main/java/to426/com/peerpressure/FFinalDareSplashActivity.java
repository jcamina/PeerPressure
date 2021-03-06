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

public class FFinalDareSplashActivity extends AppCompatActivity {

    public TextView finalDareParticipantOneTextView;
    public TextView finalDareParticipantTwoTextView;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ffinal_dare_splash);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        finalDareParticipantOneTextView = findViewById(R.id.finalDareParticipantOneTextView);
        finalDareParticipantTwoTextView = findViewById(R.id.finalDareParticipantTwoTextView);

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.exists()) {

                        final GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            //Populate Text Fields with Correct Names
                            if (data.getKey().equals(properties.getFinalDareLoserOne())) {
                                finalDareParticipantOneTextView.setText(currentPlayer.getNickname());

                            } else if (data.getKey().equals(properties.getFinalDareLoserTwo())) {
                                finalDareParticipantTwoTextView.setText(currentPlayer.getNickname());
                            }
                        }

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        new CountDownTimer(6000, 1000) {
                            public void onFinish() {

                                if (UIDCLIENT.equals(properties.getFinalDareLoserOne()) || UIDCLIENT.equals(properties.getFinalDareLoserTwo())) {

                                    Intent FFinalDareLoadingToFLosersHold = new Intent(FFinalDareSplashActivity.this, FDarePerformanceActivity.class);
                                    FFinalDareLoadingToFLosersHold.putExtra("lobbyCode", lobbyCode);
                                    startActivity(FFinalDareLoadingToFLosersHold);
                                    finish();

                                } else {

                                    Intent FFinalDareLoadingToFLobbyDareHold = new Intent(FFinalDareSplashActivity.this, FFinalVoteActivity.class);
                                    FFinalDareLoadingToFLobbyDareHold.putExtra("lobbyCode", lobbyCode);
                                    startActivity(FFinalDareLoadingToFLobbyDareHold);
                                    finish();
                                }
                            }
                            public void onTick(long millisUntilFinished) {

                            }

                        }.start();
                    }

                } catch (Exception e){

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
