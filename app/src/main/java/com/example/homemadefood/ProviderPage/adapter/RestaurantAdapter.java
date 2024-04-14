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
import com.example.homemadefood.ProviderPage.data.RestaurantData;
import com.example.homemadefood.R;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<RestaurantData> mRestaurantList;

    public RestaurantAdapter(Context context, List<RestaurantData> restaurantList) {
        mContext = context;
        mRestaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantData restaurant = mRestaurantList.get(position);

        holder.textViewName.setText(restaurant.getName());
        holder.textViewInfo.setText(restaurant.getInfo());
        holder.textViewAddress.setText(restaurant.getAddress());

        // Load restaurant image using Glide library
        Glide.with(mContext)
                .load(restaurant.getRestaurantImageUri())
                .placeholder(R.drawable.default_restaurant_image)
                .into(holder.imageViewRestaurant);
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRestaurant;
        TextView textViewName;
        TextView textViewInfo;
        TextView textViewAddress;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRestaurant = itemView.findViewById(R.id.imageViewRestaurant);
            textViewName = itemView.findViewById(R.id.textViewRestaurantName);
            textViewInfo = itemView.findViewById(R.id.textViewRestaurantInfo);
            textViewAddress = itemView.findViewById(R.id.textViewRestaurantAddress);
        }
    }
}

