package com.youssef.foodplanner.model.model;

import com.youssef.foodplanner.db.localdata.MealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface MealsRepository {
    Observable<List<Meal>> getAllMeals();
    Observable<List<Meal>> getMealsByIngredient(String ingredient);
    Completable insertMeals(Meal meal);
    Completable deleteAllMeals();
    Completable addToMealPlan(Meal meal);
    Completable addToFavourite(Meal meal);
}