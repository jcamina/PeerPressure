package to426.com.peerpressure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Button enterButton;
    public EditText lobbyCodeEditText;
    public EditText nicknameEditText;

    //Database & Auth
    private DatabaseReference gameRef;
    private FirebaseDatabase gameDatabase;
    private FirebaseAuth playerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        enterButton = (Button) findViewById(R.id.enterButton);
        lobbyCodeEditText = (EditText) findViewById(R.id.lobbyCodeEditText);
        nicknameEditText = (EditText) findViewById(R.id.nicknameEditText);

        enterButton.setOnClickListener(this);

        //Database & Auth
        gameDatabase = FirebaseDatabase.getInstance();
        gameRef = gameDatabase.getReference();
        playerAuth = FirebaseAuth.getInstance();
    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {

        Intent joinGameToJoinGameLobby = new Intent(JoinGameActivity.this, JoinGameLobbyActivity.class);

        if (v == enterButton){

            joinGame();

            //startActivity(joinGameToJoinGameLobby);
        }
    }

    public void joinGame() {
        final String NICKNAME = nicknameEditText.getText().toString();
        final String GAMECODE = lobbyCodeEditText.getText().toString().toUpperCase();

        final int SCORE  = 0;
        final boolean ISHOST = false;
        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (NICKNAME.isEmpty() || GAMECODE.isEmpty()) {

            Toast.makeText(this,"ERROR: Please enter Nickname AND Lobby Code!",Toast.LENGTH_SHORT).show();

            nicknameEditText.startAnimation(shakeError());//shake animation ;)
            lobbyCodeEditText.startAnimation(shakeError());//shake animation ;)

            nicknameEditText.setText("");
            lobbyCodeEditText.setText("");

        }  else {
            final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                    .child("Games").child(GAMECODE);

            lobbyCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        if (!(dataSnapshot.child("Players").child(UIDCLIENT).exists())) {

                            gameRef.child("Games").child(GAMECODE).child("Players").child(UIDCLIENT)
                                    .setValue(new Player(UIDCLIENT, NICKNAME, SCORE, ISHOST));

                            Toast.makeText(JoinGameActivity.this, "Lobby Joined Successfully!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(JoinGameActivity.this,"ERROR: Somebody is Using Your Username!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(JoinGameActivity.this,"ERROR: Lobby " + GAMECODE + " Does Not Exist!",
                                Toast.LENGTH_SHORT).show();

                        nicknameEditText.startAnimation(shakeError());//shake animation ;)
                        lobbyCodeEditText.startAnimation(shakeError());//shake animation ;)

                        nicknameEditText.setText("");
                        lobbyCodeEditText.setText("");


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });

        }

    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(300);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
}
