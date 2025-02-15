package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Query;

public interface MealRemoteDataSource {
    Single<MealResponse> getMealById(String mealId); // Add this line
    Observable<MealResponse> getAllMeals();

    Single<MealResponse> getRandomMeal();
    Observable<MealResponse> getMealsByIngredient(String ingredient);

    <@NonNull T> Observable<T> filterByIngredient(String ingredient);
}