package com.imran.movieapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.imran.movieapp.model.Movie;

import java.util.List;

@Dao
public interface IMovieDAO {

    @Query("SELECT * FROM favourite_movie")
    List<Movie> loadAllMovies();

    @Insert
    void saveMovieAsFavourite(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void removeMovieFromFavourites(Movie movie);

    @Query("SELECT * FROM favourite_movie WHERE movieId = :id")
    LiveData<Movie> loadMovieById(int id);

    @Query("SELECT * FROM favourite_movie WHERE favourite = 1 AND movieId = :id")
    LiveData<Movie> getFavourite(int id);

    @Query("SELECT * FROM favourite_movie WHERE movie_title = :movieTitle")
    Movie loadMovie(String movieTitle);
}
