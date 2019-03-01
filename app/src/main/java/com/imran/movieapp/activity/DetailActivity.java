package com.imran.movieapp.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imran.movieapp.R;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.utils.Constants;
import com.imran.movieapp.utils.Network;
import com.imran.movieapp.utils.StateData;
import com.imran.movieapp.viewModel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    MovieDetailViewModel movieDetailViewModel;

    private ImageView mPosterImageView, mMovieImageView;
    private TextView mTitle, mReleaseDate, mMovieDescription;
    private RatingBar mRatingBar;
    private CardView mMainLayout;
    private ProgressBar mProgressBar;
    private Movie movie;
    private TextView retry;
    private int movieId;

    @Override
    protected int getResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar("");
        getWidgets();

        /*Observing Boolean Live Data for showing Progress Bar*/
        movieDetailViewModel.getListMutableLiveData().observe(this, getObserver());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getInt("id");
            requestForMovieDetail();
        }
    }

    private void getWidgets() {
        mProgressBar = findViewById(R.id.progress_bar);
        mMainLayout = findViewById(R.id.main_layout);
        mMainLayout.setVisibility(View.GONE);
        mPosterImageView = findViewById(R.id.poster_image);
        mMovieImageView = findViewById(R.id.movie_image);
        mTitle = findViewById(R.id.title);
        mReleaseDate = findViewById(R.id.release_date);
        mMovieDescription = findViewById(R.id.movie_description);
        mRatingBar = findViewById(R.id.rating_bar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        FloatingActionButton mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);
    }

    private void requestForMovieDetail() {
        if (Network.isConnected(getApplicationContext())) {
            retry.setVisibility(View.GONE);
            movieDetailViewModel.loadMovieDetail(movieId);
        } else {
            retry.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData() {
        mTitle.setText(movie.getMovieTitle());
        mReleaseDate.setText(movie.getMovieReleaseDate());
        mMovieDescription.setText(movie.getMovieOverview());
        mRatingBar.setRating(movie.getVoterAverage());
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.getBackdrop())
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary)
                .into(mPosterImageView);
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.getMoviePoster())
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary)
                .into(mMovieImageView);
        /*Observing Boolean Live Data for Added to Favourites*/
        movieDetailViewModel.getIsAddedToFavourite().observe(this, getAddedToFavouritesObserver());

    }

    private Observer<Boolean> getAddedToFavouritesObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFav) {
                if (isFav != null) {
                    String message = isFav ? "Added to Favourites" : "Removed from Favourites";
                    showSnackBar(message);
                }
            }
        };
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mMainLayout, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    private Observer<StateData<Movie>> getObserver() {
        return new Observer<StateData<Movie>>() {
            @Override
            public void onChanged(@Nullable StateData<Movie> movieStateData) {
                if (movieStateData == null) return;
                switch (movieStateData.getStatus()) {
                    case SUCCESS:
                        setVisibility(View.GONE, View.VISIBLE);
                        movie = movieStateData.getData();
                        updateData();
                        break;
                    case ERROR:
                        setVisibility(View.GONE, View.GONE);
                        if (movieStateData.getError() != null && movieStateData.getError().getMessage() != null) {
                            Throwable e = movieStateData.getError();
                            Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LOADING:
                        setVisibility(View.VISIBLE, View.GONE);
                        break;
                    case COMPLETE:
                        /*No Results Found*/
                        setVisibility(View.GONE, View.VISIBLE);
                        Toast.makeText(DetailActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void setVisibility(int progressVisibility, int mainVisibility) {
        mProgressBar.setVisibility(progressVisibility);
        mMainLayout.setVisibility(mainVisibility);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                movieDetailViewModel.handleFavourites(movie);
                break;
            case R.id.retry:
                requestForMovieDetail();
                break;
        }
    }
}
