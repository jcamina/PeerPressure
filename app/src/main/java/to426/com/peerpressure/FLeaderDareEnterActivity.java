package to426.com.peerpressure;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    public MediaPlayer mm;

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_fleader_dare_enter);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        finalDareNameTextView = findViewById(R.id.finalDareNameTextView);
        enterFinalDareEditText = findViewById(R.id.enterFinalDareEditText);
        submitFinalDareButton = findViewById(R.id.submitFinalDareButton);

        submitFinalDareButton.setOnClickListener(this);

        mm = new MediaPlayer();
        mm = MediaPlayer.create(this, R.raw.theme);
        mm.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mm.setLooping(true);
        mm.start();

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

            //Final Dare Submit Creation of Dare!
            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares").setValue("");
            FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares")
                    .child("Final Dare").setValue(new Dare("Final Dare", dareSubmissionText,0,"selectOne",0));

            Intent FLeaderDareEnterToFFinalDareSplash = new Intent(FLeaderDareEnterActivity.this,FFinalDareSplashActivity.class);
            FLeaderDareEnterToFFinalDareSplash.putExtra("lobbyCode", lobbyCode);
            startActivity(FLeaderDareEnterToFFinalDareSplash);

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

    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing()){
            mm.stop();
        }
    }
}
