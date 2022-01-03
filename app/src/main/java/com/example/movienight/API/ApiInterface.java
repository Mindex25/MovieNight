package com.example.movienight.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //Request method designator and corresponds to the similarly named HTTP method
    @GET("/3/movie/{category}")
    Call<MovieResults> getMovies
    (
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    Call<MovieResults> listOfMovies(String category, String apiKey, String language, int page);

    //To get an image
    @GET("https://image.tmdb.org/t/p/w500")
    Call<MovieResults> getImage
    (
            @Path("image") String imagePath
    );
    Call<MovieResults> movieImage(String imagePath);
}