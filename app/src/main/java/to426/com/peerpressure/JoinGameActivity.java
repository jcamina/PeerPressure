package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        enterButton = (Button) findViewById(R.id.enterButton);

        enterButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent joinGameToJoinGameLobby = new Intent(JoinGameActivity.this, JoinGameLobbyActivity.class);


        if (v == enterButton){
            startActivity(joinGameToJoinGameLobby);
        }
    }
}
