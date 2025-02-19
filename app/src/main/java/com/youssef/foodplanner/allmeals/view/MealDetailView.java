package com.youssef.foodplanner.allmeals.view;

import com.youssef.foodplanner.model.model.Meal;

public interface MealDetailView {
    void showMealDetails(Meal meal);
    void updateFavoriteStatus(boolean isFavorite);
    void showErrorMessage(String message);
}