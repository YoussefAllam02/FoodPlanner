package com.youssef.foodplanner.SearchMeal.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.SearchMeal.Presenter.SearchPresenter;
import com.youssef.foodplanner.SearchMeal.Presenter.SearchPresenterImpl;
import com.youssef.foodplanner.allmeals.view.MealAdapter;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Area;
import com.youssef.foodplanner.model.model.Category;
import com.youssef.foodplanner.model.model.Ingredient;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView {

    private RecyclerView recyclerAreas, recyclerCategories, recyclerIngredients;
    private AreaAdapter areaAdapter;
    private CategoryAdapter categoryAdapter;
    private IngredientAdapter ingredientAdapter;
    private SearchPresenter presenter;
    androidx.appcompat.widget.SearchView searchView;
    private ChipGroup chipGroup;
    private String flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MealsRepositoryImpl repository = new MealsRepositoryImpl(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext()));
        chipGroup = view.findViewById(R.id.chipGroup);
        initializeViews(view);
        setupAdapters();
        hiderecyclerview();
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int selectedId = checkedIds.get(0);
                if (selectedId == R.id.chipAreas) {
                    flag = "area";
                    recyclerAreas.setVisibility(View.VISIBLE);
                    recyclerCategories.setVisibility(View.GONE);
                    recyclerIngredients.setVisibility(View.GONE);
                } else if (selectedId == R.id.chipCategories) {
                    flag = "category";
                    recyclerCategories.setVisibility(View.VISIBLE);
                    recyclerAreas.setVisibility(View.GONE);
                    recyclerIngredients.setVisibility(View.GONE);
                } else if (selectedId == R.id.chipIngredients) {
                    flag = "ingredient";
                    recyclerIngredients.setVisibility(View.VISIBLE);
                    recyclerAreas.setVisibility(View.GONE);
                    recyclerCategories.setVisibility(View.GONE);
                }
            } else  {flag=null;

            }
        });
        presenter = new SearchPresenterImpl(this, repository);
        presenter.loadAreas();
        presenter.loadCategories();
        presenter.loadIngredients();


    }

    private void initializeViews(View view) {
        recyclerAreas = view.findViewById(R.id.AreaRecyclarView);
        recyclerCategories = view.findViewById(R.id.CatagoriesRecyclerView);
        recyclerIngredients = view.findViewById(R.id.ingredientsRecyclerView);
        searchView=view.findViewById(R.id.search_view);
    }

    private void hiderecyclerview() {
        recyclerAreas.setVisibility(View.GONE);
        recyclerCategories.setVisibility(View.GONE);
        recyclerIngredients.setVisibility(View.GONE);
    }

    private void setupAdapters() {

        areaAdapter = new AreaAdapter(getContext(), new ArrayList<>());
        recyclerAreas.setAdapter(areaAdapter);
        areaAdapter.setOnItemClickListener(position -> {
            SearchFragmentDirections.ActionSearchFragmentToMealListFragment action = SearchFragmentDirections
                    .actionSearchFragmentToMealListFragment(position.getStrArea(), "area");
            Toast.makeText(getContext(), position.getStrArea(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(action);
        });
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        recyclerCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(position -> {
            SearchFragmentDirections.ActionSearchFragmentToMealListFragment action = SearchFragmentDirections
                    .actionSearchFragmentToMealListFragment(position.getStrCategory(), "category");
            Navigation.findNavController(getView()).navigate(action);
        });
        ingredientAdapter = new IngredientAdapter(getContext(), new ArrayList<>());
        recyclerIngredients.setAdapter(ingredientAdapter);
        ingredientAdapter.setOnItemClickListener(position -> {
            SearchFragmentDirections.ActionSearchFragmentToMealListFragment action = SearchFragmentDirections
                    .actionSearchFragmentToMealListFragment(position.getStrIngredient(), "ingredient");
            Navigation.findNavController(getView()).navigate(action);
        });
    }


    @Override
    public void showAreas(List<Area> areas) {
        areaAdapter.setAreaList(areas);
        onClick();
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategoryList(categories);
        onClick();
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setIngredientList(ingredients);
        onClick();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            ((SearchPresenterImpl) presenter).onDestroy();
        }
    }

    private void onClick() {
    searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (flag != null) {
              if (flag.equals("area")) {
                  presenter.SearchOnArea(newText);
              } else if (flag.equals("category")) {
                  presenter.SearchOnCategory(newText);
              } else if (flag.equals("ingredient")) {
                  presenter.SearchOnIngrdient(newText);
              }
            }
            return false;
        }
    });
    }
}




