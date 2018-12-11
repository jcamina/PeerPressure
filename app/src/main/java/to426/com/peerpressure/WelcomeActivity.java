package to426.com.peerpressure;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    public Button beginButton;
    public TextView peerPressureTitleTextView;
    public MediaPlayer mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_welcome);


        beginButton = (Button) findViewById(R.id.beginButton);

        beginButton.setOnClickListener(this);

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mm = new MediaPlayer();
        mm = MediaPlayer.create(this, R.raw.theme);
        mm.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mm.setLooping(true);
        mm.start();

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

        Intent welcomeToLogin = new Intent(WelcomeActivity.this, LoginActivity.class);

        if (v == beginButton) {
            beginButton.setEnabled(false);
            startActivity(welcomeToLogin);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_two) {

            Intent toRules = new Intent(WelcomeActivity.this, RulesActivity.class);
            WelcomeActivity.this.startActivity(toRules);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing()){
            mm.stop();
        }
    }
}
