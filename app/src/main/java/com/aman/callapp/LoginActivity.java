package com.aman.callapp;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    String email;
    String password;

    DbHelper myDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInputLayout = findViewById(R.id.emailTextField);
        passwordInputLayout = findViewById(R.id.passwordTextField);

        myDb = new DbHelper(getApplicationContext());
    }

    public void login(View view) {
        MainActivity.isLoggedIn = true;


        email = emailInputLayout.getEditText().getText().toString();
        password = passwordInputLayout.getEditText().getText().toString();


        Cursor cursor = myDb.checkEmailAndPassword(email, password);

        Log.i("PEEP", String.format(
                "%s %s",
                email, password
        ));

        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
            MainActivity.isLoggedIn = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();

    }

    public void gotoSignupPage(View view) {

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}