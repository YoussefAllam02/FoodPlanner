package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("Meal")
    Call<MealResponse> getMeals();

}
