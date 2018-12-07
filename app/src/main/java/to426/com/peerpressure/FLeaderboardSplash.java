package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FLeaderboardSplash extends AppCompatActivity {

    String lobbyCode = "";
    public TextView restofFieldTextView;
    public TextView biggestLoserOutputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleaderboard_splash);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        restofFieldTextView = findViewById(R.id.restofFieldTextView);
        biggestLoserOutputTextView = findViewById(R.id.biggestLoserOutputTextView);

        setLeaderboardDisplay();

    }


    public void setLeaderboardDisplay() {

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.exists()) {

                        final ArrayList<Player> playerScores = new ArrayList();

                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);

                            playerScores.add(currentPlayer);
                        }

                        // Sort Lowest Score To Highest Score

                        // Sort in Birds In Proper Order
                        Collections.sort(playerScores, new Comparator<Player>() {
                            public int compare(Player p1, Player p2) {
                                return Integer.valueOf(p1.getScore()).compareTo(p2.getScore());
                            }
                        });


                        for (int i = 1; i < playerScores.size(); i++) {
                            restofFieldTextView.append(playerScores.get(i).getNickname() + " " + playerScores.get(i).getScore() + "\n");
                        }

                        biggestLoserOutputTextView.setText(playerScores.get(0).getNickname());

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
