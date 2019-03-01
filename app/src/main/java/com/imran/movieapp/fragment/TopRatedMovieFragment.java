package com.imran.movieapp.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imran.movieapp.R;
import com.imran.movieapp.activity.MainActivity;
import com.imran.movieapp.adapter.TopRatedMovieAdapter;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.navigator.BaseNavigator;
import com.imran.movieapp.navigator.MainActivityNavigator;
import com.imran.movieapp.utils.Network;
import com.imran.movieapp.utils.StateData;
import com.imran.movieapp.viewModel.TopRatedMovieViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class TopRatedMovieFragment extends DaggerFragment implements BaseNavigator, MainActivityNavigator {

    private Context mContext;
    private RelativeLayout rootView;

    @Inject
    TopRatedMovieViewModel mTopRatedMovieViewModel;

    private TopRatedMovieAdapter mTopRatedMovieAdapter;

    private int mPageNumber = 1;
    private boolean mIsSearch;
    private String mQuery;
    private ProgressBar progressBar, paginationProgressBar;
    private TextView retry;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getMainActivityModule().setNavigator(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_rated_fragment, container, false);
        mTopRatedMovieViewModel.setNavigator(this);
        final RecyclerView mRecyclerview = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progress_bar);
        paginationProgressBar = view.findViewById(R.id.pagination_progress_bar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        rootView = view.findViewById(R.id.root_view);
        retry = view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForTopRatedMovie();
            }
        });

        requestForTopRatedMovie();

        /*Observing Movie List Live Data*/
        mTopRatedMovieViewModel.getListMutableLiveData().observe(this, new Observer<StateData<List<Movie>>>() {
            @Override
            public void onChanged(@Nullable StateData<List<Movie>> listStateData) {
                if (listStateData == null) return;
                switch (listStateData.getStatus()) {
                    case SUCCESS:
                        setProgressBarVisibility(View.GONE);
                        List<Movie> movieList = listStateData.getData();
                        if (mTopRatedMovieAdapter == null) {
                            mTopRatedMovieAdapter = new TopRatedMovieAdapter(mContext, movieList, mTopRatedMovieViewModel.getNavigator(), false);
                            mRecyclerview.setAdapter(mTopRatedMovieAdapter);
                        } else {
                            mTopRatedMovieAdapter.updateList(movieList);
                        }
                        break;
                    case ERROR:
                        setProgressBarVisibility(View.GONE);
                        if (listStateData.getError() != null && listStateData.getError().getMessage() != null) {
                            Throwable e = listStateData.getError();
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LOADING:
                        setProgressBarVisibility(View.VISIBLE);
                        break;
                    case COMPLETE:
                        setProgressBarVisibility(View.GONE);
                        Toast.makeText(mContext.getApplicationContext(), "No Results Found", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        /*Observing Boolean Live Data for showing Pagination Progress Bar*/
        mTopRatedMovieViewModel.getIsPaginationLoadingLiveData().observe(this, getPaginationLoadingObserver());

        /*Recyclerview on Scroll for loading more items via Pagination*/
        setRecyclerViewScrollListener(gridLayoutManager, mRecyclerview);

        /*Observing Boolean Live Data for Added to Favourites*/
        mTopRatedMovieViewModel.getIsAddedToFavourite().observe(this, getAddedToFavouritesObserver());


        return view;
    }

    private void requestForTopRatedMovie() {
        if (Network.isConnected(mContext.getApplicationContext())) {
            retry.setVisibility(View.GONE);
            mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber);
        } else {
            retry.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, mContext.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    private void setRecyclerViewScrollListener(final GridLayoutManager gridLayoutManager, RecyclerView mRecyclerview) {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int mVisibleItemCount = gridLayoutManager.getChildCount();
                int mTotalItemCount = gridLayoutManager.getItemCount();
                int mPastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
                if ((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount) {
                    loadMore();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });
    }

    private Observer<Boolean> getPaginationLoadingObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean visibility) {
                paginationProgressBar.setVisibility(visibility != null && visibility ? View.VISIBLE : View.GONE);

            }
        };
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
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        snackbar.show();
    }

    @Override
    public void onItemClick(View view, Movie movie) {
        if (movie != null) {
            mTopRatedMovieViewModel.handleFavourites(movie);
        }
    }

    public void loadMore() {
        mPageNumber++;
        if (mIsSearch && mQuery != null) {
            mTopRatedMovieViewModel.getSearchMovie(mQuery, mPageNumber);
        } else {
            mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber);
        }

    }

    @Override
    public void searchQuery(String query) {
        Log.i("observer", "onNext " + query);
        clearAdapterList(query, true);
        mTopRatedMovieViewModel.getSearchMovie(query, mPageNumber);
    }

    private void clearAdapterList(String query, boolean isSearch) {
        mIsSearch = isSearch;
        mQuery = query;
        mPageNumber = 1;
        if (mTopRatedMovieAdapter != null) {
            mTopRatedMovieAdapter.clear();
        }
    }

    @Override
    public void topRatedMovie() {
        clearAdapterList(null, false);
        mTopRatedMovieViewModel.loadTopRatedMovie(mPageNumber);
    }

}
