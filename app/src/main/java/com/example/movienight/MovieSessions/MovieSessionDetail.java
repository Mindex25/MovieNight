package com.example.movienight.MovieSessions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movienight.MovieSessions.Adapters.MovieSessionContactDetailsAdapter;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.movienight.Models.UserContact;


public class MovieSessionDetail extends AppCompatActivity {

    Button swipeAccessButton;
    ImageView backButton;
    TextView groupTitleTextView;
    int status = 0;
    List<UserContact> groupMembers = new ArrayList<>();
    List<UserContact> finished = new ArrayList<>();
    List<String> names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_session_detail);

        String groupTitle = getIntent().getStringExtra("group_title");

        swipeAccessButton = findViewById(R.id.swipe_access_button);
        backButton = findViewById(R.id.back_icon_session);
        groupTitleTextView = findViewById(R.id.group_title_textview);
        groupTitleTextView.setText(groupTitle);

        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference();

        infoStorage(groupRef, groupTitle);

        swipeAccessButton.setOnClickListener(s -> {

            if (status == 1) {
                Intent intent = new Intent(getApplicationContext(), SwipingPage.class);
                intent.putExtra("groupTitle", groupTitle);
                intent.putExtra("groupMembers", (Serializable) groupMembers);
                startActivity(intent);

            } else if (status == 3) {

                Intent intent = new Intent(getApplicationContext(), Matches.class);
                intent.putExtra("groupTitle", groupTitle);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(b -> {
            finish();
        });

    }

    private void infoStorage(DatabaseReference groupRef, String groupTitle) {

        groupRef.child("Groups").child(groupTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupMembers.clear();
                names.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserContact groupMember = postSnapshot.getValue(UserContact.class);
                    groupMembers.add(groupMember);
                }

                for (UserContact userContact : groupMembers) {
                    names.add(userContact.getFullname());
                }

                MovieSessionContactDetailsAdapter adapter = new MovieSessionContactDetailsAdapter(groupMembers, getApplicationContext());
                ListView lView = findViewById(R.id.movie_session_contact_listview);
                lView.setAdapter(adapter);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (checkMe(user, snapshot)) {
                    checkAll(snapshot);
                }
                statusCheck(groupMembers, finished);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkMe(FirebaseUser user, DataSnapshot snapshot) {
        for (DataSnapshot snapshot1 : snapshot.child(user.getUid()).getChildren()) {
            if (snapshot1.getKey().equals("flag")) {
                String flag = (String) snapshot1.getValue();
                if (flag.equals("True")) {
                    return true;
                }
            }
        }
        status = 1;
        return false;
    }

    private void statusCheck(List<UserContact> groupMembers, List<UserContact> finished) {
        if (status == 1) {
            swipeAccessButton.setText("Commencer");
        } else if (groupMembers.size() == finished.size()) {
            status = 3;
            swipeAccessButton.setText("Consuler les matchs");
        } else {
            swipeAccessButton.setText("En attente");
        }
    }

    private void checkAll(DataSnapshot snapshot){
        for (UserContact uc : this.groupMembers) {
            for (DataSnapshot snapshot1 : snapshot.child(uc.getId()).getChildren()) {
                if (snapshot1.getKey().equals("flag")) {
                    String flag = (String) snapshot1.getValue();
                    if (flag.equals("True")) {
                        finished.add(uc);
                    }
                }
            }
        }
    }

}