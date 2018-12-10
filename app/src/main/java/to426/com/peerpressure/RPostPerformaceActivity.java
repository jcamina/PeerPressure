package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RPostPerformaceActivity extends AppCompatActivity {

    public String lobbyCode = "";
    public ImageView postPerformanceImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rpost_performace);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Hourglass Gif
        postPerformanceImageView = findViewById(R.id.postPerformanceImageView);
        Glide.with(this).asGif().load(R.drawable.hourglass).into(postPerformanceImageView);

        updateNumVoted();

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (properties.getNumVoted() == (dataSnapshot.child("Players").getChildrenCount() - 1) ) {

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                        // Send To Host End of Round Part
                        if (currentPlayer.getIsHost()) {

                            Intent RDareLoserToRHostEndPartRound = new Intent(RPostPerformaceActivity.this,RHostEndPartRoundActivity.class);
                            RDareLoserToRHostEndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToRHostEndPartRound);
                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        } else {
                            Intent RDareLoserToREndPartRound = new Intent(RPostPerformaceActivity.this,REndPartRoundActivity.class);
                            RDareLoserToREndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToREndPartRound);
                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        }
                        currentLobby.removeEventListener(this);
                    }

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

    public void updateNumVoted() {
        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    properties.setNumVoted(properties.getNumVoted() + 1);

                    currentLobby.child("Properties").setValue(properties);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}
