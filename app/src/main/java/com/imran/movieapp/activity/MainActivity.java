package com.imran.movieapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.imran.movieapp.R;
import com.imran.movieapp.adapter.ViewPagerAdapter;
import com.imran.movieapp.navigator.MainActivityNavigator;
import com.imran.movieapp.viewModel.MainActivityViewModel;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener, MainActivityNavigator, MenuItem.OnActionExpandListener {

    @Inject
    MainActivityViewModel mainActivityModule;

    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar("Movie App");
        mainActivityModule.setNavigator(this);
        TabLayout mTabs = findViewById(R.id.tabs);
        ViewPager mViewPager = findViewById(R.id.container);
        String[] title_array = new String[]{"Top Rated", "Favourites"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), title_array));
        mTabs.setupWithViewPager(mViewPager);
        mainActivityModule.getObservable().subscribe(mainActivityModule.getObserver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search");
            searchView.setOnQueryTextListener(this);
            searchItem.setOnActionExpandListener(this);
            searchView.clearFocus();
        }
        if (searchManager != null && searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mainActivityModule.query(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mainActivityModule.query(s);
        return false;
    }

    public MainActivityViewModel getMainActivityModule() {
        return mainActivityModule;
    }

    @Override
    public void searchQuery(String query) {
        mainActivityModule.getNavigator().searchQuery(query);
    }

    @Override
    public void topRatedMovie() {

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mainActivityModule.getNavigator().topRatedMovie();
        return true;
    }

}
