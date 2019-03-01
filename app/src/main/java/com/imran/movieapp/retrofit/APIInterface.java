package com.imran.movieapp.retrofit;


import com.imran.movieapp.model.Movie;
import com.imran.movieapp.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //Highest Rated
    @GET("movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page

    );

    @GET("search/movie")
    Call<MovieListResponse> getSearchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page

    );

    //Response to get movie details to display on Details UI
    //@Path sets value for {args}
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}
