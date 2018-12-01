package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class REndPartRoundActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rend_part_round);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);


                    //First Round Regular Operation
                     if (currentProperties.getDareRoundRandomized() && !currentProperties.getRoundOneComplete() && !currentProperties.getRoundTwoComplete()) {

                        final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Dare playerDare = dataSnapshot.child("Dares").child(UIDPLAYER).getValue(Dare.class);

                        if (playerDare.getDareUsed().equals("selectOne") || playerDare.getDareUsed().equals("selectTwo")) {

                            Intent RReadVoteToRVoteWait = new Intent(REndPartRoundActivity.this, RVoteWaitActivity.class);
                            RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteWait);

                            currentLobby.removeEventListener(this);

                            finish();

                        } else {

                            Intent RReadVoteToRVoteActive = new Intent(REndPartRoundActivity.this, RVoteActiveActivity.class);
                            RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteActive);

                            currentLobby.removeEventListener(this);

                            finish();
                        }
                    }


                    //First Round End Operation
                    if (currentProperties.getRoundOneComplete() && !currentProperties.getRoundTwoComplete() && !currentProperties.getDareRoundRandomized() && currentProperties.getGameProgression().equals("Round 1")) {

                            Intent RHostEndPartRoundAToRoundSplash = new Intent(REndPartRoundActivity.this, RoundSplashActivity.class);
                            RHostEndPartRoundAToRoundSplash.putExtra("lobbyCode", lobbyCode);
                            startActivity(RHostEndPartRoundAToRoundSplash);

                            currentLobby.removeEventListener(this);

                            finish();

                        }

                    //Second Round Regular Operation
                    if (currentProperties.getDareRoundRandomized() && currentProperties.getRoundOneComplete() && !currentProperties.getRoundTwoComplete()) {

                        final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Dare playerDare = dataSnapshot.child("Dares").child(UIDPLAYER).getValue(Dare.class);

                        if (playerDare.getDareUsed().equals("selectOne") || playerDare.getDareUsed().equals("selectTwo")) {

                            Intent RReadVoteToRVoteWait = new Intent(REndPartRoundActivity.this, RVoteWaitActivity.class);
                            RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteWait);

                            currentLobby.removeEventListener(this);

                            finish();

                        } else {

                            Intent RReadVoteToRVoteActive = new Intent(REndPartRoundActivity.this, RVoteActiveActivity.class);
                            RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteActive);

                            currentLobby.removeEventListener(this);

                            finish();
                        }
                    }



                    //FINAL ROUND ENTRANCE!
                    if (currentProperties.getRoundOneComplete() && currentProperties.getRoundTwoComplete()) {

                        Intent RHostEndPartRoundAToRoundSplash = new Intent(REndPartRoundActivity.this, FFinalDareLoadingActivity.class);
                        RHostEndPartRoundAToRoundSplash.putExtra("lobbyCode", lobbyCode);
                        startActivity(RHostEndPartRoundAToRoundSplash);

                        currentLobby.removeEventListener(this);

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


}
