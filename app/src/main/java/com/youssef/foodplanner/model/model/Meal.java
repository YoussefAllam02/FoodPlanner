package com.youssef.foodplanner.model.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class Meal {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_meal")
    private String idMeal;

    @ColumnInfo(name = "meal_name")
    private String mealName;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "area")
    private String mealArea;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "meal_thumb")
    private String mealImage;

    @ColumnInfo(name = "tags")
    private String mealTags;


    public Meal(String idMeal, String mealName, String Category, String mealArea,
                String strInstructions, String mealImage, String mealTags) {
        this.idMeal = idMeal;
        this.mealName = mealName;
        this.category = Category;
        this.mealArea = mealArea;
        this.instructions = strInstructions;
        this.mealImage = mealImage;
        this.mealTags = mealTags;
    }

    // Default constructor
    public Meal() { }

    // Getter methods
    public String getIdMeal() {
        return idMeal;
    }

    public String getMealName() {
        return mealName;
    }

    public String getCategory() {
        return category;
    }

    public String getMealArea() {
        return mealArea;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getMealImage() {
        return mealImage;
    }

    public String getMealTags() {
        return mealTags;
    }

    // Setter methods
    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMealArea(String mealArea) {
        this.mealArea = mealArea;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public void setMealTags(String mealTags) {
        this.mealTags = mealTags;
    }
}
