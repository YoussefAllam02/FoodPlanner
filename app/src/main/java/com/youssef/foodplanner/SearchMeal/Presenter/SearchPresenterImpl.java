package com.youssef.foodplanner.SearchMeal.Presenter;

import com.youssef.foodplanner.SearchMeal.View.SearchView;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

public class SearchPresenterImpl implements  SearchPresenter {

    private MealsRepositoryImpl mealRepository;
    private SearchView SearchView;

    public SearchPresenterImpl(SearchView SearchView, MealsRepositoryImpl mealRepository) {
        this.SearchView = SearchView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getmealsByIngredient(String ingredient) {

    }

    @Override
    public void getmealsByArea(String area) {

    }

    @Override
    public void getmealsByCategory(String category) {

    }
}
