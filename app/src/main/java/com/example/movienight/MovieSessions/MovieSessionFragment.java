package com.example.movienight.MovieSessions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movienight.MovieSessions.Adapters.MovieSessionAdapter;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.example.movienight.Models.User;

public class MovieSessionFragment extends Fragment {
    Button createMovieSessionButton;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_session,container,false);
        createMovieSessionButton = view.findViewById(R.id.session_create_button);

        createMovieSessionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MovieSessionCreate.class);
            startActivity(intent);
        });

        return  view;
    }

    public void onViewCreated (@NotNull View view, Bundle savedInstaceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> groupList = new ArrayList<>();

        User.USER_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot postSnapshot: snapshot.child(user.getUid()).child("Groups").getChildren()) {
                    String groupName = postSnapshot.getValue(String.class);
                    groupList.add(groupName);
                }

                MovieSessionAdapter adapter = new MovieSessionAdapter(groupList, getContext());
                ListView lView = view.findViewById(R.id.session_listview) ;
                lView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Une erreur c'est produite.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
