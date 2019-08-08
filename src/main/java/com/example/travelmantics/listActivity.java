package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class listActivity extends AppCompatActivity {
    private ArrayList<TravelDeal> dealList;
    private FirebaseDatabase mFirebaseDatebase;
    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.newDeal:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtils.attachAuthListener();
                            }
                        });
                 FirebaseUtils.detachAuthListener();
                 return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.newDeal);
        if (FirebaseUtils.isAdministrator == true) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachAuthListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtils.openToReference("travelDeals" , this);
        mFirebaseDatebase = FirebaseUtils.mFirebaseDatabase;
        mFirebaseReference = FirebaseUtils.mFirebaseReference;

        RecyclerView recyclerView = findViewById(R.id.rvDeals);
        final DealAdapter dealAdapter = new DealAdapter();
        recyclerView.setAdapter(dealAdapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseUtils.attachAuthListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        mFirebaseReference.addChildEventListener(mChildEventListener);
    }

    public  void showMenu(){
        invalidateOptionsMenu();
    }
}
