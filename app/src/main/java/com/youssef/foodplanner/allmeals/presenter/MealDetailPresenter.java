package com.youssef.foodplanner.allmeals.presenter;

import com.youssef.foodplanner.model.model.Meal;

public interface MealDetailPresenter {
    void getMealById(String mealId);
    void checkIfFavorite(String mealId);
    void toggleFavorite(Meal meal);
    void dispose();
}