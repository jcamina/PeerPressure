package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateNewGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Spinner modeSelectSpinner;
    public Button enterButton;
    public EditText nicknameEditText;

    //Database & Auth
    private DatabaseReference gameRef;
    private FirebaseDatabase gameDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_create_new_game);

        enterButton = findViewById(R.id.enterButton);
        modeSelectSpinner = findViewById(R.id.modeSelectSpinner);
        nicknameEditText = findViewById(R.id.nicknameEditText);

        enterButton.setOnClickListener(this);

        //Database & Auth
        gameDatabase = FirebaseDatabase.getInstance();
        gameRef = gameDatabase.getReference();

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener for Back Button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateToJoinCreate = new Intent(CreateNewGameActivity.this, JoinCreateGameActivity.class);
                CreateNewGameActivity.this.startActivity(CreateToJoinCreate);
                finish();
            }
        });

        //Reassigns the Green Check Key in Keyboard!
        nicknameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        Intent createNewGameToNewGameLobby = new Intent(CreateNewGameActivity.this,
                                CreateNewGameLobbyActivity.class);

                        String lobbyCode = createNewGame();

                        if (!(lobbyCode.equals("Error"))) {
                            //Send the Game Code To The Next Screen
                            createNewGameToNewGameLobby.putExtra("lobbyCode", lobbyCode);

                            enterButton.setEnabled(false);

                            startActivity(createNewGameToNewGameLobby);
                            finish();

                        }
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

    //Creates a New Lobby
    public String createNewGame() {

        final int SCORE  = 0;
        final boolean ISHOST = true;
        final String UIDHOST = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String LOBBYCODE = getGameCode();
        final String NICKNAME = nicknameEditText.getText().toString();
        final String MODESELECT = modeSelectSpinner.getSelectedItem().toString();

        if (NICKNAME.isEmpty()) {

            Toast.makeText(this, "ERROR: Enter A Nickname!", Toast.LENGTH_SHORT).show();

            return "Error";

        } else {

            //Creates Host Player Within The New Game
            gameRef.child("Games").child(LOBBYCODE).child("Players").child(UIDHOST).
                    setValue(new Player(NICKNAME, SCORE, ISHOST));

            //Sets the Initial Lobby Properties
            gameRef.child("Games").child(LOBBYCODE).child("Properties").
                    setValue(new GameProperties("Lobby",MODESELECT,
                            false,0,false,
                            false));

            return LOBBYCODE;
        }
    }

    //Randomizes 6 Char String and Returns the Value
    protected String getGameCode() {
        final String AVAILCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        while (code.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * AVAILCHARS.length());
            code.append(AVAILCHARS.charAt(index));
        }

        String gameCode = code.toString();
        return gameCode;
    }

    @Override
    public void onClick(View v) {

        if (v == enterButton){

            Intent createNewGameToNewGameLobby = new Intent(CreateNewGameActivity.this,
                    CreateNewGameLobbyActivity.class);

            String lobbyCode = createNewGame();

            if (!(lobbyCode.equals("Error"))) {
                //Send the Game Code To The Next Screen
                createNewGameToNewGameLobby.putExtra("lobbyCode", lobbyCode);

                enterButton.setEnabled(false);

                startActivity(createNewGameToNewGameLobby);
                finish();
            }
        }
    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    //Info Button Listener 
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
