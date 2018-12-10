package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class FLeaderboardSplash extends AppCompatActivity implements View.OnClickListener {

    String lobbyCode = "";
    public TextView leaderBoardOutputTextView;
    public TextView biggestLoserOutputTextView;
    public Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_fleaderboard_splash);


        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leaderBoardOutputTextView = findViewById(R.id.leaderBoardOutputTextView);
        biggestLoserOutputTextView = findViewById(R.id.biggestLoserOutputTextView);
        mainMenuButton = findViewById(R.id.mainMenuButton);

        mainMenuButton.setOnClickListener(this);

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
                            builder.append(details.getNickname() + " Score: " + details.getScore() + "\n");
                        }


                        leaderBoardOutputTextView.setText(builder.toString());

                        biggestLoserOutputTextView.setText(playerScores.get(0).getNickname());

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

    @Override
    public void onClick(View v) {

        if (v == mainMenuButton){

            final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference().child("Games");

            lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (dataSnapshot.child(lobbyCode).child("Players").child(UIDCLIENT).exists()) {

                            Player currentPlayer = dataSnapshot.child(lobbyCode).child("Players").child(UIDCLIENT).getValue(Player.class);

                            if (currentPlayer.getIsHost()) {

                                lobbyRef.child(lobbyCode).removeValue();

                            }
                        }

                        Intent FLeaderBoardToJoinCreate = new Intent(FLeaderboardSplash.this,JoinCreateGameActivity.class);
                        FLeaderBoardToJoinCreate.putExtra("lobbyCode", lobbyCode);
                        startActivity(FLeaderBoardToJoinCreate);
                        finish();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
        }
    }
}
