package com.youssef.foodplanner.MealList.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.youssef.foodplanner.MealList.Presenter.MealListPresenter;
import com.youssef.foodplanner.MealList.Presenter.MealListPresenterImpl;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Area;
import com.youssef.foodplanner.model.model.AreaData;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class MealListFragment extends Fragment implements MealListView {
    private RecyclerView recyclerMealList;
    private MealListAdapter adapter;
    private MealListPresenter presenter;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerMealList = view.findViewById(R.id.meallist);
        searchView = view.findViewById(R.id.search_view);

        // Set up RecyclerView
        recyclerMealList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealListAdapter(getContext(), new ArrayList<>());
        recyclerMealList.setAdapter(adapter);

        MealsRepositoryImpl repository = new MealsRepositoryImpl(
                MealRemoteDataSourceImpl.getInstance(),
                MealLocalDataSourceImpl.getInstance(getContext()));
        presenter = new MealListPresenterImpl(this, repository);


        loadInitialData();
    }



    private void loadInitialData() {
            MealListFragmentArgs args = MealListFragmentArgs.fromBundle(getArguments());
            String value=args.getName();
            String type=args.getType();
        Log.d("TAG", "loadInitialData:  "+type+value);
            if (type != null && value != null) {
                switch (type) {
                    case "area":
                        presenter.getmealsByArea(value);
                        break;
                    case "category":
                        presenter.getmealsByCategory(value);
                        break;
                    case "ingredient":
                        presenter.getmealsByIngredient(value);
                        break;
                }
            }
        }
    @Override
    public void show_meals_Areas(List<AreaData.MealsDTO> meals) {

        adapter.setMealList(meals);
        adapter.notifyDataSetChanged();
        onClick();
    }

    @Override
    public void show_meals_Categories(List<AreaData.MealsDTO> meals) {
        adapter.setMealList(meals);
        adapter.notifyDataSetChanged();
        onClick();

    }

    @Override
    public void show_meals_Ingredients(List<AreaData.MealsDTO> meals) {
        adapter.setMealList(meals);
        adapter.notifyDataSetChanged();
        onClick();

    }

    @Override
    public void showSearchResults(List<Meal> meals) {
//        adapter.setMealList(meals);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onClick(){
        adapter.setOnItemClickListener(new MealListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AreaData.MealsDTO meal) {
                Navigation.findNavController(getView())
                        .navigate(MealListFragmentDirections
                                .actionMealListFragmentToDetailedMealFragment(meal.getIdMeal()));
            }});
    }
}