package com.youssef.foodplanner.allmeals.view;

import android.content.Context;
import android.util.Log;
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

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> meals;
    private OnMealListener onMealListener;

    public MealAdapter(Context context, List<Meal> meals, OnMealListener onMealListener) {
        this.context = context;
        this.meals = (meals != null) ? meals : new ArrayList<>();  // Ensure the list is not null
        this.onMealListener = onMealListener;
    }


    public interface OnMealListener {
        void onMealItemClick(Meal meal);
        void onFavProductClick(Meal meal);
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyItemRangeChanged(0, Math.min(meals.size(),10));
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

        holder.mealCategory.setText(meal.getCategory());
        holder.mealName.setText(meal.getMealName());
   holder.MealArea.setText(meal.getMealArea());

        Glide.with(holder.mealImage.getContext())
                .load(meal.getMealImage())
                .into(holder.mealImage);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (onMealListener != null) {
                onMealListener.onMealItemClick(meal);
            }
        });

        // Handle favorite button click (if exists)
        if (holder.favButton != null) {
            holder.favButton.setOnClickListener(v -> {
                if (onMealListener != null) {
                    onMealListener.onFavProductClick(meal);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealCategory;
        TextView mealName;

        TextView MealArea;


        ImageView mealImage;
        ImageView favButton;

        public MealViewHolder(@NonNull View itemView, OnMealListener listener) {
            super(itemView);
            mealCategory = itemView.findViewById(R.id.meal_category);
            mealImage = itemView.findViewById(R.id.img_meal);
            MealArea = itemView.findViewById(R.id.meal_area);
            mealName = itemView.findViewById(R.id.meal_name);


           // favButton = itemView.findViewById(R.id.img_fav); // Ensure this ID exists in your layout
        }
    }
}