package com.imran.movieapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.imran.movieapp.model.Movie;

@Database(entities = {Movie.class}, version = 3, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract IMovieDAO movieDAO();
}
