package com.example.homemadefood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private final Context context;
    private List<PaymentModelClass> dataList;

    public PaymentAdapter(Context context, List<PaymentModelClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_payment_recycler_view_item, parent, false);
        return new PaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        PaymentModelClass data = dataList.get(position);
        holder.cardNumber.setText(data.getCardNumber());
        holder.expiredDate.setText(data.getExpiredDate());
        holder.userName.setText(data.getUserCardName());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber;
        TextView expiredDate;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardNumber = itemView.findViewById(R.id.cardNumberTextView);
            expiredDate = itemView.findViewById(R.id.expiredDateTextView);
            userName = itemView.findViewById(R.id.cardUserName);
        }
    }

}

