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
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;

import java.util.List;

public class HomeFragment extends Fragment implements  AllMealsView, OnMealListener {

    private NavController navController;
    private RecyclerView popularMealsRecyclerView;
    private AllMealsAdapter popularMealsAdapter;
    private View loadingGif;

    AllMealsPresenterImpl presenter;


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

         presenter=new AllMealsPresenterImpl(this,new MealsRepositoryImpl(
                 MealRemoteDataSourceImpl.getInstance(),
                 MealLocalDataSourceImpl.getInstance(requireContext())
         ));

        navController = Navigation.findNavController(view);
        popularMealsRecyclerView = view.findViewById(R.id.rec_view_meals_popular);
        loadingGif = view.findViewById(R.id.loading_gif);
        //popularMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter.getMeals();
    }

    @Override
    public void onFavProductClick(Meal meal) {
        // Handle favorite product click
        Toast.makeText(requireContext(), "Added to favorites: " + meal.getMealName(), Toast.LENGTH_SHORT).show();

        // Navigate to the DetailedMealFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        navController.navigate(R.id.action_home_to_detailedMeal, bundle);
    }

    @Override
    public void showAllProducts(List<Meal> meals) {
        popularMealsAdapter = new AllMealsAdapter(requireContext(), meals, this);
        popularMealsRecyclerView.setAdapter(popularMealsAdapter);
        Log.d("mealas", "Number of meals: " + meals.size());
        if (popularMealsAdapter != null) {
            popularMealsAdapter.setMeals(meals);
        }
        if (loadingGif != null) {
            loadingGif.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(String message) {

    }
}