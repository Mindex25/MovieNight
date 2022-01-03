package com.example.movienight.Authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movienight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.movienight.Models.User;


public class Signup extends AppCompatActivity {
    EditText sinscrire_nom, sinscrire_courriel, sinscrire_mdp, sinscrire_mdp_conf;
    Button sinscrire_button;
    TextView sinscrire_deja_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sinscrire_nom=findViewById(R.id.sinscrire_nom);
        sinscrire_courriel=findViewById(R.id.login_courriel);
        sinscrire_mdp=findViewById(R.id.sinscrire_mdp);
        sinscrire_mdp_conf= findViewById(R.id.sinscrire_mdp_conf);
        sinscrire_button = findViewById(R.id.sinscrire_button);
        sinscrire_deja_text = findViewById(R.id.sinscrire_deja_text);

        sinscrire_deja_text.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        sinscrire_button.setOnClickListener(v -> {
            boolean allChecks = true;
            String fullname, password, password2, email;
            fullname = String.valueOf(sinscrire_nom.getText());
            password = String.valueOf(sinscrire_mdp.getText());
            password2 = String.valueOf(sinscrire_mdp_conf.getText());
            email = String.valueOf(sinscrire_courriel.getText());


            if(!(fullname.matches("^[a-zA-Z ]*$") && fullname.length()!=0)){
                Toast.makeText(getApplicationContext(), "Le nom peut être composé seulement de lettres.", Toast.LENGTH_SHORT).show();
                allChecks=false;
            }
            if(!(password.length() >= 8)){
                allChecks=false;
                Toast.makeText(getApplicationContext(), "Le mot de passe doit avoir au moins 8 caractères.", Toast.LENGTH_SHORT).show();
            }
            if(!password.equals(password2)){
                allChecks=false;
                Toast.makeText(getApplicationContext(), "La confirmation du mot de passe a échouée.", Toast.LENGTH_SHORT).show();
            }
            if(!(email.length()!=0 && email.matches("^(.+)@(.+)$"))){
                allChecks=false;
                Toast.makeText(getApplicationContext(), "Le format du courriel est invalide.", Toast.LENGTH_SHORT).show();
            }
            if (allChecks){
                signup(fullname,password,email);
            }
        });
    }

    private void signup(String fullname, String password, String email){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Signup success", "createUserWithEmail:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            User.addUser(mAuth.getCurrentUser().getUid(),fullname,email);
                            updateUI(user);
                        } else {
                            Log.w("Signup fail", "createUserWithEmail:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(Signup.this, "Inscription échouée.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), Logged.class);
        startActivity(intent);
        finish();
    }
}