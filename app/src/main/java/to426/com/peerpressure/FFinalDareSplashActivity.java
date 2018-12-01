package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FFinalDareSplashActivity extends AppCompatActivity {

    public TextView finalDareParticipantOneTextView;
    public TextView finalDareParticipantTwoTextView;
    public TextView debugView;

    public String lobbyCode = "";

    Intent FFinalDareSplashToFFinalVoteActivity = new Intent(FFinalDareSplashActivity.this, FFinalVoteActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffinal_dare_splash);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        finalDareParticipantOneTextView = findViewById(R.id.finalDareParticipantOneTextView);
        finalDareParticipantTwoTextView = findViewById(R.id.finalDareParticipantTwoTextView);
        debugView = findViewById(R.id.debugView);


        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.exists()) {

                        final ArrayList<Integer> playerScores = new ArrayList();
                        String loser1UID = "";
                        String loser2UID = "";


                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            playerScores.add(currentPlayer.getScore());
                        }

                        Collections.sort(playerScores, Collections.reverseOrder());

                        finalDareParticipantOneTextView.setText(Integer.toString(playerScores.get(0)));

                        /*
                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            if (currentPlayer.getScore() == playerScores.get(0)) {
                                finalDareParticipantOneTextView.setText(currentPlayer.getNickname());
                                //  FFinalDareSplashToFFinalVoteActivity.putExtra("Loser1", data.getKey());
                                loser1UID = data.getKey();

                            } else if (currentPlayer.getScore() == playerScores.get(1)) {
                                finalDareParticipantOneTextView.setText(currentPlayer.getNickname());
                                // FFinalDareSplashToFFinalVoteActivity.putExtra("Loser2", data.getKey());
                                loser2UID = data.getKey();

                            }
                        }


                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final String loser1inTimer = loser1UID;
                        final String loser2inTimer = loser2UID;

                        new CountDownTimer(4000, 1000) {
                            public void onFinish() {

                                if (UIDCLIENT.equals(loser1inTimer) || UIDCLIENT.equals(loser2inTimer)) {

                                    Intent FFinalDareLoadingToFLobbDareHold = new Intent(FFinalDareSplashActivity.this, FLosersHoldActivity.class);
                                    FFinalDareLoadingToFLobbDareHold.putExtra("lobbyCode", lobbyCode);
                                    startActivity(FFinalDareLoadingToFLobbDareHold);
                                    finish();

                                } else {
                                    Intent FFinalDareLoadingToFLobbDareHold = new Intent(FFinalDareSplashActivity.this, FFinalVoteActivity.class);
                                    FFinalDareLoadingToFLobbDareHold.putExtra("lobbyCode", lobbyCode);
                                    startActivity(FFinalDareLoadingToFLobbDareHold);
                                    finish();
                                }

                            }

                            public void onTick(long millisUntilFinished) {
                            }

                        }.start();

                        */

                    }
                }catch (Exception e){
                        debugView.setText(e.toString());
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
