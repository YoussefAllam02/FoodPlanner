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
import com.youssef.foodplanner.model.model.Area;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder>  {
    private final Context context;
    private List<Area> areaList;

    public void setOnItemClickListener(AreaAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private onItemClickListener onItemClickListener;
    public AreaAdapter(Context context, List<Area> areaList) {
        this.context = context;
        this.areaList = areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.areaitem, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);

        holder.areaName.setText(area.getStrArea());
        String flagUrl = CountryFlag.getFlagUrl(area.getStrArea());
        Glide.with(holder.areaImage.getContext())
                .load(flagUrl)
                .into(holder.areaImage);


        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(area);

        });


    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        ImageView areaImage;
        TextView areaName;  // Renamed to areaName
        //ImageView favButton; // Add this if you have a favorite button

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaImage = itemView.findViewById(R.id.item_image); // Correct ID
            areaName = itemView.findViewById(R.id.item_name);  // Correct ID

        }
    }
    public  interface onItemClickListener{
        void onItemClick(Area area );
    }
}
