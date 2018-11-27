package to426.com.peerpressure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.pplogo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b0000")));

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

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
                            Toast.makeText(LoginActivity.this, "Account Successfully Created",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            userNameEditText.startAnimation(shakeError());//shake animation
                            passwordEditText.startAnimation(shakeError()); //shake animation

                            userNameEditText.setText("");
                            passwordEditText.setText("");
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
                            Toast.makeText(LoginActivity.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            Intent loginToJoinCreate = new Intent(LoginActivity.this, JoinCreateGameActivity.class);

                            LoginActivity.this.startActivity(loginToJoinCreate);
                        } else {
                            Log.w("hi", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();


                            userNameEditText.startAnimation(shakeError());//shake animation ;)
                            passwordEditText.startAnimation(shakeError()); //shake animation ;)

                            userNameEditText.setText("");
                            passwordEditText.setText("");
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

                Toast.makeText(LoginActivity.this, "Login / Password Left Blank!",
                        Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password);
            }

        } else if (v == loginButton) {

            if (email.isEmpty() || password.isEmpty()) {
                userNameEditText.startAnimation(shakeError());//shake animation ;)
                passwordEditText.startAnimation(shakeError()); //shake animation ;)

                userNameEditText.setText("");
                passwordEditText.setText("");

                Toast.makeText(LoginActivity.this, "Login / Password Left Blank!",
                        Toast.LENGTH_SHORT).show();
            } else {
                signIn(email, password);
            }
        }
    }
}
