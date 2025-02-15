package com.youssef.foodplanner.model.model;

import com.youssef.foodplanner.db.localdata.MealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface
MealsRepository {
    Observable<List<Meal>> getAllMeals();
    Single<Meal> getMealById(String mealId);
    Observable<List<Meal>> getMealsByIngredient(String ingredient);
    Completable insertMeals(Meal meal);
    Single<MealResponse> getRandomMeal();
    Completable deleteAllMeals();
    Completable addToMealPlan(Meal meal);
    Completable addToFavourite(Meal meal);
    void clearCache();
}