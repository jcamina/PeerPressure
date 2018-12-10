package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RDareLoserActivity extends AppCompatActivity {

    String lobbyCode = "";

    public TextView loserNamePlaceholderTextView;
    public TextView loserDarePlaceholderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rdare_loser);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        //Fire Gif
        ImageView imageView = (ImageView) findViewById(R.id.fireImageView);
        Glide.with(this).asGif().load(R.drawable.fire).into(imageView);

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Create Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loserNamePlaceholderTextView = findViewById(R.id.loserNamePlaceholderTextView);
        loserDarePlaceholderTextView = findViewById(R.id.loserDarePlaceholderTextView);

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Scales the Scoring Based on the Numbers of Players in the Lobby AND Round
                    double scaleFactorPlayers = 0.0;
                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (dataSnapshot.child("Players").getChildrenCount() <= 4) {
                        scaleFactorPlayers = 1.0;
                    } else if (dataSnapshot.child("Players").getChildrenCount() > 4 && dataSnapshot.child("Players").getChildrenCount() <= 6) {
                        scaleFactorPlayers = 1.5;
                    } else if (dataSnapshot.child("Players").getChildrenCount() > 6 && dataSnapshot.child("Players").getChildrenCount() <= 8){
                        scaleFactorPlayers = 2.0;
                    }

                    double scaleFactorRounds = 1.0;
                    if (currentProperties.getRoundOneComplete()){
                        scaleFactorRounds = 2.0;
                    }

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    loserNamePlaceholderTextView.setText(currentPlayer.getNickname());

                    currentPlayer.setScore(currentPlayer.getScore() - (int) (1000 * scaleFactorPlayers * scaleFactorRounds));

                    currentLobby.child("Players").child(UIDCLIENT).setValue(currentPlayer);

                    currentLobby.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


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

                        loserDarePlaceholderTextView.setText(winningDare.getDareMessage());

                    } else if (voteTwo > voteOne) {

                        Dare winningDare = dataSnapshot.child("Dares").child(dareTwoUID).getValue(Dare.class);

                        loserDarePlaceholderTextView.setText(winningDare.getDareMessage());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (properties.getNumVoted() == (dataSnapshot.child("Players").getChildrenCount() - 1) ) {

                        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                        if(currentPlayer.getIsHost()){

                            Intent RDareLoserToRHostEndPartRound = new Intent(RDareLoserActivity.this,RHostEndPartRoundActivity.class);
                            RDareLoserToRHostEndPartRound.putExtra("lobbyCode", lobbyCode);
                            startActivity(RDareLoserToRHostEndPartRound);
                            currentLobby.removeEventListener(this); // stops from assigning
                            finish();

                        } else {
                            Intent RDareLoserToREndPartRound = new Intent(RDareLoserActivity.this,REndPartRoundActivity.class);
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
}
