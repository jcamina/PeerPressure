package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class RReadyVoteHostActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rready_vote_host);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference lobbyHoldRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyHoldRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("Players").getChildrenCount() == dataSnapshot.child("Dares").getChildrenCount()) {

                        final ArrayList<String> dareUIDS = new ArrayList();

                        for (DataSnapshot data : dataSnapshot.child("Dares").getChildren()) {

                            Dare currentDare = data.getValue(Dare.class);

                            if (currentDare.getDareUsed().equals("Unused")) {

                                dareUIDS.add(data.getKey());
                            }

                        }

                        try {

                            String randomDare1 = "";
                            String randomDare2 = "";

                            int dareCount = dareUIDS.size();

                            int randomVal1 = new Random().nextInt(dareCount);

                            randomDare1 = dareUIDS.get(randomVal1);

                            dareUIDS.remove(randomVal1);

                            randomDare2 = dareUIDS.get(new Random().nextInt(dareCount - 1));

                            lobbyHoldRef.child("Dares").child(randomDare1).child("dareUsed").setValue("selectOne");
                            lobbyHoldRef.child("Dares").child(randomDare2).child("dareUsed").setValue("selectTwo");

                            final String UIDPLAYER = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            GameProperties currentProperties = dataSnapshot.child("Properties").getValue(GameProperties.class);

                            currentProperties.setDareRoundRandomized(true);

                            lobbyHoldRef.child("Properties").setValue(currentProperties);

                            lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected


                            if (UIDPLAYER.equals(randomDare1) || UIDPLAYER.equals(randomDare2)) {

                                Intent RReadVoteToRVoteWait = new Intent(RReadyVoteHostActivity.this, RVoteWaitActivity.class);
                                RReadVoteToRVoteWait.putExtra("lobbyCode", lobbyCode);
                                startActivity(RReadVoteToRVoteWait);

                                lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected

                                finish();

                            } else {

                                Intent RReadVoteToRVoteActive = new Intent(RReadyVoteHostActivity.this, RVoteActiveActivity.class);
                                RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                                startActivity(RReadVoteToRVoteActive);

                                lobbyHoldRef.removeEventListener(this); // stops from assigning every single one as selected

                                finish();

                            }

                            } catch (Exception e) {

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


}

