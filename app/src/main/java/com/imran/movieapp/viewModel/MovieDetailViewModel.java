package com.imran.movieapp.viewModel;

import com.imran.movieapp.BuildConfig;
import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.navigator.BaseNavigator;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.utils.StateLiveData;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailViewModel extends BaseViewModel<BaseNavigator> {

    public MovieDetailViewModel(APIInterface apiInterface, MovieDatabase movieDatabase, Executor executor) {
        super(apiInterface, movieDatabase, executor);
    }

    private final StateLiveData<Movie> listMutableLiveData = new StateLiveData<>();

    public StateLiveData<Movie> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void loadMovieDetail(int movie_id) {
        listMutableLiveData.postLoading();
        getApiInterface().getMovie(movie_id, BuildConfig.API_KEY, "en")
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Movie movie = response.body();
                            listMutableLiveData.postSuccess(movie);
                        } else {
                            listMutableLiveData.postComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        listMutableLiveData.postError(t);
                    }
                });
    }
}
