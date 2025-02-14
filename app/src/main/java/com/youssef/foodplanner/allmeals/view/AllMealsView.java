package com.youssef.foodplanner.allmeals.view;



import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public interface AllMealsView {
    void showAllProducts(List<Meal> meals);
    void showErrorMessage(String message);
    void showSuccessMessage(String message);

}
