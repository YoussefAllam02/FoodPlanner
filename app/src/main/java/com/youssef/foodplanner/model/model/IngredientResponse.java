package com.youssef.foodplanner.model.model;

import java.util.List;

public class IngredientResponse {

    private List<Ingredient>Ingredient;

    public List<com.youssef.foodplanner.model.model.Ingredient> getIngredient() {
        return Ingredient;
    }

    public void setIngredient(List<com.youssef.foodplanner.model.model.Ingredient> ingredient) {
        Ingredient = ingredient;
    }
}
