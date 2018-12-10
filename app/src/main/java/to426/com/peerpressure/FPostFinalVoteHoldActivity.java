package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FPostFinalVoteHoldActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_fpost_final_vote_hold);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DatabaseReference finalDareAdvanceRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        finalDareAdvanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Dare finalDare = dataSnapshot.child("Dares").child("Final Dare").getValue(Dare.class);

                    try {

                        if ((finalDare.getVoteCount() + finalDare.getVoteCountExtra()) == (dataSnapshot.child("Players").getChildrenCount() - 2)) {

                            Intent FPostFinalVoteHoldToFBLSuspense = new Intent(FPostFinalVoteHoldActivity.this,
                                   FBiggestLoserSuspenseActivity.class);

                            FPostFinalVoteHoldToFBLSuspense.putExtra("lobbyCode", lobbyCode);

                            startActivity(FPostFinalVoteHoldToFBLSuspense);

                            finalDareAdvanceRef.removeEventListener(this);

                            finish();
                        }

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

