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
    private boolean isHorizontal;

    public RestaurantPromotionAdapter(Context context, List<RestaurantPromotion> dataList, RecyclerViewInterface recyclerViewInterface, boolean isHorizontal) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.isHorizontal = isHorizontal;
    }

    public void setData(List<RestaurantPromotion> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = isHorizontal ? R.layout.horizontal_recycler_view_top_restaurant_item : R.layout.vertical_recycler_view_top_restaurant_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantPromotion promotion = dataList.get(position);

        if (promotion == null) {
            return;
        }

        holder.bind(promotion);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public void bind(RestaurantPromotion promotion) {
            restaurantImage.setImageResource(promotion.getRestaurantImage());
            restaurantName.setText(promotion.getRestaurantName());
            ratingTextView.setText(String.valueOf(promotion.getRating()));
            totalRatingNumber.setText(String.valueOf(promotion.getTotalRating()));
            distance.setText(promotion.getDistance());
            deliveryTime.setText(promotion.getDeliveryTime());
            priceRange.setText(promotion.getPriceRange());
        }
    }
}
