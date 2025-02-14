package com.youssef.foodplanner.allmeals.view;

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

import com.youssef.foodplanner.R;
import com.youssef.foodplanner.allmeals.presenter.AllMealsPresenterImpl;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView, OnMealListener {

    private NavController navController;
    private RecyclerView popularMealsRecyclerView;
    private AllMealsAdapter popularMealsAdapter;

    // RecyclerView & adapter for random meals (horizontal list)
    private RecyclerView randomMealsRecyclerView;
    private MealAdapter randomMealAdapter;

    private View loadingGif;
    private AllMealsPresenterImpl presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize non-view related logic here if needed.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate your fragment layout (make sure it includes both RecyclerViews)
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new AllMealsPresenterImpl(
                this,
                new MealsRepositoryImpl(
                        MealRemoteDataSourceImpl.getInstance(),
                        MealLocalDataSourceImpl.getInstance(requireContext())
                )
        );

        navController = Navigation.findNavController(view);


        popularMealsRecyclerView = view.findViewById(R.id.rec_view_meals_popular);
        randomMealsRecyclerView = view.findViewById(R.id.rv_random_meals);
        loadingGif = view.findViewById(R.id.loading_gif);

        // Set layout managers
        popularMealsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        randomMealsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter.getMeals();
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle click event for favorite meals
        Toast.makeText(requireContext(), "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();


        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        navController.navigate(R.id.action_home_to_favourite, bundle);
    }


    @Override
    public void showAllProducts(List<Meal> meals) {
        // Setup adapter for popular meals (vertical RecyclerView)
        popularMealsAdapter = new AllMealsAdapter(requireContext(), meals, this);
        popularMealsRecyclerView.setAdapter(popularMealsAdapter);

        // For random meals, pick a subset (e.g., first 5 items)
        List<Meal> randomMeals = meals.subList(0, Math.min(5, meals.size()));
        randomMealAdapter = new MealAdapter(requireContext(), randomMeals, new OnMealListener() {
            @Override
            public void onFavProductClick(Meal meal) {
                // Handle favorite click if needed
            }

            @Override
            public void onMealItemClick(Meal meal) {
                Toast.makeText(requireContext(), "Random meal clicked: " + meal.getMealName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set the adapter for the horizontal RecyclerView
        randomMealsRecyclerView.setAdapter(randomMealAdapter);

        Log.d("HomeFragment", "Loaded " + meals.size() + " meals.");
    }
    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {

    }

    @Override
    public void onMealItemClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        navController.navigate(R.id.action_home_to_detailedMeal, bundle);
    }
}
