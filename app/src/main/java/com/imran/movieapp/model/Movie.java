package com.imran.movieapp.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "favourite_movie")
public class Movie {
    //model class to map json data to pojo
    //Movie GET Class objects from tmdb
    @ColumnInfo(name = "movie_title")
    @SerializedName("original_title")
    private String movieTitle;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String movieOverview;

    @ColumnInfo(name = "release")
    @SerializedName("release_date")
    private String movieReleaseDate;

    @ColumnInfo(name = "poster")
    @SerializedName("poster_path")
    private String moviePoster;

    @ColumnInfo(name = "voter_average")
    @SerializedName("vote_average")
    private float voterAverage;

    @ColumnInfo(name = "backdrop")
    @SerializedName("backdrop_path")
    private String backdrop;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int movieId;

    private int displayType;

    @ColumnInfo(name = "favourite")
    private boolean isFavourite;

    public Movie() {
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void isFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public float getVoterAverage() {
        return voterAverage;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setVoterAverage(float voterAverage) {
        this.voterAverage = voterAverage;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }
}
