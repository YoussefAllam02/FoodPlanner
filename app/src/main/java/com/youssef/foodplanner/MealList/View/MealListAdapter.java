package com.youssef.foodplanner.MealList.View;

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
import com.youssef.foodplanner.model.model.Area;
import com.youssef.foodplanner.model.model.AreaData;
import com.youssef.foodplanner.model.model.Meal;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.AreaViewHolder> {
    private final Context context;
    private List<AreaData.MealsDTO> mealList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MealListAdapter(Context context, List<AreaData.MealsDTO> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    public void setMealList(List<AreaData.MealsDTO> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_listi_tem, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        AreaData.MealsDTO meal= mealList.get(position);


        holder.mealName.setText(meal.getStrMeal());

        Glide.with(holder.mealimage.getContext())
                .load(meal.getStrMealThumb())
                .into(holder.mealimage);

        // Handle item click (if needed)
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(meal);

        });


    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        ImageView mealimage;

        TextView mealName;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            mealimage = itemView.findViewById(R.id.random_meal_image); // Correct ID
            mealName = itemView.findViewById(R.id.random_meal_name);  // Correct ID

        }
    }
    public interface OnItemClickListener {
        void onItemClick(AreaData.MealsDTO meal);
    }
}