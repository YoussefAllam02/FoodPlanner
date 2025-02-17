package com.youssef.foodplanner.allmeals.presenter;

import com.youssef.foodplanner.allmeals.view.MealDetailView;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailPresenterImpl implements MealDetailPresenter {

    private final MealDetailView view;
    private final MealsRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailPresenterImpl(MealDetailView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getMealById(String mealId) {
        disposables.add(repository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showMealDetails,
                        throwable -> view.showErrorMessage("Failed to load meal details")
                ));
    }

    @Override
    public void checkIfFavorite(String mealId) {
        disposables.add(repository.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favoriteMeals -> {
                            boolean isFavorite = false;
                            for (Meal meal : favoriteMeals) {
                                if (meal.getIdMeal().equals(mealId)) {
                                    isFavorite = true;
                                    break;
                                }
                            }
                            view.updateFavoriteStatus(isFavorite);
                        },
                        throwable -> view.showErrorMessage("Error checking favorites")
                ));
    }

    @Override
    public void toggleFavorite(Meal meal) {
        disposables.add(repository.addToFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.updateFavoriteStatus(true),
                        throwable -> view.showErrorMessage("Failed to toggle favorite")
                ));
    }

    @Override
    public void dispose() {
        disposables.clear();
    }
}