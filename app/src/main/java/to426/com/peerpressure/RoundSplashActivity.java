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


public class RoundSplashActivity extends AppCompatActivity {

    public TextView roundTextView;
    public TextView countdownTextView;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_round_splash);

        roundTextView = (TextView) findViewById(R.id.roundTextView);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        countdownTextView = (TextView) findViewById(R.id.countdownTextView);

        //Countdown to Round Start...
        new CountDownTimer(6000, 1000) {
            public void onFinish() {
                Intent roundSplashToEnterDare = new Intent(RoundSplashActivity.this,REnterDareActivity.class);

                roundSplashToEnterDare.putExtra("lobbyCode", lobbyCode);
                startActivity(roundSplashToEnterDare);
                finish();
            }

            public void onTick(long millisUntilFinished) {
                countdownTextView.setText("00:0" + millisUntilFinished / 1000);
            }

        }.start();

        //Check What Round is Completed For Titling Purposes
        final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Properties");

        lobbyCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.getValue(GameProperties.class);

                    String roundTitle = "";

                    if (!properties.getRoundOneComplete()) {
                        roundTitle = "1";
                    } else if (properties.getRoundOneComplete()){
                        roundTitle = "2";

                    }

                    roundTextView.setText(roundTitle);

                    lobbyCheckRef.removeEventListener(this); // stops from assigning every single one as selected


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
