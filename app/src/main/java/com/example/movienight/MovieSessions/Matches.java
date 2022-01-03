package com.example.movienight.MovieSessions;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movienight.MovieSessions.Adapters.MatchesAdapter;
import com.example.movienight.MovieSessions.Adapters.MovieSessionContactDetailsAdapter;
import com.example.movienight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Matches extends AppCompatActivity {

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        backButton = findViewById(R.id.back_icon_match);

        List<String> movieTotal = new ArrayList<>();
        List<String> movieUnique = new ArrayList<>();
        List<String> matches = new ArrayList<>();

        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();

        String groupTitle = bundle.getString("groupTitle");
        groupRef.child("Groups").child(groupTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int nbGroupMembers = 0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    nbGroupMembers += 1;

                    for (DataSnapshot snapshot1 : postSnapshot.child("Liked").getChildren()) {
                        String title = snapshot1.getValue().toString();
                        if (!(movieTotal.contains(title))){
                            movieUnique.add(title);
                        }
                        movieTotal.add(title);
                    }
                }

                for(String m : movieUnique){
                   int count = Collections.frequency(movieTotal, m);
                   if(count == nbGroupMembers){
                       matches.add(m);
                   }
                }

                MatchesAdapter adapter = new MatchesAdapter(matches, getApplicationContext());
                ListView lView = findViewById(R.id.matches_listview);
                lView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite.", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(b -> {
            finish();
        });
    }
}