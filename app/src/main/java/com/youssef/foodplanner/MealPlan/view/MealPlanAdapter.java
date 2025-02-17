// MealPlanAdapter.java
package com.youssef.foodplanner.MealPlan.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.model.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {

    private List<Meal> meals;
    private OnMealPlanClickListener listener;

    public MealPlanAdapter(List<Meal> meals, OnMealPlanClickListener listener) {
        this.meals = meals != null ? meals : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_plan_item, parent, false);
        return new MealPlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        Meal meal = meals.get(position);

        // Bind meal data to views
        holder.mealName.setText(meal.getMealName());
        holder.mealCategory.setText(meal.getCategory());
        holder.mealArea.setText(meal.getMealArea());

        Glide.with(holder.itemView.getContext())
                .load(meal.getMealImage())
                .into(holder.mealImage);

        // Handle meal click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealPlanClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    public void updateMeals(List<Meal> newMeals) {
        if (this.meals == null) {
            this.meals = new ArrayList<>();
        }
        this.meals.clear();
        if (newMeals != null) {
            this.meals.addAll(newMeals);
        }
        notifyDataSetChanged();
    }
    static class MealPlanViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage;
        TextView mealName, mealCategory, mealArea;

        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.meal_image);
            mealName = itemView.findViewById(R.id.meal_name);
            mealCategory = itemView.findViewById(R.id.meal_category);
            mealArea = itemView.findViewById(R.id.meal_area);
        }
    }
}