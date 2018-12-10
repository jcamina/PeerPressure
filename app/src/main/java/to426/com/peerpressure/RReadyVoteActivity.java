package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RReadyVoteActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rready_vote);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

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

                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    if (currentProperties.getDareRoundRandomized())
                    {
                        final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Dare playerDare = dataSnapshot.child("Dares").child(UIDPLAYER).getValue(Dare.class);

                        if (playerDare.getDareUsed().equals("selectOne") || playerDare.getDareUsed().equals("selectTwo")) {

                            Intent RReadVoteToRVoteWait = new Intent(RReadyVoteActivity.this, RVoteWaitActivity.class);
                            RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteWait);

                        } else {

                            Intent RReadVoteToRVoteActive = new Intent(RReadyVoteActivity.this,RVoteActiveActivity.class);
                            RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteActive);

                        }

                       currentLobby.removeEventListener(this);

                        finish();

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

            Intent toRules = new Intent(this, RulesActivity.class);
            this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
