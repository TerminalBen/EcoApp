package com.example.eco_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUp extends AppCompatActivity {
    Button singup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        singup = (Button) findViewById(R.id.bsignup);
    }

    public void goToApp(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, EcoApp.class);
        startActivity(intent);
    }
}