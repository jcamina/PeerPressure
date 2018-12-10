package to426.com.peerpressure;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class REnterDareActivity extends AppCompatActivity implements View.OnClickListener{

    public Button submitButton;
    public TextView currentPlayerNameTextView;
    public EditText enterDareEditText;
    public TextView inRoundTimerTextView;
    public Button desperateDareButton;

    public String lobbyCode = "";

    public boolean dareSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_renter_dare);

        submitButton = findViewById(R.id.submitButton);
        currentPlayerNameTextView = findViewById(R.id.currentPlayerNameTextView);
        enterDareEditText = findViewById(R.id.enterDareEditText);
        inRoundTimerTextView = findViewById(R.id.inRoundTimerTextView);
        desperateDareButton = findViewById(R.id.desperateDareButton);

        submitButton.setOnClickListener(this);
        desperateDareButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Reassigns the Green Check Key in Keyboard!
        enterDareEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        dareSubmitted = true;

                        submitDare();

                        submitButton.setEnabled(false);

                        return true;
                    }
                return false;
            }
        });


        //Auto Submit Function If Time is Up!
        new CountDownTimer(90000, 1000) {
            public void onFinish() {

                if (!dareSubmitted) {
                    submitDare();
                }
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

            //Create new dare based on submission details
            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares")
                    .child(UID).setValue(new Dare(UID, dareSubmissionText,0,"Unused"));

            final DatabaseReference hostCheckRef = FirebaseDatabase.getInstance().getReference()
                    .child("Games").child(lobbyCode).child("Players").child(UID);

            hostCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        submitButton.setEnabled(false);

                        Player currentPlayer = dataSnapshot.getValue(Player.class);

                        //Sends Host Phone to Randomization Screen
                        if (currentPlayer.getIsHost()) {
                            Intent REnterDareToRReadyVoteHost = new Intent(REnterDareActivity.this,RReadyVoteHostActivity.class);
                            REnterDareToRReadyVoteHost.putExtra("lobbyCode", lobbyCode);
                            startActivity(REnterDareToRReadyVoteHost);
                            hostCheckRef.removeEventListener(this); // stops from assigning every single one as selected

                            finish();

                        //Other Phones Are Clients While Host Randomizes The Dares
                        } else {
                            Intent REnterDareToRReadyVote = new Intent(REnterDareActivity.this,RReadyVoteActivity.class);
                            REnterDareToRReadyVote.putExtra("lobbyCode", lobbyCode);
                            startActivity(REnterDareToRReadyVote);
                            hostCheckRef.removeEventListener(this); // stops from assigning every single one as selected

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
    }

    public void useDesperateDare () {

        final DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference();

        lobbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    ArrayList<String> currentDDares = new ArrayList<>();

                    submitButton.setEnabled(false);

                    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    GameProperties currentProperties = dataSnapshot.child("Games").child(lobbyCode).child("Properties").getValue(GameProperties.class);

                    int numDesperateDares = new BigDecimal(dataSnapshot.child("Desperate Dares").child(currentProperties.getGameType()).
                            getChildrenCount()).intValueExact();

                    for (DataSnapshot data : dataSnapshot.child("Desperate Dares").child(currentProperties.getGameType()).getChildren()) {

                            currentDDares.add(data.getValue(String.class));
                    }

                    int randomNum = new Random().nextInt(numDesperateDares);

                    FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares")
                            .child(UID).setValue(new Dare(UID, currentDDares.get(randomNum),0,"Unused"));


                    Player currentPlayer = dataSnapshot.child("Games").child(lobbyCode).child("Players").child(UID).getValue(Player.class);

                    //Sends Host Phone to Randomization Screen
                    if (currentPlayer.getIsHost()) {
                        Intent REnterDareToRReadyVoteHost = new Intent(REnterDareActivity.this,RReadyVoteHostActivity.class);
                        REnterDareToRReadyVoteHost.putExtra("lobbyCode", lobbyCode);
                        startActivity(REnterDareToRReadyVoteHost);
                        finish();

                        //Other Phones Are Clients While Host Randomizes The Dares
                    } else {
                        Intent REnterDareToRReadyVote = new Intent(REnterDareActivity.this,RReadyVoteActivity.class);
                        REnterDareToRReadyVote.putExtra("lobbyCode", lobbyCode);
                        startActivity(REnterDareToRReadyVote);
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


    @Override
    public void onClick(View v) {

        if (v == submitButton) {
            //Used For Countdown Timer Check
            dareSubmitted = true;

            submitDare();

            submitButton.setEnabled(false);
        } else if (v == desperateDareButton){

            useDesperateDare();

        }
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


