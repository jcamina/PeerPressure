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

public class RVoteWaitActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rvote_wait);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        ImageView imageView = (ImageView) findViewById(R.id.hourGlassImageView);
        Glide.with(this).asGif().load(R.drawable.hourglass).into(imageView);

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addValueEventListener(new ValueEventListener() {
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

                    if ((voteTwo + voteOne + 2) == (dataSnapshot.child("Players").getChildrenCount()))
                    {
                        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (voteOne > voteTwo) {

                            if (dareOneUID.equals(currentUID)){
                                Intent RPostVoteToRDareWinner = new Intent(RVoteWaitActivity.this,RDareWinnerActivity.class);
                                RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareWinner);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else if (dareTwoUID.equals(currentUID)){
                                Intent RPostVoteToRDareLoser = new Intent(RVoteWaitActivity.this,RDareLoserActivity.class);
                                RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareLoser);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else {
                                Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RVoteWaitActivity.this,RVoteActiveWinnerSplash.class);
                                RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRVoteActiveWinnerSplash);

                                currentLobby.removeEventListener(this);
                                finish();

                            }

                        } else if (voteTwo > voteOne){

                            if (dareTwoUID.equals(currentUID)){
                                Intent RPostVoteToRDareWinner = new Intent(RVoteWaitActivity.this,RDareWinnerActivity.class);
                                RPostVoteToRDareWinner.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareWinner);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else if (dareOneUID.equals(currentUID)){
                                Intent RPostVoteToRDareLoser = new Intent(RVoteWaitActivity.this,RDareLoserActivity.class);
                                RPostVoteToRDareLoser.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRDareLoser);

                                currentLobby.removeEventListener(this);
                                finish();

                            } else {
                                Intent RPostVoteToRVoteActiveWinnerSplash = new Intent(RVoteWaitActivity.this,RVoteActiveWinnerSplash.class);
                                RPostVoteToRVoteActiveWinnerSplash.putExtra("lobbyCode", lobbyCode);
                                startActivity(RPostVoteToRVoteActiveWinnerSplash);

                                currentLobby.removeEventListener(this);
                                finish();

                            }

                        } else if (voteOne == voteTwo){

                            Intent RVoteWaitToRPostVoteTie = new Intent(RVoteWaitActivity.this, RPostVoteTieHold.class);
                            RVoteWaitToRPostVoteTie.putExtra("lobbyCode", lobbyCode);
                            startActivity(RVoteWaitToRPostVoteTie);

                            currentLobby.removeEventListener(this);
                            finish();

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

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_two) {

            Intent toRules = new Intent(RVoteWaitActivity.this, RulesActivity.class);
            RVoteWaitActivity.this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
