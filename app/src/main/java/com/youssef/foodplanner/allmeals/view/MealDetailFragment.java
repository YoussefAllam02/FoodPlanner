package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealsRepository;
import com.youssef.foodplanner.model.model.MealsRepositoryImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailFragment extends Fragment {

    private static final String ARG_MEAL_ID = "mealId";
    private String mealId;
    private MealsRepository repository;

    private ImageView mealImage;
    private ImageView favButton;
    private TextView mealName;
    private TextView mealCategory;
    private TextView mealArea;
    private TextView mealInstructions;
    private LinearLayout mealIngredients;
    private WebView mealYoutube;

    public static MealDetailFragment newInstance(String mealId) {
        Bundle args = new Bundle();
        args.putString(ARG_MEAL_ID, mealId);
        MealDetailFragment fragment = new MealDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getString(ARG_MEAL_ID);
        }

        if (mealId == null || mealId.isEmpty()) {
            Toast.makeText(getContext(), "Invalid meal ID", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }

        // Initialize repository
        MealRemoteDataSourceImpl remoteDataSource = MealRemoteDataSourceImpl.getInstance();
        MealLocalDataSourceImpl localDataSource = MealLocalDataSourceImpl.getInstance(requireContext());
        repository = new MealsRepositoryImpl(remoteDataSource, localDataSource);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_detail, container, false);

        mealImage = view.findViewById(R.id.meal_image);
        mealName = view.findViewById(R.id.meal_name);
        mealCategory = view.findViewById(R.id.meal_category);
        mealArea = view.findViewById(R.id.meal_area);
        mealInstructions = view.findViewById(R.id.meal_instructions);
        mealIngredients = view.findViewById(R.id.meal_ingredients);
        mealYoutube = view.findViewById(R.id.youtubeWebView);
        favButton = view.findViewById(R.id.fav_button);


        fetchMealDetails();

        return view;
    }


    private void fetchMealDetails() {
        repository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> {
                            if (meal != null) {
                                displayMealDetails(meal);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Failed to fetch meal details: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void displayMealDetails(Meal meal) {
        Glide.with(this)
                .load(meal.getMealImage())
                .into(mealImage);

        mealName.setText(meal.getMealName());
        mealCategory.setText(meal.getCategory());
        mealArea.setText(meal.getMealArea());
        mealInstructions.setText(meal.getInstructions());

        // Clear previous ingredients
        mealIngredients.removeAllViews();

        // Get ingredients and measurements
        List<String> ingredients = meal.getIngredients();
        List<String> measurements = meal.getMeasurements();

        // Display paired ingredients and measurements
        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);
            String measure = i < measurements.size() ? measurements.get(i) : "";

            if (!ingredient.isEmpty()) {
                TextView tv = new TextView(getContext());
                tv.setText(String.format("• %s - %s", ingredient, measure));
                tv.setTextSize(16);
                tv.setPadding(8, 4, 8, 4);
                mealIngredients.addView(tv);
            }
        }

        // Load YouTube video
        String youtubeUrl = meal.getStrYoutube();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            String videoId = extractYoutubeVideoId(youtubeUrl);
            if (videoId != null) {
                String embedUrl = "https://www.youtube.com/embed/" + videoId;
                mealYoutube.getSettings().setJavaScriptEnabled(true);
                mealYoutube.getSettings().setDomStorageEnabled(true);
                mealYoutube.setWebChromeClient(new WebChromeClient());
                mealYoutube.setWebViewClient(new WebViewClient());
                mealYoutube.loadUrl(embedUrl);
            }
        }
    }

    // Function to extract YouTube Video ID
    private String extractYoutubeVideoId(String url) {
        String videoId = null;
        if (url.contains("youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            int ampersandIndex = videoId.indexOf("&");
            if (ampersandIndex != -1) {
                videoId = videoId.substring(0, ampersandIndex);
            }
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        }
        return videoId;
    }
}