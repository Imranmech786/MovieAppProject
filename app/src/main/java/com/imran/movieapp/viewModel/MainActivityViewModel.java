package com.imran.movieapp.viewModel;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.navigator.MainActivityNavigator;
import com.imran.movieapp.retrofit.APIInterface;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivityViewModel extends BaseViewModel<MainActivityNavigator> {

    public MainActivityViewModel(APIInterface apiInterface, MovieDatabase movieDatabase, Executor executor, PublishSubject<String> publishSubject) {
        super(apiInterface, movieDatabase, executor, publishSubject);
    }

    public void query(String query) {
        getPublishSubject().onNext(query);
    }

    public Observable<String> getObservable() {
        return getPublishSubject().debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) {
                        return s.trim().length() > 0;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) {
                        return s.trim();
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String strings) {
                getNavigator().searchQuery(strings.trim());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

}
