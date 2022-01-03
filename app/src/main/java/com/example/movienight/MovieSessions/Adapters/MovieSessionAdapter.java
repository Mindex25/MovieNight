package com.example.movienight.MovieSessions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.movienight.MovieSessions.MovieSessionDetail;
import com.example.movienight.R;

import java.util.List;

public class MovieSessionAdapter extends BaseAdapter implements ListAdapter {

    private final List<String> list;
    private final Context context;

    public MovieSessionAdapter(List<String> list, Context context) {
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
            view = inflater.inflate(R.layout.movie_session_element, null);
        }

        TextView listItemText = view.findViewById(R.id.group_item_string);
        listItemText.setText(list.get(position));

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieSessionDetail.class);
                intent.putExtra("group_title", list.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }
}