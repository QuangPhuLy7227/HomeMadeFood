package com.example.homemadefood.CustomerPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homemadefood.R;

import java.util.ArrayList;
import java.util.List;


public class ResListViewFragment extends Fragment {
    public List<RestaurantData> dataList;
    public MyAdapter adapter;
    public RecyclerView recyclerView;

    public ResListViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = generateDataList();
        adapter = new MyAdapter(getContext(), dataList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_res_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        // Initialize dataList and adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public List<RestaurantData> generateDataList() {
        List<RestaurantData> dataList = new ArrayList<>();

        dataList.add(new RestaurantData(R.drawable.burger, "Burger King", "$3.99", 4.5f,
                350, 30));
        dataList.add(new RestaurantData(R.drawable.mcdonald, "McDonald's", "$2.99", 4.2f,
                200, 25));
        dataList.add(new RestaurantData(R.drawable.chick_fil_a, "Chick-fil-A", "$5.99", 4.7f,
                500, 35));
        return dataList;
    }

    public void searchList(String text) {
        List<RestaurantData> searchListData = new ArrayList<>();

        for (RestaurantData data : dataList) {
            if (data.getName().toLowerCase().contains(text.toLowerCase())) {
                searchListData.add(data);
            }
        }

        if (searchListData.isEmpty()) {
            adapter.setData(new ArrayList<>()); // Clear the RecyclerView by setting an empty list
            getView().findViewById(R.id.noResultFound).setVisibility(View.VISIBLE);
        } else {
            adapter.setData(searchListData);
            getView().findViewById(R.id.noResultFound).setVisibility(View.GONE);
        }
    }
}