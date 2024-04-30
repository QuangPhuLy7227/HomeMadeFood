package com.example.homemadefood.CustomerPage.RecyclerViewData.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuDrinkModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuFoodModel;
import com.example.homemadefood.R;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private ArrayList<Object> cartItems;
    private Context context;

    public CartItemAdapter(ArrayList<Object> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = cartItems.get(position);

        if (item instanceof RestaurantMenuDrinkModel) {
            RestaurantMenuDrinkModel drinkItem = (RestaurantMenuDrinkModel) item;
            holder.bindDrinkItem(drinkItem);
        } else if (item instanceof RestaurantMenuFoodModel) {
            RestaurantMenuFoodModel foodItem = (RestaurantMenuFoodModel) item;
            holder.bindFoodItem(foodItem);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;

        private TextView itemName;
        private TextView itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPriceInCartTextView);
        }

        public void bindDrinkItem(RestaurantMenuDrinkModel drinkItem) {
            Glide.with(context)
                    .load(drinkItem.getDrinkImage())
                    .into(itemImage);
            itemName.setText(drinkItem.getDrinkName());
            itemPrice.setText(String.valueOf(drinkItem.getDrinkPrice()));
        }

        public void bindFoodItem(RestaurantMenuFoodModel foodItem) {
            Glide.with(context)
                    .load(foodItem.getFoodImage())
                    .into(itemImage);
            itemName.setText(foodItem.getFoodName());
            itemPrice.setText(String.valueOf(foodItem.getFoodPrice()));

        }
    }
}