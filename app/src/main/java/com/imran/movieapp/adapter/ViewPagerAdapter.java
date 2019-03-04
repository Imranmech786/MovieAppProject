package com.imran.movieapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imran.movieapp.fragment.FavouriteMovieFragment;
import com.imran.movieapp.fragment.TopRatedMovieFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] title_array;

    public ViewPagerAdapter(FragmentManager fm, String[] titleArray) {
        super(fm);
        this.title_array = titleArray;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new TopRatedMovieFragment();
            default:
                return new FavouriteMovieFragment();

        }
    }

    @Override
    public int getCount() {
        return title_array.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title_array[position];
    }
}
