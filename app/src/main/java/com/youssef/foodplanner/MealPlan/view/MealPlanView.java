// MealPlanView.java
package com.youssef.foodplanner.MealPlan.view;

import com.youssef.foodplanner.model.model.Meal;
import java.util.List;

public interface MealPlanView {
    void showMeals(List<Meal> meals);
    void showError(String message);
    void onMealAddedSuccess();
    void clearArguments();
}