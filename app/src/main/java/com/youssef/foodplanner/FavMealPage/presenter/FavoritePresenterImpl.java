package com.youssef.foodplanner.FavMealPage.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.youssef.foodplanner.FavMealPage.view.FavoriteMealsView;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.FavMealPage.presenter.FavouritePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavouritePresenter {
    private FavoriteMealsView view;
    private MealsRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public FavoritePresenterImpl(FavoriteMealsView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getFavorites() {
        disposables.add(repository.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showFavoriteMeals(meals),
                        error -> view.showErrorMessage("Error loading favorite meals")
                ));
    }








    @Override
    public void delete(Meal meal) {
        disposables.add(repository.deleteFromFavorites(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getFavorites(),
                        error -> view.showErrorMessage("Error deleting meal")
                ));
    }

    @Override
    public void deleteFromFavorites(Meal meal) {
        disposables.add(repository.deleteFromFavorites(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getFavorites(), // Refresh list after deletion
                        throwable -> view.showErrorMessage(throwable.getMessage())
                ));
        }
    }







