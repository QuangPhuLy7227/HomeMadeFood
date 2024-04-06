package com.example.homemadefood.CustomerPage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.CustomerPage.RecyclerViewData.RestaurantData;
import com.example.homemadefood.R;
import java.util.ArrayList;
import java.util.List;

public class CustomerHomepage extends AppCompatActivity {

    private List<RestaurantData> dataList;
    private MyAdapter adapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);


        // Initialize dataList and adapter
        dataList = generateDataList();
        adapter = new MyAdapter(this, dataList);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.GONE);
                    backButton.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.GONE);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
            }
        });
    }

    private void searchList(String text) {
        List<RestaurantData> searchListData = new ArrayList<>();

        for (RestaurantData data : dataList) {
            if (data.getName().toLowerCase().contains(text.toLowerCase())) {
                searchListData.add(data);
            }
        }

        if (searchListData.isEmpty()) {
            adapter.setData(new ArrayList<>()); // Clear the RecyclerView by setting an empty list
            findViewById(R.id.noResultFound).setVisibility(View.VISIBLE);
        } else {
            adapter.setData(searchListData);
            findViewById(R.id.noResultFound).setVisibility(View.GONE);
        }
    }

    private List<RestaurantData> generateDataList() {
        List<RestaurantData> dataList = new ArrayList<>();

        dataList.add(new RestaurantData(R.drawable.burger, "Burger King", "$3.99", 4.5f,
                350, 30));
        dataList.add(new RestaurantData(R.drawable.mcdonald, "McDonald's", "$2.99", 4.2f,
                200, 25));
        dataList.add(new RestaurantData(R.drawable.chick_fil_a, "Chick-fil-A", "$5.99", 4.7f,
                500, 35));
        return dataList;
    }
}
