package com.youssef.foodplanner.model.model;

import android.util.Log;

import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.remotedata.ApiService;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements MealsRepository {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private static MealsRepositoryImpl repo = null;

    // Caching variables
    private final Map<String, List<Meal>> cache = new HashMap<>(); // Cache for meals
    private final long CACHE_EXPIRY_TIME = 10 * 60 * 1000;
    private final Map<String, Long> cacheTimestamps = new HashMap<>(); // Cache timestamps

    public static MealsRepositoryImpl getInstance(MealRemoteDataSource remoteDataSource,
                                                  MealLocalDataSource localDataSource) {
        if (repo == null) {
            repo = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        }
        return repo;
    }

    public MealsRepositoryImpl(MealRemoteDataSource remoteDataSource,
                               MealLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }
    @Override
    public Single<Meal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .flatMap(mealResponse -> {
                    if (mealResponse.getMeals() == null || mealResponse.getMeals().isEmpty()) {
                        return Single.error(new Exception("Meal not found"));
                    }
                    return Single.just(mealResponse.getMeals().get(0));
                });
    }
    @Override
    public Single<MealResponse> getRandomMeal() {
        String cacheKey = "random_meal";
        if (isCacheValid(cacheKey)) {
            return Single.just(new MealResponse(cache.get(cacheKey)));
        }

        return remoteDataSource.getRandomMeal()
                .doOnSuccess(mealResponse -> {
                    cache.put(cacheKey, mealResponse.getMeals()); // Cache the result
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis()); // Update timestamp
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Meal>> getAllMeals() {
        String cacheKey = "all_meals";
        if (isCacheValid(cacheKey)) {
            return Observable.just(cache.get(cacheKey)); // Return cached data
        }

        return remoteDataSource.getAllMeals()
                .map(MealResponse::getMeals)
                .doOnNext(meals -> {
                    cache.put(cacheKey, meals); // Cache the result
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis()); // Update timestamp
                })
                .onErrorReturn(throwable -> {
                    Log.e("MealsRepositoryImpl", "Error fetching meals", throwable);
                    return new ArrayList<>(); // Return an empty list in case of error
                })
                .subscribeOn(Schedulers.io());
    }

    @Override

    public Observable<List<Meal>> getMealsByIngredient(String ingredient) {
        String cacheKey = "meals_by_ingredient_" + ingredient;

        Log.d("MealsRepositoryImpl", "Fetching new data for ingredient: " + ingredient);
        return remoteDataSource.getMealsByIngredient(ingredient)
                .map(MealResponse::getMeals)
                .doOnNext(meals -> {
                    cache.put(cacheKey, meals); // Cache the result
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis()); // Update timestamp
                })
                .subscribeOn(Schedulers.io());
    }
    @Override
    public Completable insertMeals(Meal meal) {
        return localDataSource.insertMeals(meal);
    }

    @Override
    public Completable deleteAllMeals() {
        return localDataSource.deleteAllMeals();
    }

    @Override
    public Completable addToMealPlan(Meal meal) {
        return localDataSource.insertMealToPlan(meal);
    }

    @Override
    public Completable addToFavourite(Meal meal) {
        return localDataSource.insertMealToFavorites(meal);
    }

    // Helper method to check if cache is still valid
    private boolean isCacheValid(String cacheKey) {
        if (cache.containsKey(cacheKey) && cacheTimestamps.containsKey(cacheKey)) {
            long currentTime = System.currentTimeMillis();
            long cacheTime = cacheTimestamps.get(cacheKey);
            return (currentTime - cacheTime) < CACHE_EXPIRY_TIME; // Check if cache is still valid
        }
        return false;
    }

    // Optional: Method to clear the cache
    @Override
    public void clearCache() {
        cache.clear();
        cacheTimestamps.clear();
    }
}