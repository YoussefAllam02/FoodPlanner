package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
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
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public class HomeFragment extends Fragment implements OnMealListener {

    private NavController navController;
    private RecyclerView popularMealsRecyclerView;
    private AllMealsAdapter popularMealsAdapter;
    private View loadingGif;
    private List<Meal> meals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize NavController
        navController = Navigation.findNavController(view);

        // Initialize RecyclerView and Adapter
        popularMealsRecyclerView = view.findViewById(R.id.rec_view_meals_popular);
        loadingGif = view.findViewById(R.id.loading_gif);

        popularMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularMealsAdapter = new AllMealsAdapter(requireContext(), meals, this); // Pass 'this' as the OnMealListener
        popularMealsRecyclerView.setAdapter(popularMealsAdapter);





    }

    public void displayMeals(List<Meal> meals) {
        this.meals = meals; // Set the meals data
        if (popularMealsAdapter != null) {
            popularMealsAdapter.setMeals(meals); // Notify adapter to refresh the RecyclerView
        }
        if (loadingGif != null) {
            loadingGif.setVisibility(View.GONE); // Hide loading gif once data is loaded
        }
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle favorite product click
        Toast.makeText(requireContext(), "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();

        // Navigate to the DetailedMealFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        navController.navigate(R.id.action_home_to_detailedMeal, bundle); // Use the correct action ID
    }
}