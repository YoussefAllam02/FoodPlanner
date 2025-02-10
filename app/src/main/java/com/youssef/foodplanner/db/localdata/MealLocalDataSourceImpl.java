package com.youssef.foodplanner.db.localdata;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.youssef.foodplanner.db.remotedata.AppDataBase;
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
private MealDao mealDao;
private static MealLocalDataSourceImpl localDataSource = null;
private LiveData<List<Meal>>Meals;
private MealLocalDataSourceImpl(Context context){
    AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
    mealDao = db.mealDao();
    Meals = mealDao.getAllMeals();

}
public static MealLocalDataSourceImpl getInstance(Context context) {
    if (localDataSource == null) {
        localDataSource = new MealLocalDataSourceImpl(context);
    }
    return localDataSource;
}

    @Override
    public LiveData<List<Meal>> getAllMeals() {
            return Meals;
    }

    @Override
    public void insertMeals(List<Meal> meals) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.insertMeals(meals);
            }
        }).start();

    }

    @Override
    public void deleteAllMeals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDao.deleteAllMeals();
            }

    }).start();
}
}