package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.allmeals.presenter.AllMealsPresenterImpl;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView, OnMealListener {

    private NavController navController;
    private List<Meal> ingredientMealsList;
    private RecyclerView ingredientMealsRecyclerView;
    MealAdapter Adapter;
    private AllMealsAdapter popularMealsAdapter;
    private ImageView randomMealImage;

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

        // Initialize the ImageView for the random meal
        randomMealImage = view.findViewById(R.id.randomMealImage);

        // Setup RecyclerView for Trending Meals (Horizontal)
        ingredientMealsRecyclerView = view.findViewById(R.id.ingredientMealsRecyclerView);
        ingredientMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Fetch random meal
        presenter.getRandomMeal();

        // Fetch meals by ingredient (e.g., "chicken")
        presenter.getMealsByIngredient("chicken");
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
        // Setup adapter for trending meals
        popularMealsAdapter = new AllMealsAdapter(requireContext(), meals, this);
        ingredientMealsRecyclerView.setAdapter(popularMealsAdapter);
        Adapter=new MealAdapter(requireContext(),meals,this);
       // ingredientMealsRecyclerView.setAdapter(Adapter);
        Log.d("HomeFragment", "Loaded " + meals.size() + " meals.");
    }

    @Override
    public void showRandomMeal(Meal meal) {
        // Load the random meal image into the ImageView using Glide
        Glide.with(this)
                .load(meal.getMealImage()) // Assuming getMealImage() returns the image URL
                .into(randomMealImage);

        // Set a click listener for the random meal image
        randomMealImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal", meal);
            navController.navigate(R.id.action_home_to_detailedMeal, bundle);
        });
    }

    public void displayMeals(List<Meal> meals) {
        // Update meals by ingredient list (limit to 10)
        ingredientMealsList.clear();
        meals.addAll(meals.subList(0, Math.min(meals.size(), 10))); // Limit to 10 meals
        popularMealsAdapter.notifyDataSetChanged();
    }
    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        // Handle success message if needed
    }

    @Override
    public void onMealItemClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        navController.navigate(R.id.action_home_to_detailedMeal, bundle);
    }
}