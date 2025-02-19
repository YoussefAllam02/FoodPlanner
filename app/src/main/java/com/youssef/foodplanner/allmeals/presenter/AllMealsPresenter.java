package com.youssef.foodplanner.allmeals.presenter;

import com.youssef.foodplanner.model.model.Meal;

public interface AllMealsPresenter {
    void getMeals();

    void getRandomMeal();

    void getMealsByIngredient(String ingredient);

    void addToFav(Meal meal);
    void addtoPlan(Meal meal);
}
