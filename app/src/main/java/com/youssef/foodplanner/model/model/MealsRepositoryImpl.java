package com.youssef.foodplanner.model.model;

import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements MealsRepository {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private static MealsRepositoryImpl repo = null;

    // Singleton Pattern
    public static MealsRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (repo == null) {
            repo = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        }
        return repo;
    }

    public MealsRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Observable<List<Meal>> getAllMeals() {
        return remoteDataSource.makeNetworkCall()
                .map(MealResponse::getMeals) // Extracting list of meals from response
                .subscribeOn(Schedulers.io()); // Running on background thread
    }

    @Override
    public Completable insertMeals(Meal meals) {
        return localDataSource.insertMeals(meals);
    }

    @Override
    public Completable deleteAllMeals() {
        return localDataSource.deleteAllMeals();
    }

    @Override
    public Completable addtoMealPlan(Meal meal) {
        return localDataSource.insertMeals(meal);
    }

    @Override
    public Completable addtoFavourite(Meal meal) {
        return localDataSource.insertMeals(meal);
    }
}
