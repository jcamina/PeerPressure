package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FFinalVoteActivity extends AppCompatActivity implements View.OnClickListener {

    String lobbyCode = "";
    public Button finalDareVoteOneButton;
    public Button finalDareVoteTwoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffinal_vote);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        finalDareVoteOneButton = findViewById(R.id.finalDareVoteOneButton);
        finalDareVoteTwoButton = findViewById(R.id.finalDareVoteTwoButton);

        finalDareVoteTwoButton.setOnClickListener(this);
        finalDareVoteOneButton.setOnClickListener(this);

        setFinalDareButtons();

    }

    public void incrementDareVote(final String inDareSelected) {

        final DatabaseReference finalDareRef = FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares");

        finalDareRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                        Dare finalDare = dataSnapshot.child("Final Dare").getValue(Dare.class);

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
                // ...
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

                    final ArrayList<Integer> playerScores = new ArrayList();

                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                        Player currentPlayer = data.getValue(Player.class);

                        playerScores.add(currentPlayer.getScore());
                    }

                    Collections.sort(playerScores);


                    for (DataSnapshot data : dataSnapshot.child("Players").getChildren()) {

                        Player currentPlayer = data.getValue(Player.class);

                        if (currentPlayer.getScore() == playerScores.get(0)) {
                            finalDareVoteOneButton.setText(currentPlayer.getNickname());

                        } else if (currentPlayer.getScore() == playerScores.get(1)) {
                            finalDareVoteTwoButton.setText(currentPlayer.getNickname());

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



    @Override
    public void onClick(View v) {

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
}
