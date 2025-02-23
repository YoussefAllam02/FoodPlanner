package com.youssef.foodplanner.model.model;

import java.util.List;

public class MealResponse {
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }
    public MealResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}