package com.youssef.foodplanner.allmeals.presenter;

import android.util.Log;
import com.youssef.foodplanner.allmeals.view.AllMealsView;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class AllMealsPresenterImpl implements AllMealsPresenter {

    private final AllMealsView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AllMealsPresenterImpl(AllMealsView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getMeals() {
        Disposable disposable = repository.getAllMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showAllProducts,
                        throwable -> view.showErrorMessage(throwable.getMessage())
                );
        disposables.add(disposable);
    }


    public void fetchMealsByTrendingIngredients() {
        String[] trendingIngredients = {"seafood", "beef", "chicken"};

        for (String ingredient : trendingIngredients) {
            Disposable disposable = repository.getMealsByIngredient(ingredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            meals -> {
                                if (meals.isEmpty()) {
                                    view.showErrorMessage("No meals found for: " + ingredient);
                                } else {
                                    view.showAllProducts(meals);
                                }
                            },
                            throwable -> view.showErrorMessage(throwable.getMessage())
                    );
            disposables.add(disposable);
        }
    }

    @Override
    public void addToFav(Meal meal) {
        Disposable disposable = repository.addToFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showSuccessMessage("Added to favorites"),
                        throwable -> view.showErrorMessage(throwable.getMessage())
                );
        disposables.add(disposable);
    }

    @Override
    public void addtoPlan(Meal meal) {

    }


    public void addToPlan(Meal meal) {
        Disposable disposable = repository.addToMealPlan(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showSuccessMessage("Added to meal plan"),
                        throwable -> view.showErrorMessage(throwable.getMessage())
                );
        disposables.add(disposable);
    }


    public void onDestroy() {
        disposables.clear();
    }
}