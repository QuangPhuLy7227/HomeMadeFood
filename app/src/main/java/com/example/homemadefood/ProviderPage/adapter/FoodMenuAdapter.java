package com.example.homemadefood.ProviderPage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemadefood.ProviderPage.ActivityRemoveRestaurantMenu;
import com.example.homemadefood.ProviderPage.data.MenuData;
import com.example.homemadefood.R;

import java.util.List;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodViewHolder> {
    private Context context;
    private List<MenuData> foodMenuList;

    public FoodMenuAdapter(Context context, List<MenuData> foodMenuList) {
        this.context = context;
        this.foodMenuList = foodMenuList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        MenuData foodData = foodMenuList.get(position);
        holder.bind(foodData, context, holder.imageViewFood);
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityRemoveRestaurantMenu.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodMenuList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFood;
        TextView textViewCategory;
        TextView textViewFoodDescription;
        TextView textViewPrice;
        Button updateButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewRestaurant);
            textViewFood = itemView.findViewById(R.id.textViewFood);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewFoodDescription = itemView.findViewById(R.id.textViewFoodDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            updateButton = itemView.findViewById(R.id.updateFood);
        }

        public void bind(MenuData foodData, Context context, ImageView imageView) {
            textViewFood.setText(foodData.getItemName());
            textViewCategory.setText(foodData.getCategory());
            textViewFoodDescription.setText(foodData.getDescription());
            textViewPrice.setText(String.valueOf(foodData.getPrice()));
            Glide.with(context)
                    .load(foodData.getImage())
                    .placeholder(R.drawable.default_food_image)
                    .into(imageViewFood);
        }
    }
}