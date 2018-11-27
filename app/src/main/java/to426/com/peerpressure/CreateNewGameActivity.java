package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth playerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_game);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        enterButton = (Button) findViewById(R.id.enterButton);
        modeSelectSpinner = (Spinner) findViewById(R.id.modeSelectSpinner);
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


        if (v == enterButton){

            Intent createNewGameToNewGameLobby = new Intent(CreateNewGameActivity.this, CreateNewGameLobbyActivity.class);

            String gameCode = createNewGame();

            Toast.makeText(this,  gameCode, Toast.LENGTH_SHORT).show();

            //Send the Game Code To The Next Screen
            createNewGameToNewGameLobby.putExtra("lobbyCode", gameCode);

            startActivity(createNewGameToNewGameLobby);
        }
    }

    public String createNewGame() {

        boolean validNickname = false;

        final String NICKNAME = nicknameEditText.getText().toString();
        final int SCORE  = 0;
        final boolean ISHOST = true;
        final String UIDHOST = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String LOBBYCODE = getGameCode();


        while (!validNickname)
            if (NICKNAME.isEmpty()) {
                Toast.makeText(this, "ERROR: Enter A Nickname!", Toast.LENGTH_SHORT).show();

            } else {

                gameRef.child("Games").child(LOBBYCODE).child("Players").child(UIDHOST).
                        setValue(new Player(UIDHOST, NICKNAME, SCORE, ISHOST));

                Toast.makeText(this, "Lobby Successfully Created!", Toast.LENGTH_SHORT).show();

                validNickname = true;
        }
            return LOBBYCODE;
    }

    protected String getGameCode() {
        final String AVAILCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        while (code.length() < 7) { // length of the random string.
            int index = (int) (rnd.nextFloat() * AVAILCHARS.length());
            code.append(AVAILCHARS.charAt(index));
        }

        String gameCode = code.toString();
        return gameCode;

    }
}
