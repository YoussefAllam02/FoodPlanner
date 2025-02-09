package com.youssef.foodplanner.db.remotedata;

import com.youssef.foodplanner.model.model.MealResponse;

public interface NetworkCallBack {
    void onSuccess(MealResponse mealResponse);
    void onFailure(String errorMessage);

}
