package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.NetworkCallBack;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealResponse;
import java.util.List;

public class home extends Fragment {

    private NavController navController;
    private RecyclerView popularMealsRecyclerView;
    private AllMealsAdapter popularMealsAdapter;
    private View loadingGif;

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


        navController = Navigation.findNavController(view);
        popularMealsRecyclerView = view.findViewById(R.id.rec_view_meals_popular);
        loadingGif = view.findViewById(R.id.loading_gif);


        popularMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularMealsAdapter = new AllMealsAdapter();
        popularMealsRecyclerView.setAdapter(popularMealsAdapter);


        view.findViewById(R.id.favourite).setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_favourite);
        });


        fetchPopularMeals();
    }

    private void fetchPopularMeals() {
        // Show loading GIF
        loadingGif.setVisibility(View.VISIBLE);

        MealRemoteDataSourceImpl.getInstance().makeNetworkCall(new NetworkCallBack() {
            @Override
            public void onSuccess(MealResponse response) {

                loadingGif.setVisibility(View.GONE);


                List<Meal> meals = response.getMeals();
                popularMealsAdapter.setMeals(meals);
                popularMealsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {

                loadingGif.setVisibility(View.GONE);


            }
        });
    }
}