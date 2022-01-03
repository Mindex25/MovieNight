package com.example.movienight.MovieSessions;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movienight.Models.User;
import com.example.movienight.Models.UserContact;
import com.example.movienight.MovieSessions.Adapters.MovieSessionContactAdapter;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieSessionCreate extends AppCompatActivity {

    ImageButton createBtn;
    User user;
    EditText session_title_edittext;
    private List<UserContact> checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_session_create);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener(b -> {
            finish();
        });

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        List<UserContact> friendList = new ArrayList<>();

        // Display current user friendlist.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();

                for (DataSnapshot postSnapshot2: snapshot.child(currentUser.getUid()).child("Friends").getChildren()) {
                    UserContact friend = postSnapshot2.getValue(UserContact.class);
                    friendList.add(friend);
                }

                user = snapshot.child(currentUser.getUid()).getValue(User.class);
                MovieSessionContactAdapter adapter = new MovieSessionContactAdapter(friendList, getApplicationContext(), user);
                checked = adapter.getChecked();

                ListView lView = findViewById(R.id.invite_contact_listview) ;
                lView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite.", Toast.LENGTH_SHORT).show();
            }
        });

        session_title_edittext = findViewById(R.id.session_title_edittext);
        createBtn = findViewById(R.id.create_session_button);

        createBtn.setOnClickListener(s -> {
            if(String.valueOf(session_title_edittext.getText()).length()==0){
                Toast.makeText(getApplicationContext(), "Le nom du groupe ne peut être laissé vide.", Toast.LENGTH_SHORT).show();

            } else if (String.valueOf(session_title_edittext.getText()).length()>10){
                Toast.makeText(getApplicationContext(), "Le nom du groupe ne peut dépasser 10 caractères.", Toast.LENGTH_SHORT).show();
            } else {
                String nomGroupe = String.valueOf(session_title_edittext.getText());
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("Groups").child(nomGroupe).child(user.getId()).setValue(new UserContact(user.getFullname(),user.getEmail(), false, user.getId()));
                db.child("Users").child(user.getId()).child("Groups").child(nomGroupe).setValue(nomGroupe);

                for (int i = 0; i < checked.size(); i++){
                    db.child("Groups").child(nomGroupe).child(checked.get(i).getId()).setValue(checked.get(i));
                    db.child("Users").child(checked.get(i).getId()).child("Groups").child(nomGroupe).setValue(nomGroupe);
                }
                Toast.makeText(getApplicationContext(), "Groupe créé.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}