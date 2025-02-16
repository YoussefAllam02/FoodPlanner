package com.youssef.foodplanner.model.model;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface MealsRepository {
    Observable<List<Meal>> getAllMeals();
    Single<Meal> getMealById(String mealId);
    Observable<List<Meal>> getMealsByIngredient(String ingredient);
    Completable insertMeals(Meal meal);
    Single<MealResponse> getRandomMeal();
    Completable deleteAllMeals();
    Completable addToMealPlan(Meal meal);
    Completable addToFavourite(Meal meal);
    void clearCache();


    Single<AreaResponse> getArea();
    Single<CategoryResponse> getCategory();
    Single<IngredientResponse> getIngredient();
}