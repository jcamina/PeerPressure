package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    TextView rulesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_rules);

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rulesTextView = findViewById(R.id.textRules);

        String rules = "1. Login and begin a new game as the host or join an existing game with a unique lobby code that the host will give you. Host will select the ‘mode’ of the game: friends mode (the most extreme desperate dares), work mode, family mode (least extreme desperate dares). \n\n”" +
                "2. Once the game begins, each player will input an exciting and funny dare that they want to see done by the other players. (WARNING: PLEASE CHOOSE TO BE SAFE WITH YOUR DARES. By playing, you agree that PeerPressure is NOT responsible for any harm or injury incurred while playing. The game is only intended to be fun and humorous, not dangerous).\n\n" +
                "3. If you cannot come up with a dare within in the time limit, select ‘Desperate Dare.’\n\n" +
                "4. When everyone is done inputting their dares, the game will proceed and two random dares will be selected from the entire pool of dares. These two selected dares will battle against each other.\n\n" +
                "5. Users whose dares were not selected for the battle will now have the option to vote on the dare that they would like to see be done! The users whose dares are randomly chosen will not vote and will wait to see the results.\n\n" +
                "6. After everyone is done voting, the dare that gets more votes must be completed by the user whose dare got fewer votes for that individual round.\n\n" +
                "7. Once the player completes the dare, all users will vote on how well they thought the player executed the dare.\n\n" +
                "8. Steps 3-7 will then repeat themselves each time for a maximum of 2 rounds.\n\n" +
                "9. After 2 total rounds are complete, the game will proceed to the final dare round.\n\n" +
                "10. All players will then decide on a final dare together without knowing which two players are currently in last place. The host will be the only person who can input the chosen final dare on their device.\n\n" +
                "11. The game will proceed to reveal the players in last and second to last place. Those players will both complete the final dare.\n\n" +
                "13. The final results will be revealed as the game concludes.";

    rulesTextView.setText(rules);
    rulesTextView.setTextColor(Color.BLACK);
    rulesTextView.setTypeface(null, Typeface.BOLD);
    rulesTextView.setTextSize(20);
    rulesTextView.setMovementMethod(new ScrollingMovementMethod());

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    //Disable Back Button
    @Override
    public void onBackPressed() {
    }
}
