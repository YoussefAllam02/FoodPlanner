package com.youssef.foodplanner.db.localdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.youssef.foodplanner.model.model.Meal;

import java.util.List;
@Dao
public interface MealDao {
    @Query("SELECT * FROM meals")
    LiveData<List<Meal>> getAllMeals();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeals(List<Meal> meals);
    @Query("DELETE FROM meals")
    void deleteAllMeals();
}
