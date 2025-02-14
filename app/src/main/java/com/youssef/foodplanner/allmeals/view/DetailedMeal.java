package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.remotedata.ApiService;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.model.model.MealResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailedMeal extends Fragment {

    private Meal meal;
    private ApiService apiService;

    private ImageView imgMealDetail;
    private TextView tvMealName, tvCategoryInfo, tvAreaInfo, tvInstructions;
    private WebView webViewYoutube;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the Meal object from arguments safely
        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable("meal");
        }

        // Initialize API service
        apiService = MealRemoteDataSourceImpl.getInstance().create(ApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_meal, container, false);

        // Initialize UI components
        imgMealDetail = view.findViewById(R.id.img_meal_detail);
        tvMealName = view.findViewById(R.id.collapsing_toolbar);
        tvCategoryInfo = view.findViewById(R.id.tv_categoryInfo);
        tvAreaInfo = view.findViewById(R.id.tv_areaInfo);
        tvInstructions = view.findViewById(R.id.tv_instructions);
        webViewYoutube = view.findViewById(R.id.webView_youtube);

        if (meal != null) {
            displayMealDetails();
            fetchMealDetails(meal.getIdMeal());
        } else {
            Toast.makeText(requireContext(), "Meal data is missing!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void displayMealDetails() {
        // Load meal image
        Glide.with(requireContext())
                .load(meal.getMealImage())
                .into(imgMealDetail);

        // Set meal name, category, and area
        tvMealName.setText(meal.getMealName());
        tvCategoryInfo.setText(getString(R.string.category_format, meal.getCategory()));
        tvAreaInfo.setText(getString(R.string.area_format, meal.getMealArea()));

        tvCategoryInfo.setVisibility(View.VISIBLE);
        tvAreaInfo.setVisibility(View.VISIBLE);
    }

    private void fetchMealDetails(String mealId) {
        apiService.getMealDetail(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MealResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull MealResponse mealResponse) {
                        if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                            Meal detailedMeal = mealResponse.getMeals().get(0);

                            tvInstructions.setText(detailedMeal.getInstructions());


                            loadYoutubeVideo(detailedMeal.getStrYoutube());
                        } else {
                            Toast.makeText(requireContext(), "No detailed meal data found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(requireContext(), "Failed to fetch meal details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadYoutubeVideo(String youtubeUrl) {
        if (youtubeUrl != null && youtubeUrl.contains("=")) {
            String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("=") + 1);
            String embedUrl = "https://www.youtube.com/embed/" + videoId;

            WebSettings webSettings = webViewYoutube.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            String iframe = "<html><body style=\"margin:0;padding:0;\"><iframe width=\"100%\" height=\"100%\" src=\"" + embedUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            webViewYoutube.loadData(iframe, "text/html", "utf-8");
        } else {
            Toast.makeText(requireContext(), "No video available", Toast.LENGTH_SHORT).show();
        }
    }
}
