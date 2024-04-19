package com.example.homemadefood.AnunjinPart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;

import java.util.ArrayList;
import java.util.List;


public class MyCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<FoodItem_Model> dataList;
    private foodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_anunjin);

        //ImageView addToCartIcon = findViewById(R.id.add);
        //ImageView removeFromCartIcon = findViewById(R.id.remove);

        //addToCartIcon.setImageResource(R.drawable.baseline_add_24);
        //removeFromCartIcon.setImageResource(R.drawable.baseline_remove_24);
        recyclerView = findViewById(R.id.cart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        List<FoodItem_Model> selectedFoodItems = (List<FoodItem_Model>) getIntent().getSerializableExtra("selectedFoodItems");
//        adapter = new foodAdapter(selectedFoodItems);

        dataList = generatedList();
        adapter = new foodAdapter(getBaseContext(), dataList );
        recyclerView.setAdapter(adapter);
    }

    public List<FoodItem_Model> generatedList() {
        List<FoodItem_Model> dataList = new ArrayList<>();
        dataList.add(new FoodItem_Model("Pizza", 5.0, 50));
        dataList.add(new FoodItem_Model("Burger", 4.0, 50));
        dataList.add(new FoodItem_Model("Salad", 3.0, 50));
        return dataList;
    }
}



