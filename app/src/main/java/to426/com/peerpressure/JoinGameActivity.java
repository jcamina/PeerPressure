package to426.com.peerpressure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_join_game);

        enterButton = findViewById(R.id.enterButton);
        lobbyCodeEditText = findViewById(R.id.lobbyCodeEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);

        enterButton.setOnClickListener(this);

        //Database & Auth
        gameDatabase = FirebaseDatabase.getInstance();
        gameRef = gameDatabase.getReference();
        playerAuth = FirebaseAuth.getInstance();

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent JoinToJoinCreate = new Intent(JoinGameActivity.this, JoinCreateGameActivity.class);
                startActivity(JoinToJoinCreate);
                finish();
            }
        });

        //Reassigns the Green Check Key in Keyboard!
        nicknameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        enterButton.setEnabled(false);

                        joinGame();

                        return true;
                    }
                return false;
            }
        });

    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == enterButton){

            enterButton.setEnabled(false);

            joinGame();

        }
    }

    public void joinGame() {
        final String NICKNAME = nicknameEditText.getText().toString();
        final String LOBBYCODE = lobbyCodeEditText.getText().toString().toUpperCase();

        final int SCORE = 0;
        final boolean ISHOST = false;
        final String UIDCLIENT = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Checks if Lobby Code is Empty
        if (NICKNAME.isEmpty() || LOBBYCODE.isEmpty()) {

            Toast.makeText(this, "ERROR: Please enter Nickname AND Lobby Code!", Toast.LENGTH_SHORT).show();

            nicknameEditText.startAnimation(shakeError());//shake animation ;)
            lobbyCodeEditText.startAnimation(shakeError());//shake animation ;)

            nicknameEditText.setText("");
            lobbyCodeEditText.setText("");

            enterButton.setEnabled(true);

        } else {

            final DatabaseReference lobbyCheckRef = FirebaseDatabase.getInstance().getReference()
                    .child("Games").child(LOBBYCODE);

            lobbyCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        if (!(dataSnapshot.child("Players").child(UIDCLIENT).exists()) && (dataSnapshot.child("Players").getChildrenCount() < 9)) {

                            //Creates Player within the Lobby
                            gameRef.child("Games").child(LOBBYCODE).child("Players").child(UIDCLIENT)
                                    .setValue(new Player(NICKNAME, SCORE, ISHOST));

                            //Joins Lobby if Exists
                            Intent joinGameToJoinGameLobby = new Intent(JoinGameActivity.this, JoinGameLobbyActivity.class);
                            joinGameToJoinGameLobby.putExtra("lobbyCode", LOBBYCODE);
                            startActivity(joinGameToJoinGameLobby);
                            finish();


                        } else {
                            Toast.makeText(JoinGameActivity.this, "ERROR: Somebody is Already Logged-in With This Account or There Are Already 8 Players",
                                    Toast.LENGTH_SHORT).show();

                            enterButton.setEnabled(true);

                        }

                    } else {
                        Toast.makeText(JoinGameActivity.this, "ERROR: Lobby " + LOBBYCODE + " Does Not Exist!",
                                Toast.LENGTH_SHORT).show();

                        nicknameEditText.startAnimation(shakeError());//shake animation ;)
                        lobbyCodeEditText.startAnimation(shakeError());//shake animation ;)

                        nicknameEditText.setText("");
                        lobbyCodeEditText.setText("");

                        enterButton.setEnabled(true);

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

    //Disable Back Button
    @Override
    public void onBackPressed() {
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
