package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rvote_active);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dareOneButton = findViewById(R.id.dareOneButton);
        dareTwoButton = findViewById(R.id.dareTwoButton);

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

                        Dare currentDare = data.getValue(Dare.class);

                        try {

                            //Sets The Text
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

    //Called To Increment Dare Vote Count By 1 Vote
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

            dareTwoButton.setEnabled(false);
            dareOneButton.setEnabled(false);

            Intent RVoteActiveToRAfterVoteHold = new Intent(RVoteActiveActivity.this,RPostVoteHold.class);
            RVoteActiveToRAfterVoteHold.putExtra("lobbyCode", lobbyCode);
            startActivity(RVoteActiveToRAfterVoteHold);
            finish();

        } else if (v == dareTwoButton) {

            incrementDareVote("selectTwo");

            dareTwoButton.setEnabled(false);
            dareOneButton.setEnabled(false);

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

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
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
