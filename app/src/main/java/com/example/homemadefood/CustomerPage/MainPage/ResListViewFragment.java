package com.example.homemadefood.CustomerPage.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.CustomerPage.RecyclerViewData.RecyclerViewInterface;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuAdapter;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotion;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotionAdapter;
import com.example.homemadefood.CustomerPage.RestaurantPage.RestaurantMenuActivity;
import com.example.homemadefood.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ResListViewFragment extends Fragment implements RecyclerViewInterface {

    protected List<RestaurantData> dataList1;
    protected RestaurantMenuAdapter adapter1;
    protected RecyclerView verticalRecyclerView;

    protected List<RestaurantPromotion> dataList2;
    protected RestaurantPromotionAdapter adapter2;
    protected RecyclerView horizontalRecyclerView;
    protected NestedScrollView nestedScrollView;

    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList1 = new ArrayList<>();
        adapter1 = new RestaurantMenuAdapter(getContext(), dataList1, this);
        dataList2 = generatePromotionList();
        adapter2 = new RestaurantPromotionAdapter(getContext(), dataList2, this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_res_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);
        horizontalRecyclerView.setAdapter(adapter2);
        verticalRecyclerView = view.findViewById(R.id.verticalRecyclerView);
        verticalRecyclerView.setAdapter(adapter1);
        verticalRecyclerView.setHasFixedSize(true);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        db.collection("restaurants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dataList1.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        RestaurantData restaurantData = snapshot.toObject(RestaurantData.class);
                        dataList1.add(restaurantData);
                    }
                    adapter1.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to read data from Firestore", Toast.LENGTH_SHORT).show());
    }

    public void queryRestaurants(float maxDeliveryFee, String category) {
        Query query = db.collection("restaurants");

        if (!category.equals("All")) {
            query = query.whereEqualTo("category", category);
        }

        if (maxDeliveryFee > 0) {
            query = query.whereLessThanOrEqualTo("deliveryFee", maxDeliveryFee);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            dataList1.clear();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                RestaurantData restaurantData = snapshot.toObject(RestaurantData.class);
                dataList1.add(restaurantData);
            }
            adapter1.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Failed to query data from Firestore", e);
            Toast.makeText(getContext(), "Failed to query data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }



    public void restoreOriginalList() {
        fetchDataFromFirestore();
    }

    @Override
    public void onItemClick(int position) {
        RestaurantData data = dataList1.get(position);
        Intent intent = new Intent(getActivity(), RestaurantMenuActivity.class);
        intent.putExtra("restaurant_name", data.getName());
        startActivity(intent);
    }

    public void searchList(String text) {
        List<RestaurantData> searchListData1 = new ArrayList<>();

        for (RestaurantData data : dataList1) {
            if (data.getName() != null && data.getName().toLowerCase().contains(text.toLowerCase())) {
                searchListData1.add(data);
            }
        }

        if (searchListData1.isEmpty()) {
            adapter1.setData(new ArrayList<>());
            requireView().findViewById(R.id.noResultFound).setVisibility(View.VISIBLE);
        } else {
            adapter1.setData(searchListData1);
            requireView().findViewById(R.id.noResultFound).setVisibility(View.GONE);
        }
    }

    private List<RestaurantPromotion> generatePromotionList() {
        List<RestaurantPromotion> dataList = new ArrayList<>();
        dataList.add(new RestaurantPromotion(R.drawable.steak, "Steak House", 4.5f, 350, "0.5 mile", "10 min", "$15"));
        dataList.add(new RestaurantPromotion(R.drawable.lobster, "Red Lobster", 4.5f, 350, "0.5 mile", "10 min", "$15"));
        return dataList;
    }
}
