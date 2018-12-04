package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FBiggestLoserSuspenseActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbiggest_loser_suspense);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        new CountDownTimer(4000, 1000) {
            public void onFinish() {

                Intent FBiggestLoserSuspenseToFLeaderboardSplash = new Intent(
                        FBiggestLoserSuspenseActivity.this,FLeaderboardSplash.class);
                FBiggestLoserSuspenseToFLeaderboardSplash.putExtra("lobbyCode", lobbyCode);
                startActivity(FBiggestLoserSuspenseToFLeaderboardSplash);

            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();

        final DatabaseReference hostCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        hostCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    if (currentPlayer.getIsHost()) {

                        setFinalDareScore();

                    }

                    hostCheckRef.removeEventListener(this);

                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    public void setFinalDareScore(){

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.exists()) {

                        final GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                        Dare finalDare = dataSnapshot.child("Dare").child("Final Dare").getValue(Dare.class);

                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            if (data.getKey().equals(properties.getFinalDareLoserOne())) {

                                if (finalDare.getVoteCount() > finalDare.getVoteCountExtra()) {
                                    currentPlayer.setScore(currentPlayer.getScore() + 1000);
                                    lobbyRef.child("Players").child(data.getKey()).setValue(currentPlayer);

                                } else if (finalDare.getVoteCount() < finalDare.getVoteCountExtra()) {

                                    currentPlayer.setScore(currentPlayer.getScore() - 1000);
                                    lobbyRef.child("Players").child(data.getKey()).setValue(currentPlayer);

                                }

                            } else if (data.getKey().equals(properties.getFinalDareLoserTwo())) {

                                if (finalDare.getVoteCount() < finalDare.getVoteCountExtra()) {
                                    currentPlayer.setScore(currentPlayer.getScore() + 1000);
                                    lobbyRef.child("Players").child(data.getKey()).setValue(currentPlayer);

                                } else if (finalDare.getVoteCount() > finalDare.getVoteCountExtra()) {
                                    currentPlayer.setScore(currentPlayer.getScore() - 1000);
                                    lobbyRef.child("Players").child(data.getKey()).setValue(currentPlayer);

                                }
                            }
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
}



