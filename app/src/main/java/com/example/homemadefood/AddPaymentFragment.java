package com.example.homemadefood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AddPaymentFragment extends Fragment {
    private List<PaymentModelClass> dataList;
    private PaymentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView paymentRecyclerView = view.findViewById(R.id.paymentRecyclerView);

        dataList = generatePaymentList();
        adapter = new PaymentAdapter(getContext(), dataList);
        paymentRecyclerView.setAdapter(adapter);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button addNewCardButton = view.findViewById(R.id.addNewCardButton);
        addNewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.addNewCardTextView).setVisibility(View.VISIBLE);
                paymentRecyclerView.setVisibility(View.GONE);
                addNewCardButton.setVisibility(View.GONE);
                view.findViewById(R.id.addNewCardLayout).setVisibility(View.VISIBLE);
            }
        });

        Button submitCardButton = view.findViewById(R.id.submitCardButton);
        submitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.addNewCardTextView).setVisibility(View.GONE);
                paymentRecyclerView.setVisibility(View.VISIBLE);
                addNewCardButton.setVisibility(View.VISIBLE);
                view.findViewById(R.id.addNewCardLayout).setVisibility(View.GONE);
            }
        });
    }


    public List<PaymentModelClass> generatePaymentList() {
        List<PaymentModelClass> dataList = new ArrayList<>();
        dataList.add(new PaymentModelClass("98765", "03/2026", "Peter Pham"));
        dataList.add(new PaymentModelClass("12345", "04/2026", "Quang Phu Ly"));
        return dataList;
    }


}