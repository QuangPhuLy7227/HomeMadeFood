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
import com.example.homemadefood.CustomerPage.RecyclerViewData.RecyclerViewInterface;
import com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass.RestaurantDataModel;
import com.example.homemadefood.R;
import java.text.DecimalFormat;
import java.util.List;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.ViewHolder> {
    private final Context context;
    private List<RestaurantDataModel> dataList;
    private final RecyclerViewInterface recyclerViewInterface;

    public RestaurantMenuAdapter(Context context, List<RestaurantDataModel> dataList, RecyclerViewInterface recyclerViewInterface) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        this.dataList = dataList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<RestaurantDataModel> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantDataModel data = dataList.get(position);
        // Load restaurant image using Glide
        Glide.with(context)
                .load(data.getRestaurantImageUri())
                .placeholder(R.drawable.burger) // Placeholder image while loading
                .into(holder.restaurantImage);

        // Set other restaurant details
        holder.restaurantName.setText(data.getName());

        // Format delivery fee to show whole numbers if it's an integer, otherwise show with two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedDeliveryFee = decimalFormat.format(data.getDeliveryFee());
        holder.deliveryFee.setText(formattedDeliveryFee);

        // Format rating to show with two decimal places
        String formattedRating = decimalFormat.format(data.getRating());
        holder.ratingTextView.setText(formattedRating);

        holder.totalRatingNumber.setText(String.valueOf(data.getTotalRating()));
        holder.deliveryTime.setText(String.valueOf(data.getDeliveryTime()));
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        TextView deliveryFee;
        TextView ratingTextView;
        TextView totalRatingNumber;
        TextView deliveryTime;
        RecyclerViewInterface recyclerViewInterface;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            deliveryFee = itemView.findViewById(R.id.deliveryFee);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            totalRatingNumber = itemView.findViewById(R.id.ratingNumber);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            this.recyclerViewInterface = recyclerViewInterface;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
}
