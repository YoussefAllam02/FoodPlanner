package com.youssef.foodplanner.allmeals.presenter;

import android.util.Log;

import com.youssef.foodplanner.allmeals.view.AllMealsView;
import com.youssef.foodplanner.db.remotedata.NetworkCallBack;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealResponse;
import com.youssef.foodplanner.model.model.MealsRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllMealsPresenterImpl implements AllMealsPresenter, NetworkCallBack {
    private AllMealsView view;
    private MealsRepository repository;

    public AllMealsPresenterImpl(AllMealsView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }


    public void getMeals() {

        repository.getAllMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // Observe on the main thread
                .subscribe(meals -> view.showAllProducts(meals));
    }

    @Override
    public void addToFav(Meal meal) {
        repository.addtoFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void addtoPlan(Meal meal) {

        repository.addtoMealPlan(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    // onSuccess method that is properly placed in the class
    public void onSuccess(MealResponse response) {
        if (response != null && response.getMeals() != null) {
            view.showAllProducts(response.getMeals());
        } else {
            view.showErrorMessage("No products available.");
        }
    }



    public void onFailure(Throwable error) {
        Log.e("Error", "Failed to fetch products: " + error.getMessage());
        view.showErrorMessage("Failed to load products.");
    }


    @Override
    public void onFailure(String errorMessage) {
        Log.e("Error", errorMessage);
        view.showErrorMessage(errorMessage);
    }
}
