package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.allmeals.presenter.AllMealsPresenter;
import com.youssef.foodplanner.allmeals.presenter.AllMealsPresenterImpl;
import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class AllMealsActivity extends AppCompatActivity implements AllMealsView, OnMealListener {
    private AllMealsPresenter presenter;
    private ImageView randomMealImage;
    private RecyclerView ingredientMealsRecyclerView;
    private AllMealsAdapter ingredientMealsAdapter;
    private List<Meal> ingredientMealsList;
    private MealsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meals);

        if (savedInstanceState == null) {
            loadHomeFragment();
        }

        // Initialize data sources and repository
        MealRemoteDataSource remoteDataSource = new MealRemoteDataSourceImpl();
        MealLocalDataSource localDataSource = MealLocalDataSourceImpl.getInstance(this);
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);

        // Initialize the presenter
        presenter = new AllMealsPresenterImpl(this, repository);

        // Initialize the ImageView for the random meal
        randomMealImage = findViewById(R.id.randomMealImage);

        // Initialize RecyclerView for meals by ingredient
        ingredientMealsRecyclerView = findViewById(R.id.ingredientMealsRecyclerView);
        ingredientMealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize the list and adapter
        ingredientMealsList = new ArrayList<>();
        ingredientMealsAdapter = new AllMealsAdapter(this, ingredientMealsList, this);
        ingredientMealsRecyclerView.setAdapter(ingredientMealsAdapter);

        // Fetch random meal
        presenter.getRandomMeal();

        // Fetch meals by ingredient (e.g., "chicken")
        presenter.getMealsByIngredient("chicken");
    }

    private void loadHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void showAllProducts(List<Meal> meals) {
        // This method is not needed in this activity since we are using displayMeals()
    }

    @Override
    public void showRandomMeal(Meal meal) {
        // Load the random meal image into the ImageView using Glide
        Glide.with(this)
                .load(meal.getMealImage()) // Assuming getMealImage() returns the image URL
                .into(randomMealImage);

        // Set a click listener for the random meal image
        randomMealImage.setOnClickListener(v -> {
            // Handle click event (e.g., navigate to detailed meal view)
            Toast.makeText(this, "Random meal clicked: " + meal.getMealName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void displayMeals(List<Meal> meals) {
        // Update meals by ingredient list (limit to 10)
        ingredientMealsList.clear();
        ingredientMealsList.addAll(meals.subList(0, Math.min(meals.size(), 10))); // Limit to 10 meals
        ingredientMealsAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }

    @Override
    public void showErrorMessage(String message) {
        // Show an error message to the user
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        // Handle success message if needed
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle favorite product click
        Toast.makeText(this, "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();

        // Delegate the action to the presenter
        presenter.addToFav(meal); // Add to favorites
    }

    @Override
    public void onMealItemClick(Meal meal) {
        // Handle meal item click (e.g., navigate to detailed meal view)
    }
}