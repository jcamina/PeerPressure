package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FFinalVoteActivity extends AppCompatActivity implements View.OnClickListener {

    String lobbyCode = "";
    public Button finalDareVoteOneButton;
    public Button finalDareVoteTwoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ffinal_vote);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        finalDareVoteOneButton = findViewById(R.id.finalDareVoteOneButton);
        finalDareVoteTwoButton = findViewById(R.id.finalDareVoteTwoButton);

        finalDareVoteTwoButton.setOnClickListener(this);
        finalDareVoteOneButton.setOnClickListener(this);

        //Set Buttons For Voting
        setFinalDareButtons();

    }

    public void incrementDareVote(final String inDareSelected) {

        final DatabaseReference finalDareRef = FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares");

        finalDareRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Dare finalDare = dataSnapshot.child("Final Dare").getValue(Dare.class);

                    //Changes Dare Vote Based On Which Button is Clicked
                    if (inDareSelected.equals("selectOne")) {

                        finalDare.setVoteCount(finalDare.getVoteCount() + 1);

                    } else if (inDareSelected.equals("selectTwo")){

                        finalDare.setVoteCountExtra(finalDare.getVoteCountExtra() + 1);

                    }

                    finalDareRef.child("Final Dare").setValue(finalDare);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setFinalDareButtons(){

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final GameProperties properties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                    //Loops to find the correct data for Buttons
                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                        Player currentPlayer = data.getValue(Player.class);

                        if (data.getKey().equals(properties.getFinalDareLoserOne())) {

                            finalDareVoteOneButton.setText(currentPlayer.getNickname());

                        } else if (data.getKey().equals(properties.getFinalDareLoserTwo())) {

                            finalDareVoteTwoButton.setText(currentPlayer.getNickname());

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onClick(View v) {

        //Click One of these buttons to vote!
        if (v == finalDareVoteOneButton) {

            incrementDareVote("selectOne");

            finalDareVoteOneButton.setEnabled(false);
            finalDareVoteTwoButton.setEnabled(false);

            Intent RVoteActiveToRAfterVoteHold = new Intent(FFinalVoteActivity.this,FPostFinalVoteHoldActivity.class);
            RVoteActiveToRAfterVoteHold.putExtra("lobbyCode", lobbyCode);
            startActivity(RVoteActiveToRAfterVoteHold);

            finish();

        } else if (v == finalDareVoteTwoButton) {

            incrementDareVote("selectTwo");

            finalDareVoteOneButton.setEnabled(false);
            finalDareVoteTwoButton.setEnabled(false);

            Intent RVoteActiveToRAfterVoteHold = new Intent(FFinalVoteActivity.this,FPostFinalVoteHoldActivity.class);
            RVoteActiveToRAfterVoteHold.putExtra("lobbyCode", lobbyCode);
            startActivity(RVoteActiveToRAfterVoteHold);

            finish();
        }
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
