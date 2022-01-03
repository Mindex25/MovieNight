package com.example.movienight.MovieSessions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.movienight.API.ApiInterface;
import com.example.movienight.API.MovieResults;
import com.example.movienight.MovieSessions.Adapters.MovieSessionMovieAdapter;
import com.example.movienight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SwipingPage extends Activity {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static int PAGE = 1;
    public static String API_KEY = "3e9fd079a654b8477d322a695f1bbdd9";
    public static String LANGUAGE = "en-US";
    public static String CATEGORY = "popular";

    List<MovieResults.ResultsBean> listOfMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping_page);

        // Retrofit object with the URL and parse the result from Json to Gson
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Bundle bundle = getIntent().getExtras();
        String groupTitle = bundle.getString("groupTitle");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ApiInterface myInterface = retrofit.create(ApiInterface.class);
        Call<MovieResults> call = myInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);
        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupTitle).child(user.getUid());

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                listOfMovies = results.getResults();

                MovieSessionMovieAdapter movieAdapter = new MovieSessionMovieAdapter(getApplicationContext(), listOfMovies);
                flingContainer.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();

                flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        listOfMovies.remove(0);
                        movieAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLeftCardExit(Object dataObject) {}

                    @Override
                    public void onRightCardExit(Object dataObject) {
                        String movieTitle = ((MovieResults.ResultsBean)dataObject).getTitle();
                        String movieId = String.valueOf(((MovieResults.ResultsBean)dataObject).getId());
                        groupRef.child("Liked").child(movieId).setValue(movieTitle);
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int i) {
                        movieAdapter.notifyDataSetChanged();
                        if (i == 0) {
                            groupRef.child("flag").setValue("True");
                            Intent intent = new Intent(getApplicationContext(), MovieSessionDetail.class);
                            intent.putExtra("group_title", groupTitle);
                            startActivity(intent);
                            makeToast(getApplicationContext(),"Session terminée");
                            finish();
                        }
                    }
                    @Override
                    public void onScroll(float v) {}
                });
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
}