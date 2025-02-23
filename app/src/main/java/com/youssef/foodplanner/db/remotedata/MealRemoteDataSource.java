package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.AreaData;
import com.youssef.foodplanner.model.model.AreaResponse;
import com.youssef.foodplanner.model.model.CategoryResponse;
import com.youssef.foodplanner.model.model.IngredientResponse;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealRemoteDataSource {


    public Single<AreaData>getMealsByArea(String area);
    public Single<AreaData>getMealsByCategory(String category);
    public Single<AreaData> getMealsByIngredients(String ingredient);




    public Single<IngredientResponse> getIngredients();

    public Single<MealResponse> getMealsByIngredient(String ingredient);

    public Single<MealResponse> getMealById(String mealId);

    public Single<MealResponse> getRandomMeal();

    public Single<CategoryResponse> getCategories();
    Single<MealResponse> filterByIngredient( String ingredient);

    public Single<MealResponse> filterByCategory(String category);

    public Single<MealResponse> filterByArea(String area);

    public Single<AreaResponse> getAreas();

    public Single<MealResponse> getAllMeals(String query);


    // <@NonNull T> Observable<T> filterByIngredient(String ingredient);
}