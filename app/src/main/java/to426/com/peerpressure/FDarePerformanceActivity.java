package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_fdare_performance);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

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

                Intent FFinalDareLoadingToFLoserseHold = new Intent(FDarePerformanceActivity.this, FLobbyDareHoldActivity.class);
                FFinalDareLoadingToFLoserseHold.putExtra("lobbyCode", lobbyCode);
                startActivity(FFinalDareLoadingToFLoserseHold);
                finish();

            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();

    }


}
