package com.isa.sch.ly;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signup extends AppCompatActivity
{

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPsw;
    private Button btnSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Initializing variables
        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edt_email);
        edtPsw = findViewById(R.id.edt_psw);
        edtName = findViewById(R.id.edt_name);
        btnSignUp = findViewById(R.id.btn_signup);


        //Sign up
        btnSignUp.setOnClickListener((View v) -> {

            String email = edtEmail.getText().toString();
            String psw = edtPsw.getText().toString();
            String username = edtName.getText().toString();

            signUp(username, email, psw);
        });
    }


    //Signup function.
    private void signUp(String username, String email, String password)
    {
        //Invalidation checker.
        if (email.isEmpty() || password.isEmpty() || username.isEmpty())
        {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Actual signup
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                FirebaseDatabase.getInstance().getReference("User/" +
                        FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(username, email, password, ""));

                //jump to login page after signing up
                startActivity(new Intent(Signup.this, Login.class));
            }
            else
            {
                // If sign in fails, display a message to the user.
                Toast.makeText(Signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}