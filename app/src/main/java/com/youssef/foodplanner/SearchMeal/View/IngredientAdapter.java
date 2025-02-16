package com.youssef.foodplanner.SearchMeal.View;

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
import com.youssef.foodplanner.model.model.Ingredient; // Import Ingredient model

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final Context context;
    private List<Ingredient> ingredientList; // Use Ingredient list
    private  onItemClickListener onItemClickListener;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingerdiantitem, parent, false); // Use ingredient_item layout
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    public void setOnItemClickListener(IngredientAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);

        holder.ingredientName.setText(ingredient.getStrIngredient());
        String ingredientImageUrl = "https://www.themealdb.com/images/ingredients/"+ingredient.getStrIngredient()+".png"; // Construct image URL

        Glide.with(holder.ingredientImage.getContext())
                .load(ingredientImageUrl)


                .into(holder.ingredientImage);

        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(ingredient);

        });

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.item_image);
            ingredientName = itemView.findViewById(R.id.item_name);
        }
    }
    public  interface onItemClickListener{
        void onItemClick(Ingredient ingredient );
    }
}