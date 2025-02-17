package com.youssef.foodplanner.db.localdata;

import android.content.Context;
import android.util.Log;

import com.youssef.foodplanner.db.remotedata.AppDataBase;
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
    private MealDao mealDao;
    private static MealLocalDataSourceImpl localDataSource = null;

    public MealLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        mealDao = db.mealDao();
    }

    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (localDataSource == null) {
            localDataSource = new MealLocalDataSourceImpl(context);
        }
        return localDataSource;
    }


    public Observable<List<Meal>> getAllMeals() {
        return mealDao.getfavmeal(); // ✅ Now returns Observable instead of LiveData
    }

    @Override
    public Completable insertMeals(Meal meals) {
        return Completable.fromRunnable(() -> mealDao.insert(meals)); // ✅ Now uses Completable
    }

    @Override
    public Completable deleteAllMeals() {
        return Completable.fromRunnable(() -> mealDao.deleteAllMeals()); // ✅ Uses Completable for deletion
    }

    @Override
    public Completable insertMealToPlan(Meal meal) {
        return mealDao.insert(meal)
                .doOnComplete(() -> Log.d("DB", "Meal inserted: " + meal.getIdMeal()))
                .doOnError(e -> Log.e("DB", "Insert error: ", e));
    }

    @Override
    public Single<List<Meal>> getFavoriteMeals() {
        return mealDao.getFavoriteMeals();
    }

    @Override
    public Completable insertMealToFavorites(Meal meal) {
        return  mealDao.insert(meal);
    }

    @Override
    public Completable deleteFromFavorites(Meal meal) {
        return mealDao.deleteFromFavorites(meal.getIdMeal());
    }
    public Single<List<Meal>> getMealsByDate(String date) {
        Log.d("DB_QUERY", "Querying meals for date: " + date);
        return mealDao.getMealsByDate(date)
                .doOnSuccess(meals -> Log.d("DB_QUERY", "Found " + meals.size() + " meals"))
                .doOnError(e -> Log.e("DB_QUERY", "Error: ", e));
    }


}
