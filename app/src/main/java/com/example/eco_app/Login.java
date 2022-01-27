package com.example.eco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity implements View.OnClickListener {
    //Button logged;
    ImageButton gBackbtn;

    private EditText email,password;
    private Button login_btn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        gBackbtn = findViewById(R.id.back);
        gBackbtn.setOnClickListener(this);

        login_btn = findViewById(R.id.blogin);
        login_btn.setOnClickListener(this);

        email =findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressBar=findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void goBack() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void goToApp() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                goBack();

            case R.id.blogin:
                login();
                break;

            case R.id.forgotPassword:
                //create method to reset the password
                //pwReset();
        }
    }

    private void login() {
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        if (emailString.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            email.setError("Valid Email is required!");
            email.requestFocus();
            return;
        }

        if(passwordString.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if(passwordString.length()<6){
            password.setError("Password min. Length should be 6 characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    goToApp();
                }
                else{
                    Toast.makeText(Login.this, "Failed to login! Please check your Credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}