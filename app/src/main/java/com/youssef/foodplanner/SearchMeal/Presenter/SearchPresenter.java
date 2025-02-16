package com.youssef.foodplanner.SearchMeal.Presenter;

public interface SearchPresenter {
   void loadAreas();
   void loadCategories();
   void loadIngredients();
   void SearchOnIngrdient(String query);
   void SearchOnArea(String query);
   void SearchOnCategory(String query);

}