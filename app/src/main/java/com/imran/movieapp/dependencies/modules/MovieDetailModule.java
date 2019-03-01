package com.imran.movieapp.dependencies.modules;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.viewModel.MovieDetailViewModel;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieDetailModule {

    @Provides
    MovieDetailViewModel movieDetailViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                                              Executor executor) {
        return new MovieDetailViewModel(apiInterface, movieDatabase, executor);
    }
}
