package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FDarePerformanceActivity extends AppCompatActivity {

    String lobbyCode = "";

    public TextView darePerformanceNicknameTextView;
    public TextView darePerformanceDareTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_fdare_performance);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        darePerformanceNicknameTextView = findViewById(R.id.darePerformanceNicknameTextView);
        darePerformanceDareTextView = findViewById(R.id.darePerformanceDareTextView);

        final DatabaseReference loserPlayerRef = FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode);

        loserPlayerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player loserPlayer = dataSnapshot.child("Players").child(UIDPLAYER).getValue(Player.class);

                    Dare finalDare = dataSnapshot.child("Dares").child("Final Dare").getValue(Dare.class);

                    darePerformanceNicknameTextView.setText(loserPlayer.getNickname());
                    darePerformanceDareTextView.setText(finalDare.getDareMessage());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        new CountDownTimer(6000, 1000) {
            public void onFinish() {

                Intent FFinalDareLoadingToFLoserseHold = new Intent(FDarePerformanceActivity.this, FLosersHoldActivity.class);
                FFinalDareLoadingToFLoserseHold.putExtra("lobbyCode", lobbyCode);
                startActivity(FFinalDareLoadingToFLoserseHold);

                finish();
            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();

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

            Intent toRules = new Intent(this, RulesActivity.class);
            this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
