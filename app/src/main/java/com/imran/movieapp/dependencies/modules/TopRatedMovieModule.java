package com.imran.movieapp.dependencies.modules;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.viewModel.TopRatedMovieViewModel;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@Module
public class TopRatedMovieModule {

    @Provides
    TopRatedMovieViewModel topRatedMovieViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                                                  Executor executor) {
        return new TopRatedMovieViewModel(apiInterface, movieDatabase,executor);
    }
}
