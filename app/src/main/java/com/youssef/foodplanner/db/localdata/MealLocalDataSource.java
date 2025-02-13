package com.youssef.foodplanner.db.localdata;

import androidx.lifecycle.LiveData;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface MealLocalDataSource {
    Observable<List<Meal>> getAllMeals();
    Completable insertMeals(Meal meal);
    Completable deleteAllMeals();
}
