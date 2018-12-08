package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
    public TextView leaderBoardOutputTextView;
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

        leaderBoardOutputTextView = findViewById(R.id.leaderBoardOutputTextView);
        biggestLoserOutputTextView = findViewById(R.id.biggestLoserOutputTextView);

        setLeaderboardDisplay();

    }


    public void setLeaderboardDisplay() {

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

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


                        // StringBuilder Used to Print Array List To TextView
                        StringBuilder builder = new StringBuilder();
                        for (Player details : playerScores) {
                            builder.append(details.getNickname() + " Score:" + details.getScore() + "\n");
                        }


                        leaderBoardOutputTextView.setText(builder.toString());

                        biggestLoserOutputTextView.setText(playerScores.get(0).getNickname() + " " + playerScores.get(0).getScore());

                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}
