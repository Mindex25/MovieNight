package com.example.movienight.Contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movienight.ContactAdapter;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.movienight.Models.User;
import com.example.movienight.Models.UserContact;

public class ContactFragment extends Fragment {
    EditText contacts_search_text;
    Button contacts_search_button;
    User requester;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts,container,false);
    }


    public void onViewCreated (@NotNull View view, Bundle savedInstaceState) {

        contacts_search_text = view.findViewById(R.id.contacts_search_text);
        contacts_search_button = getView().findViewById(R.id.contacts_search_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");

        List<User> userList = new ArrayList<>();
        List<UserContact> friendList = new ArrayList<>();


        // Display current user friendlist.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                friendList.clear();

                 for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }

                for (DataSnapshot postSnapshot2: snapshot.child(user.getUid()).child("Friends").getChildren()) {
                    UserContact friend = postSnapshot2.getValue(UserContact.class);
                    friendList.add(friend);
                }

                // Sort friend list by status
                Collections.sort(friendList, (x, y) -> Boolean.compare(x.getStatus(), y.getStatus()));

                // Instantiate adapter
                requester = snapshot.child(user.getUid()).getValue(User.class);
                ContactAdapter adapter = new ContactAdapter(friendList, getContext(), requester);

                //handle listview and assign adapter
                ListView lView = view.findViewById(R.id.contact_listview) ;
                lView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Une erreure c'est produite.", Toast.LENGTH_SHORT).show();
            }
        });

        contacts_search_button.setOnClickListener(v->{
            sendFriendRequest(userList, friendList, requester);
        });
    }

    private void sendFriendRequest(List<User> userList, List<UserContact> friendList , User requester) {
        String input = String.valueOf(contacts_search_text.getText());
        if (!input.matches("^(.+)@(.+)$")){
            Toast.makeText(getContext(), "Le format du courriel est invalide.", Toast.LENGTH_SHORT).show();
        } else {
            User receiver = User.getUserbyEmail(userList, input);
            if (receiver != null) {
                if (!user.getEmail().equals(input)) {
                    if (UserContact.contactExists(friendList, receiver.getEmail())) {
                        Toast.makeText(getContext(), "Ce contact est déjà dans votre liste d'amis.", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference DBreceiver = FirebaseDatabase.getInstance().getReference().child("Users").child(receiver.getId()).child("Friends").child(requester.getId());
                        DBreceiver.setValue(new UserContact(requester.getFullname(), requester.getEmail(), false, requester.getId()));
                        Toast.makeText(getContext(), "Demande d'ami envoyée à " + input, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Impossible d'envoyer une demande d'ami à soi-même.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Cet utilisateur n'existe pas.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
