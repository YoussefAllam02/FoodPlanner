package com.youssef.foodplanner.model.model;

import androidx.lifecycle.LiveData;

import com.youssef.foodplanner.db.remotedata.NetworkCallBack;

import java.util.List;

public interface MealsRepository {
void getAllMeals(NetworkCallBack callBack);
void insertMeals(List<Meal> meals);
void deleteAllMeals();
void addtoMealPlan(Meal meal);
void addtoFavourite(Meal meal);
LiveData<List<Meal>> getAllMeals();>
}
