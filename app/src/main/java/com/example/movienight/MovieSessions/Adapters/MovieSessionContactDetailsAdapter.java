package com.example.movienight.MovieSessions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.movienight.R;
import java.util.List;
import com.example.movienight.Models.UserContact;

public class MovieSessionContactDetailsAdapter extends BaseAdapter implements ListAdapter {
    private final List<UserContact> list;
    private final Context context;

    public MovieSessionContactDetailsAdapter(List<UserContact> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = inflater.inflate(R.layout.movie_session_contact_element, null);
        }

        TextView listItemText = view.findViewById(R.id.movie_session_contact_textview);
        listItemText.setText(list.get(position).getFullname());

        return view;
    }
}