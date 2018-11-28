package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class REnterDareActivity extends AppCompatActivity implements View.OnClickListener{

    public Button submitButton;
    public TextView currentPlayerNameTextView;
    public EditText enterDareEditText;
    public TextView inRoundTimerTextView;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_dare);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        submitButton = findViewById(R.id.submitButton);
        currentPlayerNameTextView = findViewById(R.id.currentPlayerNameTextView);
        enterDareEditText = findViewById(R.id.enterDareEditText);
        inRoundTimerTextView = findViewById(R.id.inRoundTimerTextView);

        submitButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        new CountDownTimer(90000, 1000) {
            public void onFinish() {

                submitDare();

                finish();
            }

            public void onTick(long millisUntilFinished) {
                inRoundTimerTextView.setText( (millisUntilFinished / 1000) + " Seconds");
            }

        }.start();

        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        final DatabaseReference currentPlayer = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Players").child(currentFirebaseUser.getUid());

        currentPlayer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Player currentPlayer = dataSnapshot.getValue(Player.class);

                    currentPlayerNameTextView.setText(currentPlayer.getNickname() + ",");

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    public void submitDare() {

        String dareSubmissionText = enterDareEditText.getText().toString();

        if (dareSubmissionText.isEmpty()) {
            Toast.makeText(this, "ERROR: No Text Inputted!", Toast.LENGTH_SHORT).show();

        } else {

            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares")
                    .child(UID).setValue(new Dare(UID, dareSubmissionText,0,"Unused"));

            final DatabaseReference hostCheckRef = FirebaseDatabase.getInstance().getReference()
                    .child("Games").child(lobbyCode).child("Players").child(UID);

            hostCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Player currentPlayer = dataSnapshot.getValue(Player.class);

                        if (currentPlayer.getIsHost()) {
                            Intent REnterDareToRReadyVoteHost = new Intent(REnterDareActivity.this,RReadyVoteHostActivity.class);
                            REnterDareToRReadyVoteHost.putExtra("lobbyCode", lobbyCode);
                            startActivity(REnterDareToRReadyVoteHost);
                        } else {

                            Intent REnterDareToRReadyVote = new Intent(REnterDareActivity.this,RReadyVoteActivity.class);
                            REnterDareToRReadyVote.putExtra("lobbyCode", lobbyCode);
                            startActivity(REnterDareToRReadyVote);
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


    @Override
    public void onClick(View v) {


        if (v == submitButton) {
            submitDare();
        }

    }




    }


