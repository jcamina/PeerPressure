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

public class RVoteActiveWinnerSplash extends AppCompatActivity {

    String lobbyCode = "";
    public TextView splashVoteDareTextView;
    public TextView splashVoteNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvote_active_winner_splash);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        splashVoteNameTextView = findViewById(R.id.splashVoteNameTextView);
        splashVoteDareTextView = findViewById(R.id.splashVoteDareTextView);


        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        //Sets the dare to text field
        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    int voteOne = 0;
                    String dareOneUID = "";

                    int voteTwo = 0;
                    String dareTwoUID = "";

                    for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                        Dare currentDare = data.getValue(Dare.class);

                        if (currentDare.getDareUsed().equals("selectOne")) {
                            voteOne = currentDare.getVoteCount();
                            dareOneUID = data.getKey();

                        } else if (currentDare.getDareUsed().equals("selectTwo")) {
                            voteTwo = currentDare.getVoteCount();
                            dareTwoUID = data.getKey();

                        }
                    }

                    if (voteOne > voteTwo) {

                        Dare winningDare = dataSnapshot.child("Dares").child(dareOneUID).getValue(Dare.class);

                        splashVoteDareTextView.setText(winningDare.getDareMessage());

                        Player losingPlayer = dataSnapshot.child("Players").child(dareTwoUID).getValue(Player.class);
                        splashVoteNameTextView.setText(losingPlayer.getNickname());


                    } else if (voteTwo > voteOne) {

                        Dare winningDare = dataSnapshot.child("Dares").child(dareTwoUID).getValue(Dare.class);

                        splashVoteDareTextView.setText(winningDare.getDareMessage());

                        Player losingPlayer = dataSnapshot.child("Players").child(dareOneUID).getValue(Player.class);
                        splashVoteNameTextView.setText(losingPlayer.getNickname());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        new CountDownTimer(11000, 1000) {
            public void onFinish() {

                Intent RVoteActiveWinnerSplashToRVotePerformance = new Intent(RVoteActiveWinnerSplash.this,RVotePerformanceActivity.class);
                RVoteActiveWinnerSplashToRVotePerformance.putExtra("lobbyCode", lobbyCode);
                startActivity(RVoteActiveWinnerSplashToRVotePerformance);
                finish();

            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();

    }
}
