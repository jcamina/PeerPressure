package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FFinalDareLoadingActivity extends AppCompatActivity {

    String lobbyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffinal_dare_loading);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        Intent retrieveCode = getIntent();
        Bundle bundle = retrieveCode.getExtras();

        if (bundle != null) {
            lobbyCode = (String) bundle.get("lobbyCode");

        }
        new CountDownTimer(4000, 1000) {
            public void onFinish() {

                Intent FFinalDareLoadingToFLobbDareHold = new Intent(FFinalDareLoadingActivity.this, FLobbyDareHoldActivity.class);
                FFinalDareLoadingToFLobbDareHold.putExtra("lobbyCode", lobbyCode);
                startActivity(FFinalDareLoadingToFLobbDareHold);
                finish();

            }

            public void onTick(long millisUntilFinished) {

            }

        }.start();
    }
}
