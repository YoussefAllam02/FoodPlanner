package com.youssef.foodplanner.allmeals.view;



import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public interface AllMealsView {
    public void showAllProducts(List<Meal> products);
    public void showErrorMessage(String message);

}
