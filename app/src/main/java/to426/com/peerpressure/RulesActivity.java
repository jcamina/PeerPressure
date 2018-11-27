package to426.com.peerpressure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    TextView rulesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        rulesTextView = (TextView) findViewById(R.id.textRules);

        String rules = "1. Login and begin a new game as the host or join an existing game with a unique lobby code that the host will give you. \n\n" +
                "2. Once the game begins, each player will input an exciting and funny dare that they want to see done by the other players. (WARNING: PLEASE CHOOSE TO BE SAFE WITH YOUR DARES. By playing, you agree that PeerPressure is NOT responsible for any harm or injury incurred while playing. The game is only intended to be fun and humorous, not dangerous)\n\n" +
                "3. When everyone is done inputting their dares, the game will proceed and two random dares will be selected from the entire pool of dares. These two selected dares will battle against each other.\n\n" +
                "4. Users whose dares were not selected for the battle will now have the option to vote on the dare that they would like to see be done! The users whose dares are randomly chosen will not vote and will wait to see the results.\n\n" +
                "5. After everyone is done voting, the dare that gets more votes must be completed by the user whose dare got fewer votes for that individual round. \n\n" +
                "6. Once the player completes the dare, all users will vote on how well they thought the player executed the dare. \n\n" +
                "7. Steps 3-6 will then repeat themselves each time for a maximum of 3 rounds. \n\n" +
                "8. After 3 total rounds are complete, the game will proceed to the final dare round.\n\n" +
                "9. All players will then decide on a final dare together without knowing who is losing the game. The user who is currently in first place will be the only person who can input the chosen final dare on their device. \n\n" +
                "10. The game will proceed to reveal the players in last and second to last place. Those players will both complete the final dare.\n\n" +
                "11. All players besides the final dare contestants will vote on who they believe did the final dare better.\n\n" +
                "12. The final results, including the biggest loser, will be revealed at the very end. ";

    rulesTextView.setText(rules);
    rulesTextView.setTextColor(Color.BLACK);
    rulesTextView.setTypeface(null, Typeface.BOLD);
    rulesTextView.setTextSize(20);
    rulesTextView.setMovementMethod(new ScrollingMovementMethod());

    }
}
