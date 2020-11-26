package com.example.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OrderActivity extends AppCompatActivity {

    private TextView food_name, food_price, food_add;
    private EditText food_qty;
    private Button btnMinus, btnPlus;
    private ImageView food_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnCart;
    private Toolbar toolbar;
    private String food;
    double price;
    private String restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btnCart = findViewById(R.id.btnCart);

        final Bundle extras = getIntent().getExtras();
        food = extras.getString("food");
        price = extras.getDouble("price");
        restaurant = extras.getString("sel_res");

        Log.d("Food", food + " " + price);

        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_add = findViewById(R.id.food_add);
        food_image = findViewById(R.id.img_food);
        food_qty = findViewById(R.id.txt_qty);
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = food_qty.getText().toString();
                if(num.equals("")) {
                    food_qty.setText("1");
                }
                else {
                    food_qty.setText(String.valueOf(Integer.parseInt(num) + 1));
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String num = food_qty.getText().toString();
                Log.d("TEST", num);
                if(num.equals("")) {
                    food_qty.setText("1");
                }
                else if(num.equals("1")){
                    food_qty.setText("1");
                }
                else {
                    food_qty.setText(String.valueOf(Integer.parseInt(num) - 1));
                }
            }
        });

        toolbar = findViewById(R.id.toolbar);


        food_qty.setText("1");
        food_name.setText(food);
        food_price.setText(String.valueOf(price));
        collapsingToolbarLayout = findViewById(R.id.collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(R.style.CollapsedBar);
        collapsingToolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Order");

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Orders order = new Orders();
                order.setFoodName(food);
                order.setPrice(price);
                order.setQty(Integer.parseInt(food_qty.getText().toString()));
                order.setRestaurant(restaurant);
                OrderDatabase.getInstance().getOrdersArrayList().add(order);

                Intent intent = new Intent(OrderActivity.this, FoodActivity.class);
                intent.putExtra("sel_res", extras.getString("sel_res"));
                intent.putExtra("user", getIntent().getExtras().getString("user"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}