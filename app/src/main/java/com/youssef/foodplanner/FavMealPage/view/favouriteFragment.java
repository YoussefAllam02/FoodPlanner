package com.youssef.foodplanner.FavMealPage.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.FavMealPage.view.FavoriteMealsAdapter;
import java.util.List;

public class favouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteMealsAdapter adapter;
    private List<Meal> meals;  // List of meals (to be fetched or passed)
    private NavController navController;

    public favouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize meals here, you could pass them from the activity or fetch from a database
        // For example:
        // meals = getArguments().getSerializable("meals");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.fav_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Assuming you have a list of meals to populate in the RecyclerView
        adapter = new FavoriteMealsAdapter(getContext(), meals, meal -> {
            // Handle item click, navigating to a detailed view
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal", meal);  // Passing the clicked meal to the next fragment
            navController.navigate(R.id.action_favourite_to_detailedMeal, bundle);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}
