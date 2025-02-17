package com.youssef.foodplanner.allmeals.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailFragment extends Fragment {

    private static final String ARG_MEAL_ID = "mealId";
    private String mealId;
    private MealsRepositoryImpl repository;
    private MealLocalDataSourceImpl localDataSource;
    private CompositeDisposable disposables = new CompositeDisposable();

    private ImageView mealImage;
    private ImageView favButton;
    private ImageView saveButton;
    private TextView mealName;
    private TextView mealCategory;
    private TextView mealArea;
    private TextView mealInstructions;
    private LinearLayout mealIngredients;
    private WebView mealYoutube;
    private Meal currentMeal;

    public static MealDetailFragment newInstance(String mealId) {
        MealDetailFragment fragment = new MealDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEAL_ID, mealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getString(ARG_MEAL_ID);
        }

        MealRemoteDataSourceImpl remoteDataSource = MealRemoteDataSourceImpl.getInstance();
        localDataSource = MealLocalDataSourceImpl.getInstance(requireContext());
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_detail, container, false);
        initializeViews(view);
        fetchMealDetails();
        return view;
    }

    private void initializeViews(View view) {
        mealImage = view.findViewById(R.id.meal_image);
        mealName = view.findViewById(R.id.meal_name);
        mealCategory = view.findViewById(R.id.meal_category);
        mealArea = view.findViewById(R.id.meal_area);
        mealInstructions = view.findViewById(R.id.meal_instructions);
        mealIngredients = view.findViewById(R.id.meal_ingredients);
        mealYoutube = view.findViewById(R.id.youtubeWebView);
        favButton = view.findViewById(R.id.fav_button);
        saveButton = view.findViewById(R.id.save_button);

        favButton.setOnClickListener(v -> toggleFavorite());
        saveButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private void fetchMealDetails() {
        disposables.add(repository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> {
                            currentMeal = meal;
                            displayMealDetails(meal);
                            checkIfFavorite(meal);
                        },
                        throwable -> Toast.makeText(getContext(), "Failed to fetch meal details", Toast.LENGTH_SHORT).show()
                ));
    }

    private void displayMealDetails(Meal meal) {
        Glide.with(this)
                .load(meal.getMealImage())
                .into(mealImage);

        mealName.setText(meal.getMealName());
        mealCategory.setText(meal.getCategory());
        mealArea.setText(meal.getMealArea());
        mealInstructions.setText(meal.getInstructions());

        mealIngredients.removeAllViews();
        List<String> ingredients = meal.getIngredients();
        List<String> measurements = meal.getMeasurements();

        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);
            String measure = (i < measurements.size()) ? measurements.get(i) : "";

            if (!ingredient.isEmpty()) {
                TextView tv = new TextView(getContext());
                tv.setText(String.format("• %s - %s", ingredient, measure));
                tv.setTextSize(16);
                tv.setPadding(8, 4, 8, 4);
                mealIngredients.addView(tv);
            }
        }

        String youtubeUrl = meal.getStrYoutube();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            String videoId = extractYoutubeVideoId(youtubeUrl);
            if (videoId != null) {
                String embedUrl = "https://www.youtube.com/embed/" + videoId;
                mealYoutube.getSettings().setJavaScriptEnabled(true);
                mealYoutube.setWebChromeClient(new WebChromeClient());
                mealYoutube.loadUrl(embedUrl);
            }
        }
    }

    private void checkIfFavorite(Meal meal) {
        disposables.add(localDataSource.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favoriteMeals -> {
                            boolean isFavorite = false;
                            for (Meal favMeal : favoriteMeals) {
                                if (favMeal.getIdMeal().equals(meal.getIdMeal())) {
                                    isFavorite = true;
                                    break;
                                }
                            }
                            favButton.setImageResource(isFavorite ?
                                    R.drawable.ic_favourite : R.drawable.ic_favourite);
                        },
                        error -> Toast.makeText(getContext(), "Error checking favorites", Toast.LENGTH_SHORT).show()
                ));
    }

    private void toggleFavorite() {
        if (currentMeal == null) return;

        disposables.add(repository.addToFavourite(currentMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            favButton.setImageResource(R.drawable.ic_favourite);
                            Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        },
                        error -> Toast.makeText(getContext(), "Failed to add to favorites", Toast.LENGTH_SHORT).show()
                ));
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            saveMealToPlan(selectedDate);
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void saveMealToPlan(String selectedDate) {
        if (currentMeal != null) {
            currentMeal.setDate(selectedDate);
            disposables.add(localDataSource.insertMealToPlan(currentMeal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                Toast.makeText(requireContext(), "Meal added to plan", Toast.LENGTH_SHORT).show();
                                navigateToMealPlan(selectedDate);
                            },
                            throwable -> Toast.makeText(requireContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                    ));
        }
    }

    private void navigateToMealPlan(String selectedDate) {
        Bundle args = new Bundle();
        args.putString("selectedDate", selectedDate);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_mealDetailFragment_to_mealPlanFragment, args);
    }

    private String extractYoutubeVideoId(String url) {
        String videoId = null;
        if (url.contains("youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            int ampersandIndex = videoId.indexOf("&");
            if (ampersandIndex != -1) videoId = videoId.substring(0, ampersandIndex);
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        }
        return videoId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}