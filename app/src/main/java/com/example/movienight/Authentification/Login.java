package com.example.movienight.Authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movienight.Prenium.Ad;
import com.example.movienight.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText login_email;
    EditText login_mdp;
    Button login_button;
    TextView login_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_email= findViewById(R.id.login_email);
        login_mdp = findViewById(R.id.login_mdp);
        login_button = findViewById(R.id.login_button);
        login_text = findViewById(R.id.login_text);

        login_text.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
            finish();
        });

        login_button.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String email, password;
        email = String.valueOf(login_email.getText());
        password = String.valueOf(login_mdp.getText());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (email.isEmpty())
            Toast.makeText(Login.this, "Veuillez entrer votre courriel.", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty())
            Toast.makeText(Login.this, "Veuillez entrer votre mot de passe.", Toast.LENGTH_SHORT).show();
        else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Login.this, Logged.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Information de connexion invalide.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}