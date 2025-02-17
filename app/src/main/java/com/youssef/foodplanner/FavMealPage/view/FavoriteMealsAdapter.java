package com.youssef.foodplanner.FavMealPage.view;

import android.content.Context;
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

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> meals;
    private final OnMealClickListener listener;

    public FavoriteMealsAdapter(Context context, OnMealClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.meals = new ArrayList<>();
    }
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.favourite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getMealName() != null ?
                meal.getMealName() : "No Name");

        holder.mealCategory.setText(meal.getCategory() != null ?
                meal.getCategory() : "No Category");

        // Set meal name and category
        holder.mealName.setText(meal.getMealName());
        holder.mealCategory.setText(meal.getCategory());

        // Load image with Glide
        String imageUrl = meal.getMealImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_bg)
                    .error(R.drawable.ic_area)
                    .into(holder.mealImage);
        } else {
            holder.mealImage.setImageResource(R.drawable.ic_favourite);
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void updateMeals(List<Meal> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mealImage;
        final TextView mealName;
        final TextView mealCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.favorite_meal_image);
            mealName = itemView.findViewById(R.id.favorite_meal_name);
            mealCategory = itemView.findViewById(R.id.favorite_meal_category);
        }
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }
}