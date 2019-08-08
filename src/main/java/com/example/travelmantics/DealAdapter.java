package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    private ArrayList<TravelDeal> dealList;
    private FirebaseDatabase mFirebaseDatebase;
    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListener;
    ImageView imageDeal;
    public DealAdapter () {
   // FirebaseUtils.openToReference("travelDeals", this);
        mFirebaseDatebase = FirebaseUtils.mFirebaseDatabase;
        mFirebaseReference = FirebaseUtils.mFirebaseReference;
        dealList = FirebaseUtils.dealArrayList;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                deal.setId(dataSnapshot.getKey());
                dealList.add(deal);
                notifyItemInserted(dealList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mFirebaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_row,parent,false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
     TravelDeal deal = dealList.get(position);
     holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvPrice;
        TextView tvDescription;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription =itemView.findViewById(R.id.tvDescription);
            imageDeal = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal){
          tvTitle.setText(deal.getTitle());
          tvPrice.setText(deal.getPrice());
          tvDescription.setText(deal.getDescription());
          showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TravelDeal dealSelected = dealList.get(position);
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("Deal",dealSelected);
            view.getContext().startActivity(intent);
        }
    }
    private void showImage(String url){
        if (url != null && url.isEmpty() == false) {
            Picasso.with(imageDeal.getContext())
                    .load(url)
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageDeal);
        }
    }

}
