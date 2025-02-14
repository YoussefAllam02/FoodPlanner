package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;
import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("random.php")
    Observable<MealResponse> getMeals();
    @GET("lookup.php")
    Single<MealResponse> getMealDetail(@Query("i") String id);

}
