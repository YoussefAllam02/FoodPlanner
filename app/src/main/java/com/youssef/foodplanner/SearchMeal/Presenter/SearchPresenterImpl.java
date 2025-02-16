package com.youssef.foodplanner.SearchMeal.Presenter;


import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.Area;
import com.youssef.foodplanner.model.model.Category;
import com.youssef.foodplanner.model.model.Ingredient;
import com.youssef.foodplanner.SearchMeal.View.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter {
    private final MealsRepository mealRepository;
    private final SearchView searchView;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private List<Area> arealist;
    private List<Category> categorylist;
    private List<Ingredient> ingredientList;

    public SearchPresenterImpl(SearchView searchView, MealsRepository mealRepository) {
        this.searchView = searchView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void loadAreas() {
        disposables.add(mealRepository.getArea()
                .subscribeOn(Schedulers.io()).map(v -> {
                    return v.getArea();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> {
                            arealist = new ArrayList<>(areas);
                            searchView.showAreas(areas);

                        },
                        error -> searchView.showError(error.getMessage())
                ));
    }

    @Override
    public void loadCategories() {
        disposables.add(mealRepository.getCategory()
                .subscribeOn(Schedulers.io()).map(v -> {
                    return v.getCategories();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            categorylist = new ArrayList<>(categories);
                            searchView.showCategories(categories);
                        },
                        error -> searchView.showError(error.getMessage())
                ));
    }

    @Override
    public void loadIngredients() {
        disposables.add(mealRepository.getIngredient()
                .subscribeOn(Schedulers.io()).map(v -> {
                    return v.getIngredient();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients ->
                        {
                            ingredientList = new ArrayList<>(ingredients);
                            searchView.showIngredients(ingredients);
                        },
                        error -> searchView.showError(error.getMessage())
                ));
    }

    @Override
    public void SearchOnIngrdient(String query) {
        Observable<Ingredient> ingredientObservable = Observable.fromIterable(ingredientList);
        ingredientObservable
                .filter(ingredient -> ingredient.getStrIngredient().toLowerCase().contains(query.toLowerCase()))
                .toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(filteredIngredients -> searchView.showIngredients(filteredIngredients));

    }

    @Override
    public void SearchOnArea(String query) {
       Observable<Area> areaObservable = Observable.fromIterable(arealist);
       areaObservable.filter(area -> area.getStrArea().toLowerCase().contains(query.toLowerCase()))
                .toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(filteredAreas -> searchView.showAreas(filteredAreas));
    }

    @Override
    public void SearchOnCategory(String query) {
    Observable<Category> categoryObservable = Observable.fromIterable(categorylist);
    categoryObservable.filter(category -> category.getStrCategory().toLowerCase().contains(query.toLowerCase()))
            .toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(filteredCategories -> searchView.showCategories(filteredCategories));


    }


    public void onDestroy() {
        disposables.clear();
    }
}