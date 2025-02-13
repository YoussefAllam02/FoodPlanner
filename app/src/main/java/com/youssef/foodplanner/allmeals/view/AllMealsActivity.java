package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.List;

public class AllMealsActivity extends AppCompatActivity implements AllMealsView, OnMealListener {
    private AllMealsPresenter presenter;
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
        presenter.getMeals(); // Fetch meals when the activity is created
    }

    private void loadHomeFragment() {
        // Load HomeFragment to display the list of meals
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void showAllProducts(List<Meal> meals) {
        // Show all meals in the HomeFragment (or a different fragment if needed)
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (homeFragment != null) {
            // Assuming HomeFragment has a method to set the meals to a RecyclerView adapter
            homeFragment.displayMeals(meals);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        // Show an error message to the user
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle favorite product click
        Toast.makeText(this, "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();

        // Delegate the action to the presenter
        presenter.addToFav(meal); // Add to favorites
    }
}