package com.example.movienight.MovieSessions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.movienight.R;

import java.util.ArrayList;
import java.util.List;

import com.example.movienight.Models.User;
import com.example.movienight.Models.UserContact;

public class MovieSessionContactAdapter extends BaseAdapter implements ListAdapter {
    private final List<UserContact> list;
    private final Context context;
    private final User currentUser;

    private List<UserContact> checked;

    public MovieSessionContactAdapter(List<UserContact> list, Context context, User currentUser) {
        this.list = list;
        this.context = context;
        this.currentUser = currentUser;
        this.checked = new ArrayList<>();
    }

    public List<UserContact> getChecked() {
        return checked;
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
            view = inflater.inflate(R.layout.movie_session_invite_element, null);
        }

        TextView listItemText = view.findViewById(R.id.list_contact_string);
        listItemText.setText(list.get(position).getFullname());

        CheckBox inviteCheck = view.findViewById(R.id.invite_checkbox);

        inviteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked.add(list.get(position));
                } else {
                    checked.remove(list.get(position));
                }
            }
        });

        return view;
    }
}