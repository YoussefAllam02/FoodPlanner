package com.youssef.foodplanner.MealList.Presenter;

public interface MealListPresenter {
    void getmealsByIngredient( String Ingredient);
    void getmealsByArea( String Area);
    void getmealsByCategory( String category);
}
