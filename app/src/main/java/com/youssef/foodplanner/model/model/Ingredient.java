package com.youssef.foodplanner.model.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("strIngredient")
    private  String strIngredient;

    public String getStrIngredient() {
        return strIngredient;
    }

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public String getIngredientImageUrl() {
        return "https://www.themealdb.com/images/ingredients/" + strIngredient + "-Small.png";
    }
}
