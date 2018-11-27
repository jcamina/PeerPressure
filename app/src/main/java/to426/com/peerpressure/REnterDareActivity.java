package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class REnterDareActivity extends AppCompatActivity implements View.OnClickListener{

    public Button submitButton;
    public TextView currentPlayerNameTextView;

    public String lobbyCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_dare);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        submitButton = (Button) findViewById(R.id.submitButton);
        currentPlayerNameTextView = (TextView) findViewById(R.id.currentPlayerNameTextView);

        submitButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");
        }

        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        final DatabaseReference currentPlayer = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(lobbyCode).child("Players").child(currentFirebaseUser.getUid());

        currentPlayer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Player currentPlayer = dataSnapshot.getValue(Player.class);

                    currentPlayerNameTextView.setText(currentPlayer.getNickname() + ",");

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


        if (v == submitButton){

        }

    }
}
