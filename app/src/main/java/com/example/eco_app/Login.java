package com.example.eco_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Login extends AppCompatActivity {
    Button logged;
    ImageButton gback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        gback = (ImageButton) findViewById(R.id.back);
        logged = (Button) findViewById(R.id.blogin);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, EcoApp.class);
        startActivity(intent);
    }

    public void goToApp(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}