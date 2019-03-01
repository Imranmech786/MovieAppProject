package com.imran.movieapp.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imran.movieapp.R;
import com.imran.movieapp.adapter.TopRatedMovieAdapter;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.navigator.BaseNavigator;
import com.imran.movieapp.viewModel.FavouriteMovieViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class FavouriteMovieFragment extends DaggerFragment implements BaseNavigator {

    private Context mContext;

    @Inject
    FavouriteMovieViewModel favouriteMovieViewModel;

    private TopRatedMovieAdapter topRatedMovieAdapter;
    private RelativeLayout rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_movie_fragment, container, false);
        favouriteMovieViewModel.setNavigator(this);
        final RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        rootView = view.findViewById(R.id.root_view);
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));
        final TextView noFavs = view.findViewById(R.id.no_favs);
        favouriteMovieViewModel.getListMutableLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                boolean visibility = movieList == null || movieList.isEmpty();
                noFavs.setVisibility(visibility ? View.VISIBLE : View.GONE);
                if (topRatedMovieAdapter == null) {
                    topRatedMovieAdapter = new TopRatedMovieAdapter(mContext, movieList, favouriteMovieViewModel.getNavigator(), true);
                    recyclerview.setAdapter(topRatedMovieAdapter);
                } else {
                    topRatedMovieAdapter.updateFavourite(movieList);
                }
            }
        });

        /*Observing Boolean Live Data for Added to Favourites*/
        favouriteMovieViewModel.getIsAddedToFavourite().observe(this, getAddedToFavouritesObserver());

        return view;
    }

    private Observer<Boolean> getAddedToFavouritesObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFav) {
                if (isFav != null) {
                    favouriteMovieViewModel.getFavMovieList();
                    String message = isFav ? "Added to Favourites" : "Removed from Favourites";
                    showSnackBar(message);

                }
            }
        };
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        snackbar.show();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            favouriteMovieViewModel.getFavMovieList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favouriteMovieViewModel.getFavMovieList();
    }

    @Override
    public void onItemClick(View view, Movie movie) {
        if (movie != null) {
            favouriteMovieViewModel.handleFavourites(movie);
        }
    }

}
