package com.imran.movieapp.navigator;

import android.view.View;

import com.imran.movieapp.model.Movie;

public interface BaseNavigator {

    void onItemClick(View view, Movie movie);

}
