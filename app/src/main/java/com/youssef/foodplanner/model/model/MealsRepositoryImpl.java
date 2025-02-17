package com.youssef.foodplanner.model.model;

import android.util.Log;
import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepositoryImpl implements MealsRepository {
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private static MealsRepositoryImpl repo = null;

    // Caching
    private final Map<String, List<Meal>> cache = new HashMap<>();
    private final long CACHE_EXPIRY_TIME = 10 * 60 * 1000; // 10 minutes
    private final Map<String, Long> cacheTimestamps = new HashMap<>();

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
    public Completable deleteFromFavorites(Meal meal) {
       return localDataSource.deleteFromFavorites(meal);
    }

    @Override
    public Single<List<Meal>> getFavoriteMeals() {
        return localDataSource.getFavoriteMeals()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<AreaData> getMealsByArea(String area) {
        return remoteDataSource.getMealsByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AreaData> getMealsByCategory(String category) {
        return remoteDataSource.getMealsByCategory(category)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<AreaData> getMealsByIngredients(String ingredient) {
        return remoteDataSource.getMealsByIngredients(ingredient).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Meal>> getAllMeals() {
        String cacheKey = "all_meals";
        if (isCacheValid(cacheKey)) {
            return Observable.just(cache.get(cacheKey));
        }

        return remoteDataSource.getAllMeals("")
                .map(MealResponse::getMeals)
                .doOnSuccess(meals -> {
                    cache.put(cacheKey, meals);
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis());
                })
                .toObservable() // Convert Single to Observable AFTER handling caching
                .onErrorReturn(throwable -> {
                    Log.e("MealsRepositoryImpl", "Error fetching all meals", throwable);
                    return new ArrayList<>();
                })
                .subscribeOn(Schedulers.io());
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
    public Observable<List<Meal>> getMealsByIngredient(String ingredient) {
        String cacheKey = "meals_by_ingredient_" + ingredient;
        if (isCacheValid(cacheKey)) {
            return Observable.just(cache.get(cacheKey));
        }

        return remoteDataSource.getMealsByIngredient(ingredient)
                .map(MealResponse::getMeals)
                .doOnSuccess(meals -> {
                    cache.put(cacheKey, meals);
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis());
                })
                .toObservable() // Convert Single to Observable AFTER handling caching
                .onErrorReturn(throwable -> {
                    Log.e("MealsRepositoryImpl", "Error fetching meals by ingredient", throwable);
                    return new ArrayList<>();
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        String cacheKey = "random_meal";
        if (isCacheValid(cacheKey)) {
            return Single.just(new MealResponse(cache.get(cacheKey)));
        }

        return remoteDataSource.getRandomMeal()
                .doOnSuccess(mealResponse -> {
                    cache.put(cacheKey, mealResponse.getMeals());
                    cacheTimestamps.put(cacheKey, System.currentTimeMillis());
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertMeals(Meal meal) {
        return localDataSource.insertMeals(meal)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteAllMeals() {
        return localDataSource.deleteAllMeals()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable addToMealPlan(Meal meal) {
        return localDataSource.insertMealToPlan(meal)
                .subscribeOn(Schedulers.io());
    }




    @Override
    public Completable addToFavourite(Meal meal) {
        meal.setFavorite(true);  // Mark as favorite
        return localDataSource.insertMealToFavorites(meal)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<AreaResponse> getArea() {
        return remoteDataSource.getAreas()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> filterByIngredient(String Ingredient) {
        return null;
    }

    @Override
    public Single<MealResponse> filterByCategory(String Category) {
        return null;
    }

    @Override
    public Single<AreaResponse> filterByArea(String area) {
        return null;
    }

    @Override
    public Single<CategoryResponse> getCategory() {
        return remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<IngredientResponse> getIngredient() {
        return remoteDataSource.getIngredients()
                .subscribeOn(Schedulers.io());
    }

    private boolean isCacheValid(String cacheKey) {
        if (cache.containsKey(cacheKey) && cacheTimestamps.containsKey(cacheKey)) {
            long currentTime = System.currentTimeMillis();
            long cacheTime = cacheTimestamps.get(cacheKey);
            return (currentTime - cacheTime) < CACHE_EXPIRY_TIME;
        }
        return false;
    }

    @Override
    public void clearCache() {
        cache.clear();
        cacheTimestamps.clear();
    }
}
