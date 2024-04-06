package com.example.homemadefood.CustomerPage.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.CustomerPage.RecyclerViewData.RecyclerViewInterface;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantMenuAdapter;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotion;
import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantPromotionAdapter;
import com.example.homemadefood.CustomerPage.RestaurantPage.RestaurantMenuActivity;
import com.example.homemadefood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResListViewFragment extends Fragment implements RecyclerViewInterface {

    protected List<RestaurantData> dataList1;
    protected RestaurantMenuAdapter adapter1;
    protected RecyclerView verticalRecyclerView;

    protected List<RestaurantPromotion> dataList2;
    protected RestaurantPromotionAdapter adapter2;
    protected RecyclerView horizontalRecyclerView;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList1 = new ArrayList<>();
        adapter1 = new RestaurantMenuAdapter(getContext(), dataList1, this);
        dataList2 = generatePromotionList();
        adapter2 = new RestaurantPromotionAdapter(getContext(), dataList2, this);
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("restaurants");
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
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList1.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestaurantData restaurantData = snapshot.getValue(RestaurantData.class);
                    dataList1.add(restaurantData);
                }
                adapter1.notifyDataSetChanged(); // Update adapter after fetching all data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to read data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void queryBasedOnDeliveryFee(float maxDeliveryFee) {
        Query query = mDatabase.orderByChild("deliveryFee").endAt(maxDeliveryFee);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList1.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestaurantData restaurantData = snapshot.getValue(RestaurantData.class);
                    if (restaurantData != null && restaurantData.getDeliveryFee() <= maxDeliveryFee) {
                        dataList1.add(restaurantData);
                    }
                }
                adapter1.notifyDataSetChanged(); // Update adapter after fetching all data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to read data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void restoreOriginalList() {
        dataList1.clear();
        fetchDataFromFirebase();
        adapter1.notifyDataSetChanged();
    }

    public void queryBasedOnFastFood() {
        Query query = mDatabase.orderByChild("category").equalTo("Fast Food");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList1.clear(); // Clear the list before adding the new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestaurantData restaurantData = snapshot.getValue(RestaurantData.class);
                    dataList1.add(restaurantData);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void queryBasedOnCategory(String category) {
        Query query;
        switch (category) {
            case "All":
                fetchDataFromFirebase();
                return;
            case "Fast Food":
                query = mDatabase.orderByChild("category").equalTo("Fast Food");
                break;
            case "Pizza":
                query = mDatabase.orderByChild("category").equalTo("Pizza");
            default:
                query = mDatabase.orderByChild("category").equalTo(category);
                break;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList1.clear(); // Clear the list before adding the new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestaurantData restaurantData = snapshot.getValue(RestaurantData.class);
                    dataList1.add(restaurantData);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to read data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
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
        List<RestaurantPromotion> searchListData2 = new ArrayList<>();

        for (RestaurantData data : dataList1) {
            if (data.getName().toLowerCase().contains(text.toLowerCase())) {
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

        for (RestaurantPromotion data : dataList2) {
            if (data.getRestaurantName().toLowerCase().contains(text.toLowerCase())) {
                searchListData2.add(data);
            }
        }

        if (searchListData2.isEmpty()) {
            adapter2.setData(new ArrayList<>());
            requireView().findViewById(R.id.noResultFound).setVisibility(View.VISIBLE);
        } else {
            adapter2.setData(searchListData2);
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
