package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FPostFinalVoteHoldActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpost_final_vote_hold);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }

        final DatabaseReference finalDareAdvanceRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        finalDareAdvanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Dare finalDare = dataSnapshot.child("Dares").child("Final Dare").getValue(Dare.class);

                    try {

                        if ((finalDare.getVoteCount() + finalDare.getVoteCountExtra()) == (dataSnapshot.child("Players").getChildrenCount() - 2)) {

                            Intent FPostFinalVoteHoldToFBLSuspense = new Intent(FPostFinalVoteHoldActivity.this,
                                   FBiggestLoserSuspenseActivity.class);

                            FPostFinalVoteHoldToFBLSuspense.putExtra("lobbyCode", lobbyCode);

                            startActivity(FPostFinalVoteHoldToFBLSuspense);

                            finalDareAdvanceRef.removeEventListener(this);

                            finish();
                        }

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

