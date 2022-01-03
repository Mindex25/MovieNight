package com.example.movienight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movienight.Models.User;
import com.example.movienight.Prenium.Ad;
import com.example.movienight.Prenium.SubscriptionDetail;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    User user;
    Button subscriptionDetailButton;
    AdView adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        subscriptionDetailButton = view.findViewById(R.id.consulter_button);

        adView = view.findViewById(R.id.adView);

        // Get current user info
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = User.USER_REF.child(auth.getCurrentUser().getUid());

        // Display banner ad
        Ad.displayBannerAd(adView);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.isSubscribed()) {
                    adView.setVisibility(View.INVISIBLE);
                } else {
                  adView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Une erreur est survenue.", Toast.LENGTH_SHORT).show();
            }
        });

        subscriptionDetailButton.setOnClickListener(s-> {
            Intent intent = new Intent(getContext(), SubscriptionDetail.class);
            startActivity(intent);
        });

        return view;
    }
}
