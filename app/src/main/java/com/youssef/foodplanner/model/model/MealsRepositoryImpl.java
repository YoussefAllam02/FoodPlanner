package com.youssef.foodplanner.model.model;

import android.util.Log;

import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class MealsRepositoryImpl implements MealsRepository {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private static MealsRepositoryImpl repo = null;

    public static MealsRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource,
                                                  MealLocalDataSource localDataSource) {
        if (repo == null) {
            repo = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        }
        return repo;
    }

    public MealsRepositoryImpl(MealRemoteDataSource remoteDataSource,
                               MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Meal>> getAllMeals() {
        return remoteDataSource.getAllMeals()
                .map(MealResponse::getMeals)
                .onErrorReturn(throwable -> {
                    Log.e("MealsRepositoryImpl", "Error fetching meals", throwable);
                    return new ArrayList<>(); // Return an empty list in case of error
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Meal>> getMealsByIngredient(String ingredient) {
        return remoteDataSource.getMealsByIngredient(ingredient)
                .map(MealResponse::getMeals)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertMeals(Meal meal) {
        return localDataSource.insertMeals(meal);
    }

    @Override
    public Completable deleteAllMeals() {
        return localDataSource.deleteAllMeals();
    }

    @Override
    public Completable addToMealPlan(Meal meal) {
        return localDataSource.insertMealToPlan(meal);
    }

    @Override
    public Completable addToFavourite(Meal meal) {
        return localDataSource.insertMealToFavorites(meal);
    }
}