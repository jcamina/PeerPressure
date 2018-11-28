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

import java.util.ArrayList;
import java.util.Random;

public class RReadyVoteActivity extends AppCompatActivity {

    public String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rready_vote);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }


        final DatabaseReference currentLobby = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode);

       // final ArrayList<Dare> remainingDares = new ArrayList();

        currentLobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {



                     if (dataSnapshot.child("Players").getChildrenCount() == dataSnapshot.child("Dares").getChildrenCount())
                     {

                         Intent RReadVoteToRVoteActive = new Intent(RReadyVoteActivity.this,RVoteActiveActivity.class);
                         RReadVoteToRVoteActive.putExtra("lobbyCode", lobbyCode);
                         startActivity(RReadVoteToRVoteActive);

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
