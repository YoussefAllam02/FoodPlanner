package com.youssef.foodplanner.allmeals.presenter;

import com.youssef.foodplanner.model.model.Meal;

public interface AllMealsPresenter {
    void getMeals();
    void addToFav(Meal meal);
    void addtoPlan(Meal meal);
}
