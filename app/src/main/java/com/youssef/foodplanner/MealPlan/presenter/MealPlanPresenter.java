// MealPlanPresenter.java
package com.youssef.foodplanner.MealPlan.presenter;

import com.youssef.foodplanner.model.model.Meal;
import java.util.List;

public interface MealPlanPresenter {
    void getMealsForDate(String date);
    void addMealToPlan(Meal meal, String date);
}