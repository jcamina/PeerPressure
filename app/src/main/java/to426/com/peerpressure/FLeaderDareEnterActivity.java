package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FLeaderDareEnterActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView finalDareNameTextView;
    public EditText enterFinalDareEditText;
    public Button submitFinalDareButton;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleader_dare_enter);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));


        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        finalDareNameTextView = findViewById(R.id.finalDareNameTextView);
        enterFinalDareEditText = findViewById(R.id.enterFinalDareEditText);
        submitFinalDareButton = findViewById(R.id.submitFinalDareButton);

        submitFinalDareButton.setOnClickListener(this);

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference hostCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Players").child(UID);

        hostCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Player currentPlayer = dataSnapshot.getValue(Player.class);

                    finalDareNameTextView.setText(currentPlayer.getNickname());

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

        if (v == submitFinalDareButton) {

            submitDare();

        }

    }

    public void submitDare() {

        String dareSubmissionText = enterFinalDareEditText.getText().toString();

        if (dareSubmissionText.isEmpty()) {
            Toast.makeText(this, "ERROR: No Text Inputted!", Toast.LENGTH_SHORT).show();

        } else {

            submitFinalDareButton.setEnabled(false);

            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares").setValue("");
            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares")
                    .child("Final Dare").setValue(new Dare("Final Dare", dareSubmissionText,0,"selectOne"));

            Intent REnterDareToRReadyVote = new Intent(FLeaderDareEnterActivity.this,FFinalDareSplashActivity.class);
            REnterDareToRReadyVote.putExtra("lobbyCode", lobbyCode);
            startActivity(REnterDareToRReadyVote);

            finish();

        }

    }
}
