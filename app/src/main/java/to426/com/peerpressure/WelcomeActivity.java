package to426.com.peerpressure;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    public Button beginButton;
    public TextView peerPressureTitleTextView;

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
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(WelcomeActivity.this, "Back clicked!",     Toast.LENGTH_SHORT).show();
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

        Intent welcomeToLogin = new Intent(WelcomeActivity.this, LoginActivity.class);

        if (v == beginButton) {
            beginButton.setEnabled(false);
            startActivity(welcomeToLogin);

            finish();
        }

    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }
}
