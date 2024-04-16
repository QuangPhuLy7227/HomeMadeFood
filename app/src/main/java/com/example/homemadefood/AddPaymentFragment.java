package com.example.homemadefood;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantDataModel;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPaymentFragment extends Fragment {
    private List<PaymentModelClass> dataList;
    private PaymentAdapter adapter;
    FirebaseFirestore db;
    private static final String SHARED_PREF_NAME = "homemadefood_shared_pref";
    private static final String KEY_USERNAME = "username";

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");

        db = FirebaseFirestore.getInstance();
        CollectionReference cardsCollection = db.collection("users").document(username).collection("Cards");

        RecyclerView paymentRecyclerView = view.findViewById(R.id.paymentRecyclerView);

        dataList = new ArrayList<>();
        adapter = new PaymentAdapter(getContext(), dataList);
        paymentRecyclerView.setAdapter(adapter);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchCardFromFirestore();

        Button addNewCardButton = view.findViewById(R.id.addNewCardButton);
        EditText cardHolderNameEditText = view.findViewById(R.id.cardHolderNameEditText);
        EditText cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        EditText expireDateEditeText = view.findViewById(R.id.expirationDateEditText);
        EditText cvvEditeText = view.findViewById(R.id.cvvEditText);
        addNewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.addNewCardTextView).setVisibility(View.VISIBLE);
                paymentRecyclerView.setVisibility(View.GONE);
                addNewCardButton.setVisibility(View.GONE);
                view.findViewById(R.id.addNewCardLayout).setVisibility(View.VISIBLE);

                cardHolderNameEditText.setText("");
                cardNumberEditText.setText("");
                expireDateEditeText.setText("");
                cvvEditeText.setText("");
            }
        });

        Button submitCardButton = view.findViewById(R.id.submitCardButton);
        Button viewCardButton = view.findViewById(R.id.viewCardButton);
        submitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.addNewCardTextView).setVisibility(View.GONE);
                paymentRecyclerView.setVisibility(View.VISIBLE);
                addNewCardButton.setVisibility(View.VISIBLE);
                view.findViewById(R.id.addNewCardLayout).setVisibility(View.GONE);

                String cardHolderName = cardHolderNameEditText.getText().toString();
                String cardNumber = cardNumberEditText.getText().toString();
                String expireDate = expireDateEditeText.getText().toString();
                String cvv = cvvEditeText.getText().toString();
                checkExistCardAndAdd(cardsCollection, cardHolderName, cardNumber, expireDate, cvv);
                fetchCardFromFirestore();
            }
        });

        viewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.addNewCardTextView).setVisibility(View.GONE);
                paymentRecyclerView.setVisibility(View.VISIBLE);
                addNewCardButton.setVisibility(View.VISIBLE);
                view.findViewById(R.id.addNewCardLayout).setVisibility(View.GONE);
                fetchCardFromFirestore();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchCardFromFirestore() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");

        db = FirebaseFirestore.getInstance();
        CollectionReference cardsCollection = db.collection("users").document(username).collection("Cards");

        cardsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dataList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        dataList.add(new PaymentModelClass(snapshot.getString("cardNumber"), snapshot.getString("expireDate"), snapshot.getString("cardHolderName")));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to read data from Firestore", Toast.LENGTH_SHORT).show());
    }


//    public List<PaymentModelClass> generatePaymentList() {
//        List<PaymentModelClass> dataList = new ArrayList<>();
//        dataList.add(new PaymentModelClass("98765", "03/2026", "Peter Pham"));
//        dataList.add(new PaymentModelClass("12345", "04/2026", "Quang Phu Ly"));
//        return dataList;
//    }

    private void checkExistCardAndAdd(CollectionReference cardsCollection, String cardHolderName, String cardNumber, String expireDate, String cvv) {
        cardsCollection.whereEqualTo("cardNumber", cardNumber).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Card already exists
                            Toast.makeText(getContext(), "This card already Added!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cardHolderName.isEmpty() || cardNumber.isEmpty() || expireDate.isEmpty() || cvv.isEmpty()) {
                                Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> newCard = new HashMap<>();
                                newCard.put("cardHolderName", cardHolderName);
                                newCard.put("cardNumber", cardNumber);
                                newCard.put("expireDate", expireDate);
                                newCard.put("cvv", cvv);
                                cardsCollection.document(cardNumber).set(newCard)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "New card added successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}