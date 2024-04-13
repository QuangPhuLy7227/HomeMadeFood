package com.example.homemadefood.CustomerPage.RecyclerViewData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemadefood.R;

import java.text.DecimalFormat;
import java.util.List;

public class RestaurantMenuSelectionAdapter extends RecyclerView.Adapter<RestaurantMenuSelectionAdapter.ViewHolder> {
    private final Context context;
    private List<RestaurantMenuDrinkModel> dataList;

    public RestaurantMenuSelectionAdapter(Context context, List<RestaurantMenuDrinkModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_menu_selection_drink_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantMenuDrinkModel drinkData = dataList.get(position);

        // Load image using Glide
        Glide.with(context).load(drinkData.getDrinkImage()).into(holder.drinkImage);

        holder.drinkName.setText(drinkData.getDrinkName());

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedDrinkPrice = decimalFormat.format(drinkData.getDrinkPrice());
        holder.drinkPrice.setText(formattedDrinkPrice);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImage;
        TextView drinkName;
        TextView drinkPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            drinkImage = itemView.findViewById(R.id.drinkImage);
            drinkName = itemView.findViewById(R.id.drinkName);
            drinkPrice = itemView.findViewById(R.id.drinkPrice);
        }
    }
}
