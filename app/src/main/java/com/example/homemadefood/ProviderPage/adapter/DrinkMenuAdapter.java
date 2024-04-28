package com.example.homemadefood.ProviderPage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemadefood.ProviderPage.data.MenuData;
import com.example.homemadefood.R;

import java.util.List;

public class DrinkMenuAdapter extends RecyclerView.Adapter<DrinkMenuAdapter.DrinkViewHolder> {
    private Context context;
    private List<MenuData> drinkMenuList;

    public DrinkMenuAdapter(Context context, List<MenuData> drinkMenuList) {
        this.context = context;
        this.drinkMenuList = drinkMenuList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        MenuData drinkData = drinkMenuList.get(position);
        holder.bind(drinkData, context, holder.imageViewDrink);
    }

    @Override
    public int getItemCount() {
        return drinkMenuList.size();
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDrink;
        TextView textViewDrink;
        TextView textViewCategory;
        TextView textViewDrinkDescription;
        TextView textViewPrice;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDrink = itemView.findViewById(R.id.imageViewDrinkPic);
            textViewDrink = itemView.findViewById(R.id.textViewDrink);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDrinkDescription = itemView.findViewById(R.id.textViewDrinkDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }

        public void bind(MenuData drinkData, Context context, ImageView imageView) {
            textViewDrink.setText(drinkData.getItemName());
            textViewCategory.setText(drinkData.getCategory());
            textViewDrinkDescription.setText(drinkData.getDescription());
            textViewPrice.setText(String.valueOf(drinkData.getPrice()));
            Glide.with(context)
                    .load(drinkData.getImage())
                    .placeholder(R.drawable.default_drink_image)
                    .into(imageView);
        }
    }
}