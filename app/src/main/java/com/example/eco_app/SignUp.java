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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button signup,login;
    private FirebaseAuth mAuth;

    private EditText userName, email,password;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = (Button)findViewById(R.id.loginbutton);
        login.setOnClickListener(this);

        userName = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);


        signup = (Button) findViewById(R.id.bsignup);
        signup.setOnClickListener((View.OnClickListener) this);

        mAuth = FirebaseAuth.getInstance();
    }

    public void goToApp(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void registerUser(){
        String email_string = email.getText().toString().trim();
        String userName_string = userName.getText().toString().trim();
        String password_string = password.getText().toString().trim();

        if (userName_string.isEmpty()){
            userName.setError("Name is required!");
            userName.requestFocus();
            return;
        }

        if (email_string.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()){
            email.setError("Valid email is Required!");
            email.requestFocus();
            return;
        }


        if (password_string.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if(password_string.length()<6){
            password.setError("Min Password Error should be 6 characters!");
            password.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_string,password_string)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(userName_string,email_string);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                    .getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progressbar.setVisibility(View.GONE);
                                                //redirect to login layout
                                                login();
                                            }
                                            else{
                                                Toast.makeText(SignUp.this, "Failed to Register. Try Again!", Toast.LENGTH_LONG).show();
                                                progressbar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(SignUp.this, "Failed to Register. Try Again!", Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bsignup:
                registerUser();
                break;
            case R.id.loginbutton:
                login();
                break;
        }
    }


    /*
    public void goback(View view) {
        Intent intent = new Intent(this, EcoApp.class);
        startActivity(intent);
    }

     */



}