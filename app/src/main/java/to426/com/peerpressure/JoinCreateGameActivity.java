package to426.com.peerpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class JoinCreateGameActivity extends AppCompatActivity implements View.OnClickListener {

    public Button newGameButton;
    public Button joinGameButton;
    public Button rulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_join_create_game);

        newGameButton = findViewById(R.id.newGameButton);
        joinGameButton = findViewById(R.id.joinGameButton);
        rulesButton = findViewById(R.id.rulesButton);

        newGameButton.setOnClickListener(this);
        joinGameButton.setOnClickListener(this);
        rulesButton.setOnClickListener(this);

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    //Info Button OnClick
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
