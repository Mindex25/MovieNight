package com.example.movienight;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import com.example.movienight.Models.User;
import com.example.movienight.Models.UserContact;

public class ContactAdapter extends BaseAdapter implements ListAdapter {
    private final List<UserContact> list;
    private final Context context;
    private final User currentUser;

    public ContactAdapter(List<UserContact> list, Context context, User currentUser) {
        this.list = list;
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_element, null);
        }

        // Handle TextView and display string from the list
        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getFullname());

        // Handle buttons and add onClickListeners
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton addBtn = view.findViewById(R.id.add_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (list.get(position).getStatus()) {
            addBtn.setVisibility(View.INVISIBLE);
        }

        deleteBtn.setOnClickListener(v -> {

            String friendRequestId = list.get(position).getId();
            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Friends");
            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference().child("Users").child(list.get(position).getId()).child("Friends");

            new AlertDialog.Builder(context)
                    .setTitle("Supprimer")
                    .setMessage("Êtes-vous certain(e) de vouloir supprimer ce contact?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!list.get(position).getStatus()) {
                                friendsRef.child(friendRequestId).removeValue();
                            } else {
                                friendsRef.child(friendRequestId).removeValue();
                                senderRef.child(currentUser.getId()).removeValue();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            notifyDataSetChanged();
        });

        addBtn.setOnClickListener(v -> {
            // User that sent the request
            String id = list.get(position).getId();
            list.get(position).setStatus(true);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Friends").child(id).child("status");
            ref.setValue(true);

            // Add current user to the sender's friendlist
            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(list.get(position).getId()).child("Friends").child(currentUser.getId());
            ref2.setValue(new UserContact(currentUser.getFullname(), currentUser.getEmail(), true, currentUser.getId()));
        });

        return view;
    }
}