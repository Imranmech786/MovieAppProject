package com.imran.movieapp.dependencies.modules;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.viewModel.MainActivityViewModel;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;

@Module
public class MainActivityModule {

    @Provides
    MainActivityViewModel mainActivityViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                                                Executor executor, PublishSubject<String> publishSubject) {
        return new MainActivityViewModel(apiInterface, movieDatabase, executor, publishSubject);
    }
}
