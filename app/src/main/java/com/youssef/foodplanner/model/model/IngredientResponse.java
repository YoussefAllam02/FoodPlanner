package com.youssef.foodplanner.model.model;

import java.util.List;

public class IngredientResponse {

    private List<Ingredient>meals;

    public List<Ingredient> getIngredient() {
        return meals;
    }

    public void setIngredient(List<Ingredient> meals) {
        this.meals = meals;
    }
}
