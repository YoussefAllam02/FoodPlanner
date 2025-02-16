package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.AreaResponse;
import com.youssef.foodplanner.model.model.CategoryResponse;
import com.youssef.foodplanner.model.model.IngredientResponse;
import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();
    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient(@Query("i") String ingredient);
    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String mealId);



    @GET("random.php")
    Single<MealResponse> getRandomMeal();

  @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<AreaResponse> filterByArea(@Query("a") String area);

    @GET("list.php?a=list")
    Single<AreaResponse> getAreas();
    @GET("search.php")
    Single<MealResponse> getAllMeals(@Query("s") String query);
}
