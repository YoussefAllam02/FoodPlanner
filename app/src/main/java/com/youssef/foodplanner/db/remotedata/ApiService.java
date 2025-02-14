package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.allmeals.view.DetailedMeal;
import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient();

    @GET("search.php")
    Single<MealResponse> searchMeal(@Query("s") String mealName);

    @GET("search.php")
    Single<MealResponse> listMealsByFirstLetter(@Query("f") String letter);

    @GET("lookup.php")
    Single<DetailedMeal> getMealDetails(@Query("i") int mealId);

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

//    @GET("categories.php")
//    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> filterByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> filterByArea(@Query("a") String area);
    @GET("search.php")
    Single<MealResponse> getAllMeals(@Query("s") String query);
}
