/*
 * Copyright (c) 2018.
 *
 * This file is part of MovieDB.
 *
 * MovieDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MovieDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MovieDB.  If not, see <http://www.gnu.org/licenses/>.
 */

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
