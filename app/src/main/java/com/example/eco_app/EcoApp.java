package com.example.eco_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EcoApp extends AppCompatActivity {
    Button log, sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccoapp);

        log = (Button) findViewById(R.id.blogin);
        sign = (Button) findViewById(R.id.bsignup);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}