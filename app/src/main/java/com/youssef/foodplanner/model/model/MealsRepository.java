package com.youssef.foodplanner.model.model;

import com.youssef.foodplanner.db.localdata.MealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface MealsRepository {

    Observable<List<Meal>> getAllMeals();


     Completable insertMeals(Meal meal);

    Completable deleteAllMeals();

    Completable addtoMealPlan(Meal meal); // ✅ Uses Completable for insertions

    Completable addtoFavourite(Meal meal); // ✅ Already using Completable
}
