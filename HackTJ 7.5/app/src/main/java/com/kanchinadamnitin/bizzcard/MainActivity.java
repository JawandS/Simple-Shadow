package com.kanchinadamnitin.bizzcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText;
    private Button signInButton, toRegister, toForgotPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.signInEmailEditText);
        passwordEditText = findViewById(R.id.signInPasswordEditText);
        signInButton = findViewById(R.id.signInButton);
        toRegister = findViewById(R.id.toRegisterFromSignIn);
        toForgotPassword = findViewById(R.id.toForgotPasswordFromSignIn);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Neither email nor password can be an empty string", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            editor.putString("currentUserEmail", emailEditText.getText().toString());
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, AuthorizedActivity.class));
                        }
                        else if(sharedPreferences.contains(emailEditText.getText().toString()))
                            Toast.makeText(getApplicationContext(), "Wrong password. Reset your password if you have forgotten", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "User not found. Please register before logging in", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        toForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
}