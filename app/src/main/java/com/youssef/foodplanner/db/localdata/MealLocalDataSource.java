package com.youssef.foodplanner.db.localdata;

import androidx.lifecycle.LiveData;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable insertMeals(Meal meal);
    Completable deleteAllMeals();
    Completable insertMealToPlan(Meal meal);
    // In MealLocalDataSourceImpl.java
     Single<List<Meal>> getFavoriteMeals();
    Completable insertMealToFavorites(Meal meal);
    Single<List<Meal>> getMealsByDate(String date);
    Completable deleteFromFavorites(Meal meal);

         // Uses Room DAO

}
