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

    public String lobbyCode = "";



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

                            if (data.getKey().equals(properties.getFinalDareLoserOne())) {
                                finalDareParticipantOneTextView.setText(currentPlayer.getNickname());


                            } else if (data.getKey().equals(properties.getFinalDareLoserTwo())) {
                                finalDareParticipantTwoTextView.setText(currentPlayer.getNickname());

                            }
                        }

                        /*

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        new CountDownTimer(6000, 1000) {
                            public void onFinish() {

                                if (UIDCLIENT.equals(properties.getFinalDareLoserOne()) || UIDCLIENT.equals(properties.getFinalDareLoserTwo())) {

                                    Intent FFinalDareLoadingToFLosersHold = new Intent(FFinalDareSplashActivity.this, FDarePerformanceActivity.class)

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

                        */


                    }
                }catch (Exception e){

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
