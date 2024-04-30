package com.example.homemadefood.AnunjinPart;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;

import java.util.List;


public class foodAdapter extends RecyclerView.Adapter<foodAdapter.FoodViewHolder> {
    private Context context;
    private List<FoodItem_Model> foodItems;

    public foodAdapter(Context context, List<FoodItem_Model> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_cart_anunjin, parent, false);
        return new FoodViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem_Model foodItem = foodItems.get(position);
        holder.nameTextView.setText(foodItem.getName());
        holder.ratingTextView.setText(String.valueOf(foodItem.getRating()));
        holder.priceTextView.setText(String.valueOf(foodItem.getPrice()));

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView ratingTextView;
        TextView nameTextView;
        TextView priceTextView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.detailed_name);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            priceTextView = itemView.findViewById(R.id.detailed_price);
        }
    }
}