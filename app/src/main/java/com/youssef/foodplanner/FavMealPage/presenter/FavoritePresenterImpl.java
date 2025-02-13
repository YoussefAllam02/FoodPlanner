package com.youssef.foodplanner.FavMealPage.presenter;

import com.youssef.foodplanner.FavMealPage.view.FavoriteMealsView;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.FavMealPage.presenter.FavouritePresenter;

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
        disposables.add(repository.getAllMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showFavoriteMeals(meals),  // ✅ Show meals in view
                        error -> view.showErrorMessage("Error loading favorite meals") // ✅ Error handling
                ));
    }

    @Override
    public void delete(Meal meal) {
        disposables.add(repository.deleteAllMeals()  // If you have delete logic in repository for meals
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getFavorites(),  // ✅ Refresh favorites list after deletion
                        error -> view.showErrorMessage("Error deleting meal") // ✅ Error handling
                ));
    }

    public void onDestroy() {
        disposables.clear();  // ✅ Clear disposables when presenter is destroyed to avoid memory leaks
    }
}
