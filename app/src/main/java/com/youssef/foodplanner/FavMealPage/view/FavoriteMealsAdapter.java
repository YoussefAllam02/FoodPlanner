package com.youssef.foodplanner.FavMealPage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.ViewHolder> {
    private Context context;
    private List<Meal> meals;
    private OnMealClickListener listener;

    public FavoriteMealsAdapter(Context context, List<Meal> meals, OnMealClickListener listener) {
        this.context = context;
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.title.setText(meal.getMealName());  // Updated to use getMealName()
        holder.description.setText(meal.getInstructions());  // Updated to use getInstructions()

        Glide.with(context)
                .load(meal.getMealImage())  // Updated to use getMealImage()
                .into(holder.thumbnail);

        holder.constraintLayout.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return (meals != null) ? meals.size() : 0;
    }

    public void setMeals(List<Meal> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title, description;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View v) {
            super(v);
            thumbnail = v.findViewById(R.id.img_meal_detail);
            title = v.findViewById(R.id.meal_name);
            description = v.findViewById(R.id.meal_info);
            constraintLayout = v.findViewById(R.id.details_root);
        }
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);  // Updated to take Meal as a parameter
    }
}
