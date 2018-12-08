package to426.com.peerpressure;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    public Button beginButton;
    public TextView peerPressureTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));


        /*
        TextView peerPressureTitleTextView = (TextView) findViewById(R.id.peerPressureTitleTextView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "/fonts/Adventure.otf");
        peerPressureTitleTextView.setTypeface(typeface);

        */

        beginButton = (Button) findViewById(R.id.beginButton);

        beginButton.setOnClickListener(this);


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
