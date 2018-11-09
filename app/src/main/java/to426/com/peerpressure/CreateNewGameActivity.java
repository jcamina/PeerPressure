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

public class CreateNewGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Spinner modeSelectSpinner;
    public Button enterButton;
    public EditText nicknameEditText;

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

    }

    @Override
    public void onClick(View v) {

        Intent createNewGameToNewGameLobby = new Intent(CreateNewGameActivity.this, CreateNewGameLobbyActivity.class);


        if (v == enterButton){
            startActivity(createNewGameToNewGameLobby);
        }
    }
}
