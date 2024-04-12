//package com.example.homemadefood.ProviderPage.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.homemadefood.R;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RestaurantInfoFragment extends Fragment {
//
//    private EditText resNameEditText, resInfoEditText, resAddressEditText, zipCodeEditText,
//            phoneNumberEditText, openHoursEditText, closeHoursEditText, selectDateEditText;
//
//    private Spinner selectCategorySpinner;
//    private Button addButton, cancelButton;
//
//    private FirebaseFirestore firestore;
//
//    public RestaurantInfoFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_restaurant_info, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Initialize Firestore
//        firestore = FirebaseFirestore.getInstance();
//
//        // Initialize UI elements
//        resNameEditText = view.findViewById(R.id.resName);
//        resInfoEditText = view.findViewById(R.id.resInfo);
//        resAddressEditText = view.findViewById(R.id.resAddress);
//        zipCodeEditText = view.findViewById(R.id.zipCode);
//        phoneNumberEditText = view.findViewById(R.id.phoneNumber);
//        openHoursEditText = view.findViewById(R.id.openHours);
//        closeHoursEditText = view.findViewById(R.id.closeHours);
//        selectDateEditText = view.findViewById(R.id.selectDate);
//        selectCategorySpinner = view.findViewById(R.id.selectCategorySpinner);
//        addButton = view.findViewById(R.id.save);
//        cancelButton = view.findViewById(R.id.cancel);
//
//        // Set onClickListener for the Add button
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveRestaurantToFirestore();
//            }
//        });
//
//        // Set onClickListener for the Cancel button
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle cancel action if needed
//            }
//        });
//    }
//
//    // Save the restaurant data to Firestore
//    private void saveRestaurantToFirestore() {
//        String name = resNameEditText.getText().toString();
//        String info = resInfoEditText.getText().toString();
//        String address = resAddressEditText.getText().toString();
//        String zipCode = zipCodeEditText.getText().toString();
//        String phoneNumber = phoneNumberEditText.getText().toString();
//        String openHours = openHoursEditText.getText().toString();
//        String closeHours = closeHoursEditText.getText().toString();
//        String category = selectCategorySpinner.getSelectedItem().toString();
//        String date = selectDateEditText.getText().toString();
//
//        Map<String, Object> restaurantData = new HashMap<>();
//        restaurantData.put("name", name);
//        restaurantData.put("info", info);
//        restaurantData.put("address", address);
//        restaurantData.put("zipCode", zipCode);
//        restaurantData.put("phoneNumber", phoneNumber);
//        restaurantData.put("openHours", openHours);
//        restaurantData.put("closeHours", closeHours);
//        restaurantData.put("category", category);
//        restaurantData.put("date", date);
//
//        firestore.collection("restaurants")
//                .add(restaurantData)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        // Restaurant data added successfully
//                        Toast.makeText(getActivity(), "Restaurant added successfully!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Failed to add restaurant data to Firestore
//                        Toast.makeText(getActivity(), "Failed to add restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}
