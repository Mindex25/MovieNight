package com.example.movienight.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movienight.Prenium.Ad;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.example.movienight.Models.User;

public class ProfileFragment extends Fragment {
    User user;
    Button signout_button;
    Button editprofile_button;
    boolean isSubscribed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        EditText nameT = view.findViewById(R.id.editText_name);
        EditText emailT = view.findViewById(R.id.editText_email);
        emailT.setFocusable(false);

        signout_button = view.findViewById(R.id.profil_deconnecter);
        editprofile_button = view.findViewById(R.id.profil_confirmer);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = User.USER_REF.child(auth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                nameT.setText(user.getFullname());
                emailT.setText(user.getEmail());
                if (!user.isSubscribed()) {
                    Ad.displayInterstitialAd(getContext(),getActivity());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Impossible de trouver vos informations.", Toast.LENGTH_SHORT).show();
            }
        });

        editprofile_button.setOnClickListener(x -> {
            String newName = String.valueOf(nameT.getText());

            if (!newName.equals(user.getFullname())) {
                if (newName.isEmpty())
                    Toast.makeText(getContext(), "Veuillez entrer un nom.", Toast.LENGTH_SHORT).show();
                else {
                    User.updateName(auth.getCurrentUser().getUid(), newName);
                    Toast.makeText(getContext(), "Profil sauvegardé.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signout_button.setOnClickListener(p -> {
            auth.signOut();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}
