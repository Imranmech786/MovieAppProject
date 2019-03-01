package com.imran.movieapp.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.imran.movieapp.BuildConfig;
import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.model.MovieListResponse;
import com.imran.movieapp.navigator.BaseNavigator;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.utils.StateLiveData;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedMovieViewModel extends BaseViewModel<BaseNavigator> {

    public TopRatedMovieViewModel(APIInterface apiInterface, MovieDatabase movieDatabase, Executor executor) {
        super(apiInterface, movieDatabase, executor);
    }

    private final MutableLiveData<Boolean> isPaginationLoadingLiveData = new MutableLiveData<>();
    private final StateLiveData<List<Movie>> listMutableLiveData = new StateLiveData<>();
    private final MutableLiveData<Boolean> isAddedToFavourite = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsPaginationLoadingLiveData() {
        return isPaginationLoadingLiveData;
    }

    public StateLiveData<List<Movie>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsAddedToFavourite() {
        return isAddedToFavourite;
    }

    public void loadTopRatedMovie(final int mPageNumber) {
        setLoadingLiveData(mPageNumber);
        getApiInterface().getTopRatedMovies(BuildConfig.API_KEY, "en", mPageNumber)
                .enqueue(new Callback<MovieListResponse>() {
                    @Override
                    public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                        removeLoadingLiveData(mPageNumber, null);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getMoviesResult() != null && !response.body().getMoviesResult().isEmpty()) {
                                MovieListResponse movieListResponse = response.body();
                                listMutableLiveData.postSuccess(movieListResponse.getMoviesResult());
                            } else {
                                listMutableLiveData.postComplete();
                            }
                        } else {
                            listMutableLiveData.postComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable t) {
                        removeLoadingLiveData(mPageNumber, t);
                    }
                });
    }

    public void handleFavourites(final Movie movie) {
        getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (getMovieDatabase().movieDAO().loadMovie(movie.getMovieTitle()) == null) {
                    getMovieDatabase().movieDAO().saveMovieAsFavourite(movie);
                    isAddedToFavourite.postValue(true);
                } else {
                    getMovieDatabase().movieDAO().removeMovieFromFavourites(movie);
                    isAddedToFavourite.postValue(false);
                }
            }
        });
    }

    private void setLoadingLiveData(int mPageNumber) {
        if (mPageNumber > 1) {
            isPaginationLoadingLiveData.setValue(true);
        } else {
            listMutableLiveData.postLoading();
        }
    }

    private void removeLoadingLiveData(int mPageNumber, Throwable throwable) {
        if (mPageNumber > 1) {
            isPaginationLoadingLiveData.setValue(false);
        } else if (throwable != null) {
            listMutableLiveData.postError(throwable);
        }
    }

    public void getSearchMovie(String query, final int mPageNumber) {
        setLoadingLiveData(mPageNumber);
        getApiInterface().getSearchMovies(BuildConfig.API_KEY, query, mPageNumber)
                .enqueue(new Callback<MovieListResponse>() {
                    @Override
                    public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                        removeLoadingLiveData(mPageNumber, null);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getMoviesResult() != null && !response.body().getMoviesResult().isEmpty()) {
                                MovieListResponse movieListResponse = response.body();
                                listMutableLiveData.postSuccess(movieListResponse.getMoviesResult());
                            } else {
                                listMutableLiveData.postComplete();
                            }
                        } else {
                            listMutableLiveData.postComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable t) {
                        removeLoadingLiveData(mPageNumber, t);
                        Log.i("api", t.getMessage());
                    }
                });
    }
}
