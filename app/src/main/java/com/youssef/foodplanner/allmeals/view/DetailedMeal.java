package com.youssef.foodplanner.allmeals.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.model.model.Meal;

public class DetailedMeal extends Fragment {

    private Meal meal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the Meal object from arguments
        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable("meal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_meal, container, false);
        ImageView imgMealDetail = view.findViewById(R.id.img_meal_detail);
        TextView tvMealName = view.findViewById(R.id.collapsing_toolbar);
        TextView tvCategoryInfo = view.findViewById(R.id.tv_categoryInfo);
        TextView tvAreaInfo = view.findViewById(R.id.tv_areaInfo);
        TextView tvInstructions = view.findViewById(R.id.tv_instructions);
        WebView webViewYoutube = view.findViewById(R.id.webView_youtube);


        if (meal != null) {

            Glide.with(requireContext())
                    .load(meal.getMealImage())
                    .into(imgMealDetail);

            tvMealName.setText(meal.getMealName());
            tvCategoryInfo.setText("Category: " + meal.getCategory());
            tvAreaInfo.setText("Area: " + meal.getMealArea());


            tvInstructions.setText(meal.getInstructions());


            String youtubeUrl = meal.getStrYoutube();
            if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
                String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("=") + 1);
                String embedUrl = "https://www.youtube.com/embed/" + videoId;
                webViewYoutube.getSettings().setJavaScriptEnabled(true);
                webViewYoutube.loadUrl(embedUrl);
            }
        }

        return view;
    }
}