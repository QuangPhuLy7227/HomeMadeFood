package com.example.homemadefood.CustomerPage.RecyclerViewData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;

import java.util.List;

public class RestaurantPromotionAdapter extends RecyclerView.Adapter<RestaurantPromotionAdapter.ViewHolder> {
    private final Context context;
    private List<RestaurantPromotion> dataList;
    private final RecyclerViewInterface recyclerViewInterface;

    public RestaurantPromotionAdapter(Context context, List<RestaurantPromotion> dataList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<RestaurantPromotion> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_view_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantPromotion promotion = dataList.get(position);

        if (promotion == null) {
            return;
        }

        holder.restaurantImage.setImageResource(promotion.getRestaurantImage());
        holder.restaurantName.setText(promotion.getRestaurantName());
        holder.ratingTextView.setText(String.valueOf(promotion.getRating()));
        holder.totalRatingNumber.setText(String.valueOf(promotion.getTotalRating()));
        holder.distance.setText(promotion.getDistance());
        holder.deliveryTime.setText(promotion.getDeliveryTime());
        holder.priceRange.setText(promotion.getPriceRange());
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView restaurantImage;
        private TextView restaurantName;
        private TextView ratingTextView;
        private TextView totalRatingNumber;
        private TextView distance;
        private TextView deliveryTime;
        private TextView priceRange;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            totalRatingNumber = itemView.findViewById(R.id.ratingNumber);
            distance = itemView.findViewById(R.id.distance);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            priceRange = itemView.findViewById(R.id.priceRange);

        }
    }
}
