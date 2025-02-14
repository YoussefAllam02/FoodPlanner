package com.youssef.foodplanner.allmeals.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.R;

import java.util.List;

public class AllMealsAdapter extends RecyclerView.Adapter<AllMealsAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> meals;
    private OnMealListener onMealListener;

    public AllMealsAdapter(Context context, List<Meal> meals, OnMealListener onMealListener) {
        Log.d("Meal", "AllMealsAdapter constructor calledsize"+meals.size());
        this.context = context;
        this.meals = meals;
        this.onMealListener = onMealListener;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meals, parent, false);
        return new MealViewHolder(view, onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        Log.d("Meal", "Meal Name: " + meal.getMealName());

       // holder.mealName.setText(meal.getMealName());
        holder.mealCategory.setText(meal.getCategory());
      //  holder.areainfo.setText(meal.getMealArea());
        //holder.webview.loadUrl(meal.getStrYoutube());
        Glide.with(holder.mealImage.getContext())
                .load(meal.getMealImage())
                .into(holder.mealImage);


        holder.itemView.setOnClickListener(v -> onMealListener.onFavProductClick(meal)); // Use onFavProductClick
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealCategory, areainfo;
        ImageView mealImage;
        WebView webview;

        public MealViewHolder(@NonNull View itemView, OnMealListener onMealListener) {
            super(itemView);

            mealCategory = itemView.findViewById(R.id.meal_category);
            mealImage = itemView.findViewById(R.id.img_meal);

        }
    }
}