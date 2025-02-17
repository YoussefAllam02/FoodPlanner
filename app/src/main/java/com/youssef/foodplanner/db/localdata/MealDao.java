package com.youssef.foodplanner.db.localdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meals")
    Observable<List<Meal>> getfavmeal();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     Completable insert(Meal meal);
    @Query("DELETE FROM meals")
    Completable deleteAllMeals();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealToFavorites(Meal meal);
    @Query("SELECT * FROM meals WHERE is_favorite = 1")
    Single<List<Meal>> getFavoriteMeals();
    @Query("DELETE FROM meals WHERE id_meal = :mealId")
    Completable deleteFromFavorites(String mealId);
}
