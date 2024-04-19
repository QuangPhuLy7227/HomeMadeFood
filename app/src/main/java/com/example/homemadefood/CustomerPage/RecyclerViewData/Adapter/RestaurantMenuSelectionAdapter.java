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
import com.example.homemadefood.CustomerPage.RecyclerViewData.RecyclerViewMenuInterface;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuDrinkModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantMenuFoodModel;
import com.example.homemadefood.R;

import java.text.DecimalFormat;
import java.util.List;

public class RestaurantMenuSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Object> dataList; // Contain both Drink and Food items
    private final RecyclerViewMenuInterface recyclerViewInterface;

    public RestaurantMenuSelectionAdapter(Context context, List<Object> dataList, RecyclerViewMenuInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<Object> itemList) {
        this.dataList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object itemData = dataList.get(position);
        if (itemData instanceof RestaurantMenuDrinkModel) {
            return 0;
        } else if (itemData instanceof RestaurantMenuFoodModel) {
            return 1;
        }
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.customer_menu_selection_drink_recycler_view_item, parent, false);
            return new DrinkViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.customer_menu_selection_food_recycler_view_item, parent, false);
            return new FoodViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object itemData = dataList.get(position);

        if (itemData instanceof RestaurantMenuDrinkModel) {
            RestaurantMenuDrinkModel drinkData = (RestaurantMenuDrinkModel) itemData;
            ((DrinkViewHolder) holder).bind(drinkData);
        } else if (itemData instanceof RestaurantMenuFoodModel) {
            RestaurantMenuFoodModel foodData = (RestaurantMenuFoodModel) itemData;
            ((FoodViewHolder) holder).bind(foodData);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImage;
        TextView drinkName;
        TextView drinkPrice;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkImage = itemView.findViewById(R.id.drinkImage);
            drinkName = itemView.findViewById(R.id.drinkName);
            drinkPrice = itemView.findViewById(R.id.drinkPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition(); //  Retrieves the current position of the item in the adapter
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onDrinkItemClick(position);
                }
            });
        }

        public void bind(RestaurantMenuDrinkModel drinkData) {
            // Load image using Glide
            Glide.with(context).load(drinkData.getDrinkImage()).into(drinkImage);
            drinkName.setText(drinkData.getDrinkName());

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedDrinkPrice = decimalFormat.format(drinkData.getDrinkPrice());
            drinkPrice.setText(formattedDrinkPrice);
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodDescription;
        TextView foodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onFoodItemClick(position);
                }
            });
        }

        public void bind(RestaurantMenuFoodModel foodData) {
            // Load image using Glide
            Glide.with(context).load(foodData.getFoodImage()).into(foodImage);
            foodName.setText(foodData.getFoodName());

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedFoodPrice = decimalFormat.format(foodData.getFoodPrice());
            foodPrice.setText(formattedFoodPrice);
        }
    }
}
