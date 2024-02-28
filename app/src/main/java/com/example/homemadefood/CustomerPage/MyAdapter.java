package com.example.homemadefood.CustomerPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final Context context;
    private List<RestaurantData> dataList;

    public MyAdapter(Context context, List<RestaurantData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setData(List<RestaurantData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantData data = dataList.get(position);
        holder.restaurantImage.setImageResource(data.getRestaurantImage());
        holder.name.setText(data.getName());
        holder.deliveryFee.setText(data.getDeliveryFee());
        holder.rating.setText(String.valueOf(data.getRating()));
        holder.totalRating.setText(String.valueOf(data.getTotalRating()));
        holder.deliveryTime.setText(String.valueOf(data.getDeliveryTime()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView restaurantImage;
        TextView name;
        TextView deliveryFee;
        TextView rating;
        TextView totalRating;
        TextView deliveryTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            name = itemView.findViewById(R.id.restaurantName);
            deliveryFee = itemView.findViewById(R.id.deliveryFee);
            rating = itemView.findViewById(R.id.ratingTextView);
            totalRating = itemView.findViewById(R.id.ratingNumber);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
        }
    }
}
