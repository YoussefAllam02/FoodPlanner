// MealPlanPresenterImpl.java
package com.youssef.foodplanner.MealPlan.presenter;

import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.MealPlan.view.MealPlanView;
import com.youssef.foodplanner.model.model.Meal;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class MealPlanPresenterImpl implements MealPlanPresenter {

    private final MealPlanView view;
    private final MealLocalDataSource localDataSource;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealPlanPresenterImpl(MealPlanView view, MealLocalDataSource localDataSource) {
        this.view = view;
        this.localDataSource = localDataSource;
    }

    @Override
    public void getMealsForDate(String date) {
        disposables.add(localDataSource.getMealsByDate(date)
                .onErrorReturnItem(new ArrayList<>()) // Handle null cases
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showMeals(meals != null ? meals : new ArrayList<>()),
                        error -> view.showError(error.getMessage())
                ));
    }





    @Override
    public void addMealToPlan(Meal meal, String date) {
        meal.setDate(date);
        disposables.add(localDataSource.insertMealToPlan(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onMealAddedSuccess(),
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    public void dispose() {
        disposables.clear();
    }
}