package com.example.ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CartViewHolder extends RecyclerView.Adapter<CartViewHolder.MyCartViewHolder> {

    Context context;
    ArrayList<Orders> list;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public CartViewHolder(Context context, ArrayList<Orders> list, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new MyCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        holder.txtCartName.setText(list.get(position).getFoodName());
        holder.txtPrice.setText( String.valueOf(list.get(position).getPrice()));
        holder.txtqty.setText(String.valueOf(list.get(position).getQty()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyCartViewHolder extends RecyclerView.ViewHolder {
        TextView txtCartName, txtPrice, txtqty;
        public MyCartViewHolder(View itemView) {
            super(itemView);
            txtCartName = itemView.findViewById(R.id.item_name);
            txtPrice = itemView.findViewById(R.id.item_price);
            txtqty = itemView.findViewById(R.id.txtQty);
        }
    }
}
