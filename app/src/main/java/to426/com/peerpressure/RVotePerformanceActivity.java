package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_rvote_performance);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

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

            changeScore(-500);

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

            changeScore(-250);

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

            changeScore(250);

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

            changeScore(500);

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

            changeScore(700);

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

                        Player playerLost = dataSnapshot.child("Players").child(dareTwoUID).getValue(Player.class);

                        playerLost.setScore(playerLost.getScore() + inScore);

                        currentLobby.child("Players").child(dareTwoUID).setValue(playerLost);


                    } else if (voteTwo > voteOne) {

                        Player playerLost = dataSnapshot.child("Players").child(dareOneUID).getValue(Player.class);

                        playerLost.setScore(playerLost.getScore() + inScore);

                        currentLobby.child("Players").child(dareTwoUID).setValue(playerLost);



                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
