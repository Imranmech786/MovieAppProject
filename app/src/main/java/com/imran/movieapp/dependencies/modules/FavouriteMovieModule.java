package com.imran.movieapp.dependencies.modules;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.viewModel.FavouriteMovieViewModel;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@Module
public class FavouriteMovieModule {

    @Provides
    FavouriteMovieViewModel favouriteMovieViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                                                    Executor executor) {
        return new FavouriteMovieViewModel(apiInterface, movieDatabase, executor);
    }
}
