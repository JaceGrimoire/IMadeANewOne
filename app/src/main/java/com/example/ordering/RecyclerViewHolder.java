package com.example.ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewHolder extends RecyclerView.Adapter<RecyclerViewHolder.MyViewHolder> {

    Context context;
    ArrayList<Food> foods;
    private RecyclerViewClickInterface recyclerViewClickInterface;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodName.setText(foods.get(position).getName());
        holder.foodPrice.setText(String.valueOf(foods.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodPrice;
        public MyViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClickInterface.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
    public RecyclerViewHolder(Context c, ArrayList<Food> f, RecyclerViewClickInterface recyclerViewClickInterface){
        context = c;
        foods = f;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }
}
