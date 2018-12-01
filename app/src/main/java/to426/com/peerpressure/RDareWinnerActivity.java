package to426.com.peerpressure;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RDareWinnerActivity extends AppCompatActivity {

    String lobbyCode = "";

    public TextView namePlaceholderTextView;
    public TextView darePlaceholderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdare_winner);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        namePlaceholderTextView = findViewById(R.id.namePlaceholderTextView);
        darePlaceholderTextView = findViewById(R.id.darePlaceholderTextView);

        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        currentLobby.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Player currentPlayer = dataSnapshot.child("Players").child(UIDCLIENT).getValue(Player.class);

                    namePlaceholderTextView.setText(currentPlayer.getNickname());

                    currentPlayer.setScore(currentPlayer.getScore() + 500);

                    currentLobby.child("Players").child(UIDCLIENT).setValue(currentPlayer);

                    Dare winningDare = dataSnapshot.child("Dares").child(UIDCLIENT).getValue(Dare.class);

                    darePlaceholderTextView.setText(winningDare.getDareMessage());

                    currentLobby.removeEventListener(this);


                    // Need This For Later Game Progression ~ Couldn't find better spot
                    GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);
                    currentProperties.setDareRoundRandomized(false);
                    currentLobby.child("Properties").setValue(currentProperties);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        new CountDownTimer(11000, 1000) {
            public void onFinish() {

                Intent RDareWinnerSplashToRVotePerformance = new Intent(RDareWinnerActivity.this,RVotePerformanceActivity.class);
                RDareWinnerSplashToRVotePerformance.putExtra("lobbyCode", lobbyCode);
                startActivity(RDareWinnerSplashToRVotePerformance);
                finish();

            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();




    }
}
