package com.youssef.foodplanner.FavMealPage.view;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public interface FavoriteMealsView {
    void showFavoriteMeals(List<Meal> meals);
    void showErrorMessage(String message);



}
