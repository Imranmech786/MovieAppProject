package com.imran.movieapp.dependencies.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.imran.movieapp.db.MovieDatabase;
import com.imran.movieapp.dependencies.qualifier.ApplicationContext;
import com.imran.movieapp.dependencies.qualifier.DatabaseInfo;
import com.imran.movieapp.retrofit.APIInterface;
import com.imran.movieapp.utils.Constants;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final static String MOVIE_DB_URL = "http://api.themoviedb.org/3/";

    @Provides
    @ApplicationContext
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @DatabaseInfo
    String getDbName() {
        return Constants.DATABASE_NAME;
    }

    @Provides
    @Singleton
    MovieDatabase movieDatabase(@ApplicationContext Context context, @DatabaseInfo String dbNAme) {
        return Room.databaseBuilder(context, MovieDatabase.class, dbNAme)
                .fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    PublishSubject<String> publishSubject() {
        return PublishSubject.create();
    }

    @Provides
    APIInterface getApiInterface(Retrofit retroFit) {
        return retroFit.create(APIInterface.class);
    }

    @Provides
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient getOkHttpCleint(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}
