package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

public class FLobbyDareHoldActivity extends AppCompatActivity {

    public String lobbyCode = "";
    public MediaPlayer mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_flobby_dare_hold);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mm = new MediaPlayer();
        mm = MediaPlayer.create(this, R.raw.theme);
        mm.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mm.setLooping(true);
        mm.start();

        final DatabaseReference hostCheckRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Dares");

        hostCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                   if  (dataSnapshot.child("Final Dare").exists()) {

                       Intent FLobbyDareHoldToFFinalDareSplash = new Intent(FLobbyDareHoldActivity.this,FFinalDareSplashActivity.class);
                       FLobbyDareHoldToFFinalDareSplash.putExtra("lobbyCode", lobbyCode);
                       startActivity(FLobbyDareHoldToFFinalDareSplash);

                       hostCheckRef.removeEventListener(this);

                       finish();
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

    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing()){
            mm.stop();
        }
    }
}
