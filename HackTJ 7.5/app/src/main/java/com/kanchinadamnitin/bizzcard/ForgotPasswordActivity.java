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
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText;
    private Button changePassword, toSignIn, toRegister;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.forgotPasswordEmailEditText);
        passwordEditText = findViewById(R.id.forgotPasswordPasswordEditText);
        changePassword = findViewById(R.id.changePasswordButton);
        toSignIn = findViewById(R.id.toSignInFromForgotPassword);
        toRegister = findViewById(R.id.toRegisterFromForgotPassword);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Neither email nor password can be an empty string", Toast.LENGTH_SHORT).show();
                else if (sharedPreferences.contains(emailEditText.getText().toString())) {
                    auth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Password reset email has been sent successfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "Password reset email failed to send", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "User with email does not exist. Please register first", Toast.LENGTH_SHORT).show();
            }
        });

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
            }
        });
    }
}