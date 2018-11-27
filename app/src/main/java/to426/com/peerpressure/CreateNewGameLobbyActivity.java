package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateNewGameLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startGameButton;
    public TextView lobbyCodeTextView;
    public String lobbyCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_game_lobby);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        startGameButton = (Button) findViewById(R.id.startGameButton);
        lobbyCodeTextView = (TextView) findViewById(R.id.lobbyCode);

        startGameButton.setOnClickListener(this);

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if(bundle != null)
        {
            lobbyCode = (String) bundle.get("lobbyCode");
            lobbyCodeTextView.setText(lobbyCode);
        }



    }

    @Override
    public void onClick(View v) {


        if (v == startGameButton){
        }

    }
}
