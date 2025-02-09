package com.youssef.foodplanner.db.localdata;

import androidx.lifecycle.LiveData;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public interface MealLocalDataSource {
    LiveData<List<Meal>> getAllMeals();
    void insertMeals(List<Meal> meals);
    void deleteAllMeals();
}
