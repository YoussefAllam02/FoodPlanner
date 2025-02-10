package com.youssef.foodplanner.allmeals.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.youssef.foodplanner.model.model.Meal;
import com.youssef.foodplanner.R;
import java.util.List;

public class AllMealsAdapter extends RecyclerView.Adapter<AllMealsAdapter.MealViewHolder> {

    private List<Meal> meals;
    private OnMealClickListener onMealClickListener;

    public AllMealsAdapter(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meals, parent, false);
        return new MealViewHolder(view, onMealClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getMealName());
        holder.mealCategory.setText(meal.getCategory());
        Glide.with(holder.itemView.getContext())
                .load(meal.getMealImage())
                .into(holder.mealImage);
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mealName, mealCategory;
        ImageView mealImage;
        OnMealClickListener onMealClickListener;

        public MealViewHolder(@NonNull View itemView, OnMealClickListener onMealClickListener) {
            super(itemView);
            this.onMealClickListener = onMealClickListener;
            mealName = itemView.findViewById(R.id.meal_name);
            mealCategory = itemView.findViewById(R.id.meal_category);
            mealImage = itemView.findViewById(R.id.meal_image);

            // Set click listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMealClickListener.onMealClick(getAdapterPosition());
        }
    }

    public interface OnMealClickListener {
        void onMealClick(int position);
    }
}