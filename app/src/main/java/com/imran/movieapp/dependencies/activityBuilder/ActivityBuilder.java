package com.imran.movieapp.dependencies.activityBuilder;

import com.imran.movieapp.dependencies.modules.FavouriteMovieModule;
import com.imran.movieapp.dependencies.modules.MainActivityModule;
import com.imran.movieapp.dependencies.modules.MovieDetailModule;
import com.imran.movieapp.dependencies.modules.TopRatedMovieModule;
import com.imran.movieapp.activity.DetailActivity;
import com.imran.movieapp.activity.MainActivity;
import com.imran.movieapp.fragment.FavouriteMovieFragment;
import com.imran.movieapp.fragment.TopRatedMovieFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = TopRatedMovieModule.class)
    abstract TopRatedMovieFragment topRatedMovieFragment();

    @ContributesAndroidInjector(modules = FavouriteMovieModule.class)
    abstract FavouriteMovieFragment favouriteMovieFragment();

    @ContributesAndroidInjector(modules = MovieDetailModule.class)
    abstract DetailActivity detailActivity();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}
