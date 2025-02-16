package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
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

public class HomeFragment extends Fragment implements AllMealsView {
    private Meal currentRandomMeal;
    private AllMealsPresenterImpl presenter;
    private MealAdapter mealAdapter;
    private RecyclerView mealsRecyclerView;
    private ImageView randomMealImage;
    private TextView randomMealCategory;
    private TextView randomMealName;
    private MealsRepository repository;
    private String[] trendingIngredients = {"seafood", "beef", "chicken"};
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        View randomMealContainer = view.findViewById(R.id.random_meal_container);
        randomMealImage = view.findViewById(R.id.random_meal_image);
        randomMealName = view.findViewById(R.id.random_meal_name);
        randomMealCategory = view.findViewById(R.id.random_meal_category);
        randomMealImage = view.findViewById(R.id.random_meal_image);
        randomMealCategory = view.findViewById(R.id.random_meal_category);
        randomMealName = view.findViewById(R.id.random_meal_name);
        mealsRecyclerView = view.findViewById(R.id.meals_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        MealLocalDataSource localDataSource = new MealLocalDataSourceImpl(requireContext());
        MealRemoteDataSource remoteDataSource = MealRemoteDataSourceImpl.getInstance();
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        presenter = new AllMealsPresenterImpl(this, repository);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            onRefresh(); // Call onRefresh to clear cache and fetch new data
            swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
        });
        randomMealContainer.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                Bundle bundle = new Bundle();
                bundle.putString("mealId", currentRandomMeal.getIdMeal());
                Navigation.findNavController(v)
                        .navigate(R.id.action_home_to_detailedMeal, bundle);
            }
        });
        mealAdapter = new MealAdapter(getContext(), new ArrayList<>(), new MealAdapter.OnMealListener() {
            @Override
            public void onMealItemClick(Meal meal) {

                Bundle bundle = new Bundle();
                bundle.putString("mealId", meal.getIdMeal());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_home_to_detailedMeal, bundle);

            }

            @Override
            public void onFavProductClick(Meal meal) {
                presenter.addToFav(meal);

            }
        });

//                new MealAdapter() {
//            @Override
//            public void onMealItemClick(Meal meal) {
//                // Navigate to MealDetailFragment with meal ID
//                Bundle bundle = new Bundle();
//                bundle.putString("mealId", meal.getIdMeal()); // Use the meal's ID
//                Navigation.findNavController(requireView())
//                        .navigate(R.id.action_home_to_detailedMeal, bundle);
//
//            }
//
//            @Override
//            public void onFavProductClick(Meal meal) {
//                presenter.addToFav(meal);
//            }
//        });


        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mealsRecyclerView.setAdapter(mealAdapter);

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchData();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Schedule the task to fetch a new random meal every 20 seconds
        runnable = new Runnable() {
            @Override
            public void run() {
                presenter.getRandomMeal();
                handler.postDelayed(this, 10000); // 20 seconds
            }
        };
        handler.postDelayed(runnable, 10000); // Initial delay

        return view;
    }

    private void fetchData() {
        presenter.getMeals();
        presenter.getRandomMeal();
        int randomIndex = (int) (Math.random() * trendingIngredients.length);
        String selectedIngredient = trendingIngredients[randomIndex];
        Log.d("HomeFragment", "Fetching new meals for ingredient: " + selectedIngredient);
        presenter.getMealsByIngredient(selectedIngredient);
    }


    @Override
    public void showAllProducts(List<Meal> meals) {
        mealAdapter.setMeals(meals);
    }

    @Override
    public void displayMeals(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            Log.d("HomeFragment", "Updating UI with meals: " + meals.size());
            mealAdapter.setMeals(meals); // Update the adapter
        } else {
            Log.d("HomeFragment", "No meals found");
            showErrorMessage("No meals found");
        }
    }

    @Override
    public void showRandomMeal(Meal meal) {
        currentRandomMeal = meal;
        if (meal.getMealImage() != null) {
            Glide.with(this)
                    .load(meal.getMealImage())
                    .into(randomMealImage);
        }
        randomMealName.setText(meal.getMealName());

        randomMealCategory.setText(meal.getCategory());
    }

    @Override
    public void onRefresh() {
        repository.clearCache();
        Log.d("HomeFragment", "Cache cleared");
        fetchData();
    }


    @Override
    public void showErrorMessage(String message) {
        Log.e("HomeFragment", "Error: " + message);
    }

    @Override
    public void showSuccessMessage(String message) {
        Log.d("HomeFragment", "Success: " + message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        handler.removeCallbacks(runnable);
    }
}