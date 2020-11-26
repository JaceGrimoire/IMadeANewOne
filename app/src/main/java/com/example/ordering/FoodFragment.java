package com.example.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<Food> foods;
    private RecyclerViewHolder adapter;
    private ArrayList<String> food_ID;
    private String sTitle;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    public FoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_food, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        foods = new ArrayList<>();
        sTitle = getArguments().getString("title");

        String sRestaurant = getArguments().getString("restaurant");
        reference = database.getReference("menu").child(sRestaurant).child(sTitle).child("meals");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        food_ID = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    food_ID.add(ds.getKey());
                }
                updateUI(food_ID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TEST DB", error.toString());
            }
        });

    }

    public void updateUI(ArrayList<String> food_ID) {
        for(int i = 0; i < food_ID.size();i++) {
            reference.child(food_ID.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Food food = snapshot.getValue(Food.class);
                    foods.add(food);
                    recyclerViewClickInterface = new RecyclerViewClickInterface() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(getActivity(), OrderActivity.class);
                            intent.putExtra("food", foods.get(position).getName());
                            intent.putExtra("price", foods.get(position).getPrice());
                            intent.putExtra("sel_res", getArguments().getString("restaurant"));
                            intent.putExtra("user", getArguments().getString("user"));
                            startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(int position) {
                        }
                    };
                    adapter = new RecyclerViewHolder(getActivity(),foods, recyclerViewClickInterface);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("System", error.toString());
                }
            });
        }
    }
}