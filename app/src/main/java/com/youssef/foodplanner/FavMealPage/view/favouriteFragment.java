package com.youssef.foodplanner.FavMealPage.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youssef.foodplanner.FavMealPage.presenter.FavoritePresenterImpl;
import com.youssef.foodplanner.FavMealPage.presenter.FavouritePresenter;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class favouriteFragment extends Fragment implements FavoriteMealsView, FavoriteMealsAdapter.OnMealClickListener {

    private RecyclerView recyclerView;
    private FavoriteMealsAdapter adapter;
    private NavController navController;
    private MealsRepository repository;
    private FavouritePresenter presenter;

    public favouriteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize repository with proper data sources
        MealRemoteDataSourceImpl remoteDataSource = MealRemoteDataSourceImpl.getInstance();
        MealLocalDataSourceImpl localDataSource = MealLocalDataSourceImpl.getInstance(requireContext());
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);

        presenter = new FavoritePresenterImpl(this, repository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.fav_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getFavorites();
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        if (meals.isEmpty()) {

        } else {
            adapter = new FavoriteMealsAdapter(getContext(),this);
            adapter.setMeals(meals);
            recyclerView.setAdapter(adapter);
            adapter.setMeals(meals != null ? meals : new ArrayList<>());
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMealClick(Meal meal) {
        Log.d("MealClick", "Meal clicked: " + meal.getMealName());
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.getIdMeal()+"");
        navController.navigate(R.id.action_favourite_to_detailedMeal, bundle);
    }

    @Override
    public void onRemoveClick(Meal meal) {
        presenter.deleteFromFavorites(meal);
    }
}