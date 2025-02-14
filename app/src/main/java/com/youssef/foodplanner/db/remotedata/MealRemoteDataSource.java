package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.core.Observable;

public interface MealRemoteDataSource {
    Observable<MealResponse> getAllMeals();
    Observable<MealResponse> getMealsByIngredient(String ingredient);
}