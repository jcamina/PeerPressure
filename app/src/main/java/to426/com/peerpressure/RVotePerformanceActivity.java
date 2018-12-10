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

public class RVotePerformanceActivity extends AppCompatActivity implements View.OnClickListener {

    public Button mehButton;
    public Button okButton;
    public Button goodButton;
    public Button greatButton;
    public Button amazingButton;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rvote_performance);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mehButton = findViewById(R.id.mehButton);
        okButton = findViewById(R.id.okButton);
        goodButton = findViewById(R.id.goodButton);
        greatButton = findViewById(R.id.greatButton);
        amazingButton = findViewById(R.id.amazingButton);

        mehButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        goodButton.setOnClickListener(this);
        greatButton.setOnClickListener(this);
        amazingButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == mehButton) {

            changeScore(-200);

            mehButton.setEnabled(false);
            okButton.setEnabled(false);
            goodButton.setEnabled(false);
            greatButton.setEnabled(false);
            amazingButton.setEnabled(false);


            Intent RVotePerformanceToRPostPerformance =
                    new Intent(RVotePerformanceActivity.this,RPostPerformaceActivity.class);
            RVotePerformanceToRPostPerformance.putExtra("lobbyCode", lobbyCode);
            startActivity(RVotePerformanceToRPostPerformance);
            finish();

        } else if (v == okButton) {

            changeScore(-100);

            mehButton.setEnabled(false);
            okButton.setEnabled(false);
            goodButton.setEnabled(false);
            greatButton.setEnabled(false);
            amazingButton.setEnabled(false);

            Intent RVotePerformanceToRPostPerformance =
                    new Intent(RVotePerformanceActivity.this,RPostPerformaceActivity.class);
            RVotePerformanceToRPostPerformance.putExtra("lobbyCode", lobbyCode);
            startActivity(RVotePerformanceToRPostPerformance);
            finish();

        } else if (v == goodButton) {

            changeScore(0);

            mehButton.setEnabled(false);
            okButton.setEnabled(false);
            goodButton.setEnabled(false);
            greatButton.setEnabled(false);
            amazingButton.setEnabled(false);

            Intent RVotePerformanceToRPostPerformance =
                    new Intent(RVotePerformanceActivity.this,RPostPerformaceActivity.class);
            RVotePerformanceToRPostPerformance.putExtra("lobbyCode", lobbyCode);
            startActivity(RVotePerformanceToRPostPerformance);
            finish();

        } else if (v == greatButton) {

            changeScore(100);

            mehButton.setEnabled(false);
            okButton.setEnabled(false);
            goodButton.setEnabled(false);
            greatButton.setEnabled(false);
            amazingButton.setEnabled(false);

            Intent RVotePerformanceToRPostPerformance =
                    new Intent(RVotePerformanceActivity.this,RPostPerformaceActivity.class);
            RVotePerformanceToRPostPerformance.putExtra("lobbyCode", lobbyCode);
            startActivity(RVotePerformanceToRPostPerformance);
            finish();

        } else if (v == amazingButton) {

            changeScore(200);

            mehButton.setEnabled(false);
            okButton.setEnabled(false);
            goodButton.setEnabled(false);
            greatButton.setEnabled(false);
            amazingButton.setEnabled(false);

            Intent RVotePerformanceToRPostPerformance =
                    new Intent(RVotePerformanceActivity.this,RPostPerformaceActivity.class);
            RVotePerformanceToRPostPerformance.putExtra("lobbyCode", lobbyCode);
            startActivity(RVotePerformanceToRPostPerformance);
            finish();

        }
    }

    public void changeScore(final int inScore){

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Needed To Double the Points Awarded For Round 2
                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);
                    int scaleFactor = 1;

                    if (currentProperties.getRoundOneComplete()) {
                        scaleFactor = 2;
                    }

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

                        Player playerLost = dataSnapshot.child("Players").child(dareTwoUID).getValue(Player.class);

                        playerLost.setScore(playerLost.getScore() + (inScore * scaleFactor));

                        currentLobby.child("Players").child(dareTwoUID).setValue(playerLost);

                    } else if (voteTwo > voteOne) {

                        Player playerLost = dataSnapshot.child("Players").child(dareOneUID).getValue(Player.class);

                        playerLost.setScore(playerLost.getScore() + (inScore * scaleFactor));

                        currentLobby.child("Players").child(dareOneUID).setValue(playerLost);

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

            Intent toRules = new Intent(RVotePerformanceActivity.this, RulesActivity.class);
            RVotePerformanceActivity.this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
