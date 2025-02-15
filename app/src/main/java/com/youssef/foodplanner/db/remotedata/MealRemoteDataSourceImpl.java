package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSourceImpl implements MealRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final ApiService service;

    private static volatile MealRemoteDataSourceImpl instance;

    public static MealRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            synchronized (MealRemoteDataSourceImpl.class) {
                if (instance == null) {
                    instance = new MealRemoteDataSourceImpl();
                }
            }
        }
        return instance;
    }

    public MealRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // ✅ RxJava3 Adapter
                .build();
        service = retrofit.create(ApiService.class);
    }

    @Override
    public Observable<MealResponse> getAllMeals() {
        return service.getAllMeals("") // Fetch all meals (empty query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
    }

    @Override
    public Observable<MealResponse> getMealsByIngredient(String ingredient) {
        return service.getMealsByIngredient()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return service.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}