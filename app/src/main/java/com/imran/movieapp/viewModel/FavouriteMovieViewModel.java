package com.imran.movieapp.viewModel;

import android.arch.lifecycle.MutableLiveData;

import com.imran.movieapp.navigator.BaseNavigator;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.model.Movie;

import java.util.List;
import java.util.concurrent.Executor;

public class FavouriteMovieViewModel extends BaseViewModel<BaseNavigator> {

    private final MutableLiveData<List<Movie>> listMutableLiveData = new MutableLiveData<>();

    public FavouriteMovieViewModel(APIInterface apiInterface, MovieDatabase movieDatabase, Executor executor) {
        super(apiInterface, movieDatabase, executor);
    }

    public MutableLiveData<List<Movie>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void getFavMovieList() {
        getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                listMutableLiveData.postValue(getMovieDatabase().movieDAO().loadAllMovies());
            }
        });
    }
}
