package com.isa.sch.ly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity
{

    private EditText edtEmail;
    private EditText edtPsw;
    private Button btnLogin;
    private Button btnSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Initializing variables
         mAuth = FirebaseAuth.getInstance();

         edtEmail = findViewById(R.id.edt_email);
         edtPsw = findViewById(R.id.edt_psw);
         btnLogin = findViewById(R.id.btn_login);
         btnSignUp = findViewById(R.id.btn_signup);

        //Checking if the user is currently logged in
        //and send him to main activity.
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        //Sign up button
        btnSignUp.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
        });

        //login button
        btnLogin.setOnClickListener((View v) ->
        {
            String email = edtEmail.getText().toString();
            String psw = edtPsw.getText().toString();

            login(email, psw);
        });
    }

    private void login(String email, String psw)
    {
        //login method...
        //Invalidation check!
        if (edtEmail.getText().toString().isEmpty() || edtPsw.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                // Sign in success, update UI with the signed-in user's information
                startActivity(new Intent(Login.this, MainActivity.class));

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(Login.this, "Username or password is not correct!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}