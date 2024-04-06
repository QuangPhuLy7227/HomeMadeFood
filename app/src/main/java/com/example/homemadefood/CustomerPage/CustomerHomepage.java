package com.example.homemadefood.CustomerPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomepage extends AppCompatActivity {

    private List<RestaurantData> dataList;
    private MyAdapter adapter;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton backButton;
    ResListViewFragment listViewFragment;
    MapsFragment mapsFragment;
    TextView username, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);
        Intent intent = getIntent();
        String cusName = intent.getStringExtra("name");
        String cusUsername = intent.getStringExtra("username");

        listViewFragment = new ResListViewFragment();
        mapsFragment = new MapsFragment();

        searchView = findViewById(R.id.searchView);
        backButton = findViewById(R.id.backButton);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        username.setText("UserName: " + cusUsername);
        name.setText("Welcome Back, " + cusName);
        RadioGroup radioGroup = findViewById(R.id.viewChoice);

        // Initialize dataList and adapter
        dataList = listViewFragment.dataList;
        adapter = listViewFragment.adapter;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (checkedId == R.id.listView) {
                    ft.replace(R.id.flContainer, listViewFragment);
                } else if (checkedId == R.id.mapView) {
                    ft.replace(R.id.flContainer, mapsFragment);
                }
                ft.commit();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.flContainer, listViewFragment).commit();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewFragment.searchList(newText);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listViewFragment.recyclerView.setVisibility(View.GONE);
                    backButton.setVisibility(View.VISIBLE);
                } else {
                    listViewFragment.recyclerView.setVisibility(View.VISIBLE);
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

}
