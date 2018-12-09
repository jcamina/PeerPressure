package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public Button loginButton;
    public Button registerButton;
    public EditText userNameEditText;
    public EditText passwordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transition Change
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //Login Structure
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("hi", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("hi", "onAuthStateChanged:signed_out");
                }
            }
        };

        //Set The Tool Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

        //Nav Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent loginToWelcome = new Intent(LoginActivity.this, WelcomeActivity.class);
                LoginActivity.this.startActivity(loginToWelcome);
                finish();
            }
        });

        //Reassigns the Green Check Key in Keyboard!
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        String email = userNameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();


                        if (email.isEmpty() || password.isEmpty()) {
                            userNameEditText.startAnimation(shakeError());//shake animation ;)
                            passwordEditText.startAnimation(shakeError()); //shake animation ;)

                            userNameEditText.setText("");
                            passwordEditText.setText("");


                            Toast.makeText(LoginActivity.this, "Email / Password Left Blank!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            signIn(email, password);

                            //Disables Multiple Button Presses
                            loginButton.setEnabled(false);

                        }

                        return true;
                    }
                return false;
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
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void registerUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Account Has Been Successfully Created!",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            userNameEditText.startAnimation(shakeError());//shake animation
                            passwordEditText.startAnimation(shakeError()); //shake animation

                            userNameEditText.setText("");
                            passwordEditText.setText("");
                            registerButton.setEnabled(true);

                        }

                    }
                });
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("hi", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            //Moves To Next Activity if Login Successful
                            Intent loginToJoinCreate = new Intent(LoginActivity.this, JoinCreateGameActivity.class);
                            LoginActivity.this.startActivity(loginToJoinCreate);
                            finish();

                        } else {
                            Log.w("hi", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();


                            userNameEditText.startAnimation(shakeError());//shake animation ;)
                            passwordEditText.startAnimation(shakeError()); //shake animation ;)

                            userNameEditText.setText("");
                            passwordEditText.setText("");

                            loginButton.setEnabled(true);

                        }
                    }
                });
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(300);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }


    @Override
    public void onClick(View v) {

        String email = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (v == registerButton) {

            if (email.isEmpty() || password.isEmpty()) {
                userNameEditText.startAnimation(shakeError());//shake animation ;)
                passwordEditText.startAnimation(shakeError()); //shake animation ;)

                userNameEditText.setText("");
                passwordEditText.setText("");

                Toast.makeText(LoginActivity.this, "Email / Password Left Blank!",
                        Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password);

                //Disables Multiple Button Presses
                registerButton.setEnabled(false);


            }


        } else if (v == loginButton) {

            if (email.isEmpty() || password.isEmpty()) {
                userNameEditText.startAnimation(shakeError());//shake animation ;)
                passwordEditText.startAnimation(shakeError()); //shake animation ;)

                userNameEditText.setText("");
                passwordEditText.setText("");


                Toast.makeText(LoginActivity.this, "Email / Password Left Blank!",
                        Toast.LENGTH_SHORT).show();
            } else {
                signIn(email, password);

                //Disables Multiple Button Presses
                loginButton.setEnabled(false);

            }


        }
    }
}
