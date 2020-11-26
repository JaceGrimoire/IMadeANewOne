package com.example.ordering;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private CartViewHolder adapter;

    TextView txtTotalPrice;
    Button btnPlace;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.listCart);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("food_orders");


        adapter = new CartViewHolder(CartActivity.this, OrderDatabase.getInstance().getOrdersArrayList(), CartActivity.this);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        int total = 0;

        for(int i = 0; i < OrderDatabase.getInstance().getOrdersArrayList().size(); i++){
            for(int j = 0; j < OrderDatabase.getInstance().getOrdersArrayList().get(i).getQty(); j++){
                total += OrderDatabase.getInstance().getOrdersArrayList().get(i).getPrice();
            }
        }

        txtTotalPrice.setText(String.valueOf(total));

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = reference.push().getKey();
                reference.child(key).child("cust_uid").setValue(uid);
                reference.child(key).child("rider_uid").setValue("0");
                for(int i = 0; i < OrderDatabase.getInstance().getOrdersArrayList().size(); i++) {
                    String sec_key = reference.child(key).child("orders").push().getKey();
                    reference.child(key).child("orders").child(sec_key).setValue(OrderDatabase.getInstance().getOrdersArrayList().get(i));
                }
                reference.child(key).child("cust_lat").setValue(0);
                reference.child(key).child("cust_long").setValue(0);
            }
        });
    }
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}