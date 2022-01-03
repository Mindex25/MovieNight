package com.example.movienight.Prenium;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movienight.Models.User;
import com.example.movienight.R;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SubscriptionDetail extends AppCompatActivity {
    User user;
    ImageView backButton;
    TextView subscribtionText;
    Button subscribeButton;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        backButton = findViewById(R.id.back_icon_subscribe);
        subscribtionText = findViewById(R.id.subscribed_text);
        subscribeButton = findViewById(R.id.subscribe_button);

        // Get current user info
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = User.USER_REF.child(auth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.isSubscribed()) {
                    subscribtionText.setText("Movie Night Prenium");
                    subscribeButton.setText("Se désabonner");
                } else {
                    subscribeButton.setText("S'abonner");
                    subscribtionText.setText("Movie Night Standart");
                    User.updateSubscription(user.getId(),false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Une erreur est survenue.", Toast.LENGTH_SHORT).show();
            }
        });

        subscribeButton.setOnClickListener(s -> {
            if (user.isSubscribed()) {
                User.updateSubscription(user.getId(),false);
            } else {
                User.updateSubscription(user.getId(),true);
            }
        });

        backButton.setOnClickListener(b -> {
            //adView.destroy();
            finish();
        });
    }
}