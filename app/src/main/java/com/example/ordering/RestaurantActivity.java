package com.example.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<String> restaurantList;
    private RestaurantViewHolder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        final Bundle extras = getIntent().getExtras();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("menu");
        restaurantList = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    restaurantList.add(ds.getKey());
                }
                adapter = new RestaurantViewHolder(RestaurantActivity.this, restaurantList,RestaurantActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TEST", error.toString());
            }
        });
        recyclerView = findViewById(R.id.restaurant_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(RestaurantActivity.this, FoodActivity.class);
        intent.putExtra("sel_res", restaurantList.get(position));
        intent.putExtra("user", getIntent().getExtras().getString("user"));
        startActivity(intent);


    }

    @Override
    public void onLongItemClick(int position) {

    }
}