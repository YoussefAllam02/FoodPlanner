package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.AreaResponse;
import com.youssef.foodplanner.model.model.CategoryResponse;
import com.youssef.foodplanner.model.model.IngredientResponse;
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

    private MealRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }


    @Override
    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return service.getMealsByIngredient(ingredient) // Pass the ingredient
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return service.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<MealResponse> getMealById(String mealId) {
        return service.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return service.getCategories(). subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return service.filterByCategory(category) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AreaResponse> filterByArea(String area) {
        return service.filterByArea(area) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AreaResponse> getAreas() {
        return service.getAreas(). subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<MealResponse> getAllMeals(String query) {
        return service.getAllMeals(query)  .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<IngredientResponse> getIngredients() {
        return service.getIngredients().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}