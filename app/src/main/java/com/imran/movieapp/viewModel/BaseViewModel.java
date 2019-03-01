package com.imran.movieapp.viewModel;

import android.arch.lifecycle.ViewModel;

import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.db.MovieDatabase;

import java.util.concurrent.Executor;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by kautilya on 01/02/18.
 */

public abstract class BaseViewModel<N> extends ViewModel {

    private N mNavigator;
    private APIInterface apiInterface;
    private MovieDatabase movieDatabase;
    private Executor executor;
    private PublishSubject<String> publishSubject;


    protected BaseViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                            Executor executor) {
        this.apiInterface = apiInterface;
        this.movieDatabase = movieDatabase;
        this.executor = executor;
    }

    protected BaseViewModel(APIInterface apiInterface, MovieDatabase movieDatabase,
                            Executor executor, PublishSubject<String> publishSubject) {
        this.apiInterface = apiInterface;
        this.movieDatabase = movieDatabase;
        this.executor = executor;
        this.publishSubject = publishSubject;
    }

    PublishSubject<String> getPublishSubject() {
        return publishSubject;
    }

    public APIInterface getApiInterface() {
        return apiInterface;
    }

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public MovieDatabase getMovieDatabase() {
        return movieDatabase;
    }

    public Executor getExecutor() {
        return executor;
    }
}
