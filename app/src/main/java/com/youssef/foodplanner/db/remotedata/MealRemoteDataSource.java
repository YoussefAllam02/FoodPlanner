package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {
    Observable<MealResponse> getAllMeals();

    Single<MealResponse> getRandomMeal();
    Observable<MealResponse> getMealsByIngredient(String ingredient);
}