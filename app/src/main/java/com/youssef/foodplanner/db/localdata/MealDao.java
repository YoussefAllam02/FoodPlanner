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

@Dao
public interface MealDao {
    @Query("SELECT * FROM meals")
    Observable<List<Meal>> getAllMeals();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     Completable insert(Meal meal);
    @Query("DELETE FROM meals")
    Completable deleteAllMeals();
}
