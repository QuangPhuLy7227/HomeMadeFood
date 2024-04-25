package com.example.homemadefood.AnunjinPart;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemadefood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/** @noinspection ALL*/
public class MyCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<FoodItem_Model> dataList;
    private foodAdapter adapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_anunjin);

        recyclerView = findViewById(R.id.cart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirestore = FirebaseFirestore.getInstance();


        dataList = new ArrayList<>();
        fetchCartItemsFromFirestore();
        adapter = new foodAdapter(getApplicationContext(), dataList );
        recyclerView.setAdapter(adapter);

//        fetchCartItemsFromFirestore();
    }

    private void fetchCartItemsFromFirestore(){
        mFirestore.collection("orders")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                            String itemName = documentSnapshot.getString("itemName");
                            Double pricePerUnit = documentSnapshot.getDouble("price.itemPricePerUnit");
                            double itemPrice = pricePerUnit != null ? pricePerUnit : 0.0;
                            double quantity = documentSnapshot.getDouble("price.itemQuantity");
                            //double itemQuantity != null ? quantity : 0.0;
                            String itemImage = documentSnapshot.getString("itemImage");
                            String orderId = documentSnapshot.getString("orderId");

                            //For each document fetched fr
                            // om Firestore,
                            //extract the item name, price, and quantity, create a FoodItem_Model object, and add it to the dataList.
                            dataList.add(new FoodItem_Model(itemName, itemPrice, quantity, itemImage, orderId));
                        }
                        // to notify the adapter of the data change.
                        adapter.notifyDataSetChanged();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( MyCartActivity.this,"Failed to fetch cart items", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    public List<FoodItem_Model> generatedList() {
//        List<FoodItem_Model> dataList = new ArrayList<>();
//        dataList.add(new FoodItem_Model("Pizza", 5.0, 50, "https://firebasestorage.googleapis.com/v0/b/homemadefood-415918.appspot.com/o/item_images%2FCoffee?alt=media&token=e21021c9-d3ef-4703-91d8-fce98df32736","ORDER5157d1"));
//        dataList.add(new FoodItem_Model("Burger", 4.0, 50, "https://firebasestorage.googleapis.com/v0/b/homemadefood-415918.appspot.com/o/item_images%2FCoffee?alt=media&token=e21021c9-d3ef-4703-91d8-fce98df32736", ""));
//        dataList.add(new FoodItem_Model("Coffee", 3.0, 5, "https://firebasestorage.googleapis.com/v0/b/homemadefood-415918.appspot.com/o/item_images%2FCoffee?alt=media&token=e21021c9-d3ef-4703-91d8-fce98df32736", "ORDERac4ace"));
//        return dataList;
//    }
}



