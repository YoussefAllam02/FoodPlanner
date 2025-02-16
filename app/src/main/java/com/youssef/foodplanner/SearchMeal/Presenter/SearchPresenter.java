package com.youssef.foodplanner.SearchMeal.Presenter;

public interface SearchPresenter {
   void getmealsByIngredient(String ingredient);
   void getmealsByArea(String area);
   void getmealsByCategory(String category);

}
