package com.imran.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imran.movieapp.utils.Constants;
import com.imran.movieapp.R;
import com.imran.movieapp.activity.DetailActivity;
import com.imran.movieapp.model.Movie;
import com.imran.movieapp.navigator.BaseNavigator;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TopRatedMovieAdapter extends RecyclerView.Adapter<TopRatedMovieAdapter.ViewHolder> {


    private Context mContext;
    private List<Movie> mList;
    private BaseNavigator mCallback;
    private boolean mIsMyFavourites;

    public TopRatedMovieAdapter(Context context, List<Movie> moviesResult, BaseNavigator callback, boolean isMyFavourites) {
        mContext = context;
        mList = moviesResult;
        mCallback = callback;
        mIsMyFavourites = isMyFavourites;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public TopRatedMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopRatedMovieAdapter.ViewHolder holder, final int pos) {

        final Movie movie = mList.get(pos);
        Picasso.get().load(Constants.IMAGE_BASE_URL + movie.getMoviePoster())
                .placeholder(R.color.app_background)
                .error(R.color.colorPrimary)
                .into(holder.movie_image);
        holder.movie_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", movie.getMovieId());
                mContext.startActivity(intent);
            }
        });

        if (movie.isFavourite() && mIsMyFavourites) {
            holder.favourite_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite));
        } else {
            holder.favourite_image.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_unfavorite));
        }

        holder.favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie.isFavourite()) {
                    movie.isFavourite(false);
                } else {
                    movie.isFavourite(true);
                }
                notifyItemChanged(pos);
                if (mCallback != null) {
                    mCallback.onItemClick(v, movie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList != null && !mList.isEmpty() ? mList.size() : 0;
    }

    public void updateList(List<Movie> movieListResponse) {
        int s = mList.size();
        mList.addAll(movieListResponse);
        notifyItemRangeInserted(s, mList.size());
    }

    public void updateFavourite(List<Movie> movieListResponse) {
        mList = movieListResponse;
        notifyDataSetChanged();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView movie_image, favourite_image;


        private ViewHolder(View itemView) {
            super(itemView);
            movie_image = itemView.findViewById(R.id.movie_image);
            favourite_image = itemView.findViewById(R.id.favourite_image);
        }
    }
}
