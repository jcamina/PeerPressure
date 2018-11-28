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

        final DatabaseReference lobbyHoldRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

        lobbyHoldRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("Players").getChildrenCount() == dataSnapshot.child("Dares").getChildrenCount())
                    {
                        randomizeDareSelect();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    public void randomizeDareSelect() {


        final DatabaseReference daresRef = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Dares");

        daresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    final ArrayList<String> dareUIDS = new ArrayList();


                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                             Dare currentDare = data.getValue(Dare.class);

                             if (currentDare.getDareUsed().equals("Unused")){

                                 dareUIDS.add(data.getKey());
                             }

                        }

                        try {

                            String randomDare1 = dareUIDS.get(new Random().nextInt(dareUIDS.size()));
                            String randomDare2 = dareUIDS.get(new Random().nextInt(dareUIDS.size()));

                             while (randomDare1.equals(randomDare2)){
                                 randomDare2 = dareUIDS.get(new Random().nextInt(dareUIDS.size()));


                            }

                            daresRef.child(randomDare1).child("dareUsed").setValue("Selected1");
                            daresRef.child(randomDare2).child("dareUsed").setValue("Selected2");

                            Intent RReadVoteToRVoteActive = new Intent(RReadyVoteHostActivity.this,RVoteActiveActivity.class);
                            RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                            startActivity(RReadVoteToRVoteActive);

                        } catch (Exception e){

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
