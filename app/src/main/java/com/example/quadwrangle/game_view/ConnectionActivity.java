package com.example.quadwrangle.game_view;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quadwrangle.R;
import com.example.quadwrangle.game_model.database.User;
import com.example.quadwrangle.game_view_model.UserDbLeaderboardConnector;

import java.util.ArrayList;
import java.util.Locale;

public class ConnectionActivity extends AppCompatActivity {

    private ImageView logInButton;
    private ImageView signUpButton;
    private ImageView chooseLogInButton;
    private ImageView chooseSignUpButton;
    private ImageView backButton;
    private TextView usernameText;
    private TextView passwordText;
    private UserDbLeaderboardConnector userDbLeaderboardConnector;
    private String myUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        userDbLeaderboardConnector = new UserDbLeaderboardConnector(this);

        chooseLogInButton = findViewById(R.id.choose_login_button);
        chooseSignUpButton = findViewById(R.id.choose_signup_button);
        logInButton = findViewById(R.id.LogInButton);
        signUpButton = findViewById(R.id.SigninButton);
        backButton = findViewById(R.id.back_button);
        usernameText = findViewById(R.id.UsernameText);
        passwordText = findViewById(R.id.PasswordText);
    }

    public void openLogIn(View view) {
        // remove choosing buttons
        chooseLogInButton.setVisibility(View.GONE);
        chooseSignUpButton.setVisibility(View.GONE);
        // open texts and back button
        openInsert();
        // open log in button
        logInButton.setVisibility(View.VISIBLE);
    }

    public void openSignIn(View view) {
        // remove choosing buttons
        chooseLogInButton.setVisibility(View.GONE);
        chooseSignUpButton.setVisibility(View.GONE);
        // open texts and back button
        openInsert();
        // open log in button
        signUpButton.setVisibility(View.VISIBLE);
    }

    public void backButton(View view) {
        // remove text content
        usernameText.setText("");
        passwordText.setText("");
        // remove texts and back button
        usernameText.setVisibility(View.GONE);
        passwordText.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        // remove login/sign up buttons
        logInButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        // return the choosing buttons
        chooseLogInButton.setVisibility(View.VISIBLE);
        chooseSignUpButton.setVisibility(View.VISIBLE);
    }

    public void openInsert() {
        usernameText.setVisibility(View.VISIBLE);
        passwordText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void LogIn(View view) {
        String user = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        myUsername = userDbLeaderboardConnector.logIn(user, password);
        //
        if (myUsername == null) {
            // toast wrong user or password
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Welcome Back " + myUsername + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AnimationActivity.class);
            startActivity(intent);
        }

    }

    public void SignUp(View view) {
        // todo: check if a password is okay
        // todo: make it to lowercase when checking
        String user = usernameText.getText().toString().toLowerCase(Locale.ROOT);
        if (userDbLeaderboardConnector.doesAlreadyExist(user.toLowerCase())) {
            Toast.makeText(this, "Username Already Taken", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = passwordText.getText().toString().toLowerCase(Locale.ROOT);
        myUsername = userDbLeaderboardConnector.signUp(user, password);
        if (myUsername == null) {
            // toast wrong user or password
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Welcome " + myUsername + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AnimationActivity.class);
            startActivity(intent);
        }
    }
}