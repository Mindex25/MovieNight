package com.example.movienight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movienight.Authentification.Logged;
import com.example.movienight.Authentification.Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // User is already signed-in
                Intent intent = new Intent(getApplicationContext(), Logged.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}