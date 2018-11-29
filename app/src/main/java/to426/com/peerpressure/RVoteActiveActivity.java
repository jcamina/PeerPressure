package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RVoteActiveActivity extends AppCompatActivity implements View.OnClickListener {

    public String lobbyCode = "";
    public Button dareTwoButton;
    public Button dareOneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvote_active);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        dareOneButton = (Button) findViewById(R.id.dareOneButton);
        dareTwoButton = (Button) findViewById(R.id.dareTwoButton);

        dareOneButton.setOnClickListener(this);
        dareTwoButton.setOnClickListener(this);

        setDareButton("selectOne");
        setDareButton("selectTwo");

    }

    public void setDareButton(String inDareNum){

        final Query selectedDares = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Dares").orderByChild("dareUsed").equalTo(inDareNum);

        selectedDares.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        //If Borrow Byy Value Matches, Add to Array List "Books"
                        Dare currentDare = data.getValue(Dare.class);

                        try {

                            if (currentDare.getDareUsed().equals("selectOne")){
                                dareOneButton.setText(currentDare.getDareMessage());
                                selectedDares.removeEventListener(this); // stops from assigning every single one as selected


                            }   else if (currentDare.getDareUsed().equals("selectTwo")){
                                dareTwoButton.setText(currentDare.getDareMessage());
                                selectedDares.removeEventListener(this); // stops from assigning every single one as selected
                            }

                        } catch (Exception e) {
                            Toast.makeText(RVoteActiveActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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

    public void incrementDareVote(String inDareSelected) {

        final DatabaseReference dares = FirebaseDatabase.getInstance().getReference().child("Games").child(lobbyCode).child("Dares");

        final Query selectedDares = dares.orderByChild("dareUsed").equalTo(inDareSelected);

        selectedDares.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Dare currentDare = data.getValue(Dare.class);

                        try {
                            currentDare.setVoteCount(currentDare.getVoteCount() + 1);

                            dares.child(data.getKey()).setValue(currentDare);

                        } catch (Exception e) {
                            Toast.makeText(RVoteActiveActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View v) {

        if (v == dareOneButton) {

            incrementDareVote("selectOne");



            Intent RVoteActiveToRAfterVoteHold = new Intent(RVoteActiveActivity.this,RPostVoteHold.class);
            RVoteActiveToRAfterVoteHold.putExtra("lobbyCode", lobbyCode);
            startActivity(RVoteActiveToRAfterVoteHold);
            finish();


        } else if (v == dareTwoButton) {

            incrementDareVote("selectTwo");



            Intent RVoteActiveToRAfterVoteHold = new Intent(RVoteActiveActivity.this,RPostVoteHold.class);
            RVoteActiveToRAfterVoteHold.putExtra("lobbyCode", lobbyCode);
            startActivity(RVoteActiveToRAfterVoteHold);
            finish();


        }

    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }
}
