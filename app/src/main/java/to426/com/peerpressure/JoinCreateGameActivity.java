package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinCreateGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Button newGameButton;
    public Button joinGameButton;
    public Button rulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_create_game);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        newGameButton = findViewById(R.id.newGameButton);
        joinGameButton = findViewById(R.id.joinGameButton);
        rulesButton = findViewById(R.id.rulesButton);

        newGameButton.setOnClickListener(this);
        joinGameButton.setOnClickListener(this);
        rulesButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent joinCreateToCreateNewGame = new Intent(JoinCreateGameActivity.this, CreateNewGameActivity.class);
        Intent joinCreateToJoinGame = new Intent(JoinCreateGameActivity.this, JoinGameActivity.class);
        Intent joinCreateToRules = new Intent(JoinCreateGameActivity.this, RulesActivity.class);


        if (v == newGameButton){

            newGameButton.setEnabled(false);
            joinGameButton.setEnabled(false);

            startActivity(joinCreateToCreateNewGame);
            finish();

        }
        else if (v == joinGameButton){

            newGameButton.setEnabled(false);
            joinGameButton.setEnabled(false);

            startActivity(joinCreateToJoinGame);
            finish();

        }
        else if (v == rulesButton){
            newGameButton.setEnabled(false);
            joinGameButton.setEnabled(false);

            startActivity(joinCreateToRules);
            finish();

        }

    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }
}
