package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateNewGameLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_game_lobby);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        startGameButton = (Button) findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Intent createNewGameLobbyToRoundSplash = new Intent(CreateNewGameLobbyActivity.this, RoundSplashActivity.class);


        if (v == startGameButton){
            startActivity(createNewGameLobbyToRoundSplash);
        }

    }
}
