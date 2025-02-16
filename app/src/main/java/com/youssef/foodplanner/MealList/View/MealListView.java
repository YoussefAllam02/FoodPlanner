package com.youssef.foodplanner.MealList.View;

import com.youssef.foodplanner.model.model.Area;
import com.youssef.foodplanner.model.model.AreaData;
import com.youssef.foodplanner.model.model.Category;
import com.youssef.foodplanner.model.model.Ingredient;
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public interface MealListView {
    public void show_meals_Areas(List<AreaData.MealsDTO> meals);
    void show_meals_Categories(List<AreaData.MealsDTO> meals);
    void show_meals_Ingredients(List<AreaData.MealsDTO> meals);
    void showSearchResults(List<Meal> meals);
    void showError(String message);
}
