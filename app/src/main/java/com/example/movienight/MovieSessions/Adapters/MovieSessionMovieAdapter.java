package com.example.movienight.MovieSessions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movienight.API.MovieResults;
import com.example.movienight.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieSessionMovieAdapter extends BaseAdapter {
    private final Context context;
    private final List<MovieResults.ResultsBean> movieList;

    public MovieSessionMovieAdapter(Context context, List<MovieResults.ResultsBean> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int pos) {
        return movieList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, parent, false);
        }

        TextView movieTitle = view.findViewById(R.id.film_title_text);
        ImageView moviePoster = view.findViewById(R.id.film_image_imageview);
        String releaseDate = movieList.get(position).getRelease_date().split("-")[0];
        movieTitle.setText(movieList.get(position).getTitle() + ", " + releaseDate);
        TextView overview = view.findViewById(R.id.overview_movie_text);
        overview.setText(movieList.get(position).getOverview());
        TextView voteAverage = view.findViewById(R.id.movie_vote_average);
        voteAverage.setText( movieList.get(position).getVote_average() + "/10");
        String posterUrl =  "https://image.tmdb.org/t/p/w500" + movieList.get(position).getPoster_path();
        loadImageFromUrl(posterUrl, moviePoster);

        return view;
    }

    private void loadImageFromUrl(String url, ImageView imageView){
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
    }
}