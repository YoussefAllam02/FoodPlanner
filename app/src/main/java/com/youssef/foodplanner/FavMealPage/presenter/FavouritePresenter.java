package com.youssef.foodplanner.FavMealPage.presenter;

import com.youssef.foodplanner.model.model.Meal;

public interface FavouritePresenter {
    void getFavorites();
    void delete(Meal meal);
}
