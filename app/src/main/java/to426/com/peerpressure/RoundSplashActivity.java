package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RoundSplashActivity extends AppCompatActivity {

    public TextView roundTextView;
    public TextView countdownTextView;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_splash);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        roundTextView = (TextView) findViewById(R.id.roundTextView);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        countdownTextView = (TextView) findViewById(R.id.countdownTextView);

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

        final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Properties");

        lobbyCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.getValue(GameProperties.class);

                    String roundTitle = "";

                    if (!properties.getRoundOneComplete()) {
                        roundTitle = "Round 1";
                    } else if (properties.getRoundOneComplete()){
                        roundTitle = "Round 2";

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

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }


}
