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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class FFinalDareLoadingHostActivity extends AppCompatActivity {

    String lobbyCode = "";
    public TextView finalDareTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffinal_dare_loading_host);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        finalDareTitleTextView = findViewById(R.id.finalDareTitleTextView);


        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        selectFinalDareParticipants();

        new CountDownTimer(4000, 1000) {
            public void onFinish() {



                final Intent FFinalDareLoadingToFLeaderDareEnter = new Intent(FFinalDareLoadingHostActivity.this, FLeaderDareEnterActivity.class);
                FFinalDareLoadingToFLeaderDareEnter.putExtra("lobbyCode", lobbyCode);

                startActivity(FFinalDareLoadingToFLeaderDareEnter);
                finish();



            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();
    }

    public void selectFinalDareParticipants() {

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.exists()) {

                        final ArrayList<Player> playerScores = new ArrayList();
                        String loser1UID = "";
                        String loser2UID = "";

                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            playerScores.add(new Player(data.getKey(),currentPlayer.getNickname(),currentPlayer.getScore(),currentPlayer.getIsHost()));

                        }

                        // Sort in Birds In Proper Order
                        Collections.sort(playerScores, new Comparator<Player>() {
                            public int compare(Player p1, Player p2) {
                                return Integer.valueOf(p1.getScore()).compareTo(p2.getScore());
                            }
                        });


                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            if (data.getKey().equals(playerScores.get(0).getUID())) {

                                loser1UID = data.getKey();

                            } else if (data.getKey().equals(playerScores.get(1).getUID())) {

                                loser2UID = data.getKey();

                            }
                        }

                        lobbyRef.child("Properties").setValue(new GameProperties("Final Round",
                                "Default",0,true,true,loser1UID, loser2UID));

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}
