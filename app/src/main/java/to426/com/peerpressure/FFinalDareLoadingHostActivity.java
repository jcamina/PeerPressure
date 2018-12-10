package to426.com.peerpressure;

import android.content.Intent;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FFinalDareLoadingHostActivity extends AppCompatActivity {

    String lobbyCode = "";
    public TextView finalDareTitleTextView;
    public ImageView explosionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ffinal_dare_loading_host);

        finalDareTitleTextView = findViewById(R.id.finalDareTitleTextView);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Explosion Gif with Glide
        explosionImageView = findViewById(R.id.explosionImageView);
        Glide.with(this).asGif().load(R.drawable.explosion).into(explosionImageView);

        //Select Participants
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

                        //Creating Array list so scores can be sorted right
                        final ArrayList<Player> playerScores = new ArrayList();
                        String loser1UID = "";
                        String loser2UID = "";

                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            Player currentPlayer = data.getValue(Player.class);
                            playerScores.add(new Player(data.getKey(),currentPlayer.getNickname(),
                                    currentPlayer.getScore(),currentPlayer.getIsHost()));
                        }

                        // Sort in Birds In Proper Order
                        Collections.sort(playerScores, new Comparator<Player>() {
                            public int compare(Player p1, Player p2) {
                                return Integer.valueOf(p1.getScore()).compareTo(p2.getScore());
                            }
                        });


                        //Weird Issue With UID Not copying, Used this to grab new string of key
                        for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                            if (data.getKey().equals(playerScores.get(0).getUID())) {

                                loser1UID = data.getKey();

                            } else if (data.getKey().equals(playerScores.get(1).getUID())) {

                                loser2UID = data.getKey();
                            }
                        }

                        //Create New Properties with additional parameters
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

    //Info Button OnClick
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
}
