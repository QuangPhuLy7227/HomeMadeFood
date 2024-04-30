package com.example.homemadefood.ProviderPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.homemadefood.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>{

    private final List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_order_recycler_view_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind data to views
        Order order = orders.get(position);

        holder.usernameTextView.setText(order.getUsername());
        holder.emailTextView.setText(order.getEmail());
        holder.orderTextView.setText(order.getOrderDetails());

        // Implement click listeners for buttons if needed
        holder.cancelButton.setOnClickListener(v -> {
            orders.remove(position);
            notifyDataSetChanged();
        });
        holder.acceptButton.setOnClickListener(v -> {
            orders.remove(position);
            notifyDataSetChanged();
        });
        holder.messageButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("USERNAME", order.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (orders != null) {
            return orders.size();
        }
        return 0;
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView emailTextView;
        TextView orderTextView;
        Button cancelButton;
        Button acceptButton;
        Button messageButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            orderTextView = itemView.findViewById(R.id.orderTextView);
            cancelButton = itemView.findViewById(R.id.CancelBtn);
            acceptButton = itemView.findViewById(R.id.AcceptBtn);
            messageButton = itemView.findViewById(R.id.MsgBtn);
        }
    }

}

