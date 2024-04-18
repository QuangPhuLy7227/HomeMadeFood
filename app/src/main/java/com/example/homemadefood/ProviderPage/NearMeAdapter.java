package com.example.homemadefood.ProviderPage;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.ProviderPage.data.RestaurantData;

import java.util.List;

public class NearMeAdapter extends RecyclerView.Adapter {
    public NearMeAdapter(List<RestaurantData> restaurantList) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
