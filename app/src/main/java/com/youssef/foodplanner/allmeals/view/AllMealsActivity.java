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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String[] trendingIngredients = {"seafood", "beef", "chicken"};
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
        MealRemoteDataSource remoteDataSource = MealRemoteDataSourceImpl.getInstance();
        MealLocalDataSource localDataSource = MealLocalDataSourceImpl.getInstance(this);
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        presenter = new AllMealsPresenterImpl(this, repository);
        randomMealImage = findViewById(R.id.randomMealImage);
        ingredientMealsRecyclerView = findViewById(R.id.ingredientMealsRecyclerView);
        ingredientMealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ingredientMealsList = new ArrayList<>();
        ingredientMealsAdapter = new AllMealsAdapter(this, ingredientMealsList, this);
        ingredientMealsRecyclerView.setAdapter(ingredientMealsAdapter);
        presenter.getRandomMeal();
        presenter.getMealsByIngredient("chicken");
       // myRef=FirebaseDatabase.getInstance().getReference("meal");
    }

    private void loadHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void showAllProducts(List<Meal> meals) {
        //now in frasment
    }

    @Override
    public void showRandomMeal(Meal meal) {
        Glide.with(this)
                .load(meal.getMealImage())
                .into(randomMealImage);

        randomMealImage.setOnClickListener(v -> {
            Toast.makeText(this, "Random meal clicked: " + meal.getMealName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void displayMeals(List<Meal> meals) {
        ingredientMealsList.clear();
        ingredientMealsList.addAll(meals.subList(0, Math.min(meals.size(), 10)));
        ingredientMealsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        // Show an error message to the user
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMessage(String message) {
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle favorite product click
        Toast.makeText(this, "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();
        presenter.addToFav(meal);

    }

    @Override
    public void onMealItemClick(Meal meal) {

    }
    @Override
    public void onRefresh() {

         repository.clearCache();
         fetchData();
    }
    private void fetchData() {
        presenter.getMeals();
        presenter.getRandomMeal();
        int randomIndex = (int) (Math.random() * trendingIngredients.length);
        String selectedIngredient = trendingIngredients[randomIndex];
        presenter.getMealsByIngredient(selectedIngredient);
    }
}