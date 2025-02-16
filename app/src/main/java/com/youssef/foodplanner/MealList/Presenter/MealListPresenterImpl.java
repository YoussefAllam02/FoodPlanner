package com.youssef.foodplanner.MealList.Presenter;

import com.youssef.foodplanner.MealList.View.MealListView;
import com.youssef.foodplanner.SearchMeal.View.SearchView;
import com.youssef.foodplanner.model.model.AreaData;
import com.youssef.foodplanner.model.model.MealsRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealListPresenterImpl implements MealListPresenter {
    private final MealsRepository mealRepository;
    private final MealListView mealListView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealListPresenterImpl(MealListView mealListView, MealsRepository mealRepository) {
        this.mealListView = mealListView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getmealsByIngredient(String ingredient) {
        mealRepository.getMealsByIngredients(ingredient).subscribe(new SingleObserver<AreaData>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull AreaData areaData) {
                mealListView.show_meals_Ingredients(areaData.getMeals());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

    }

    @Override
    public void getmealsByArea(String area) {
      mealRepository.getMealsByArea(area).subscribe(new SingleObserver<AreaData>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {

          }

          @Override
          public void onSuccess(@NonNull AreaData areaData) {
              mealListView.show_meals_Areas(areaData.getMeals());
          }

          @Override
          public void onError(@NonNull Throwable e) {

          }
      });
    }

    @Override
    public void getmealsByCategory(String category) {
        mealRepository.getMealsByCategory(category).subscribe(new SingleObserver<AreaData>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull AreaData areaData) {
                mealListView.show_meals_Categories(areaData.getMeals());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });


    }
}




