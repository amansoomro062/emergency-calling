package com.aman.callapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    public final String TAG = "PEEP";
    TextInputLayout firstNameInputLayout;
    TextInputLayout lastNameInputLayout;
    TextInputLayout ageInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    String firstName;
    String lastName;
    int age;
    String email;
    String password;

    DbHelper myDb;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstNameInputLayout = findViewById(R.id.firstNameTextField);
        lastNameInputLayout = findViewById(R.id.lastNameTextField);
        ageInputLayout = findViewById(R.id.ageTextField);
        emailInputLayout = findViewById(R.id.emailTextField);
        passwordInputLayout = findViewById(R.id.passwordTextField);

        myDb = new DbHelper(getApplicationContext());


    }

    public void signup(View view) {

        firstName = firstNameInputLayout.getEditText().getText().toString();
        lastName = lastNameInputLayout.getEditText().getText().toString();
        age = Integer.parseInt(ageInputLayout.getEditText().getText().toString());
        email = emailInputLayout.getEditText().getText().toString();
        password = passwordInputLayout.getEditText().getText().toString();


        Log.i("PEEP", String.format(
                "%s %s %s %s %s",
                firstName, lastName, String.valueOf(age), email, password
        ));

        boolean isInserted = myDb.insertData(firstName, lastName, age, email, password);
        if(isInserted) {

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            int users = MainActivity.sharedPreferences.getInt("users",0);
            MainActivity.sharedPreferences.edit().putInt("users",users+1).apply();
            Log.i(TAG, "signup: users in total "+users+1);
            gotoLogin();

        } else {
            Toast.makeText(this, "There was problem creating account!", Toast.LENGTH_SHORT).show();

        }
    }

    public void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void gotoLoginPage(View view) {
        gotoLogin();
    }

}
