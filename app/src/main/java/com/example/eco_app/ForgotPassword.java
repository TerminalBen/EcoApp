package com.example.eco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText email;
    private Button resetPwdBtn;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.emailEditText);
        resetPwdBtn = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();

        resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
            }
        });

    }

    public void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void resetPwd() {
        String email_string = email.getText().toString().trim();

        if(email_string.isEmpty()){
              email.setError("Email is required!");
              email.requestFocus();
              return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("Valid email is Required!");
            email.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email_string).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email for password Reset!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Error Resetting the password!", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility((View.GONE));
        goToLogin();
    }
}