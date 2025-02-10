package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;
import com.youssef.foodplanner.model.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("search.php?s=") // Fetches all meals
    Call<MealResponse> getMeals();
}
