package com.youssef.foodplanner.model.model;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface MealsRepository {



    Single<AreaData>getMealsByArea(String area);
    Single<AreaData>getMealsByCategory(String category);
    Single<AreaData>getMealsByIngredients(String ingredient);










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
    Single<MealResponse> filterByIngredient(String Ingredient);

    public Single<MealResponse> filterByCategory(String Category);

    public Single<AreaResponse> filterByArea( String area);
    Single<CategoryResponse> getCategory();
    Single<IngredientResponse> getIngredient();
}