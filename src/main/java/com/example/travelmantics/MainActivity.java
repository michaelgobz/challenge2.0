package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseReference;
    EditText txtTitle;
    EditText txtPrice;
    EditText txtDescription;
    TravelDeal deal;
    ImageView imageView;
    private static final int PICTURE_REQ_CODE = 45;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseUtils.openToReference("travelDeals" ,MainActivity.this);
        mFirebaseDatabase = FirebaseUtils.mFirebaseDatabase;
        mFirebaseReference = FirebaseUtils.mFirebaseReference;
        txtTitle = findViewById(R.id.txtTitle);
        txtPrice =findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);
        imageView = findViewById(R.id.Image);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtPrice.setText(deal.getPrice());
        txtDescription.setText(deal.getDescription());
        showImage(deal.getImageUrl());
        Button btnImage = findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,"Insert Image"),PICTURE_REQ_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        if(FirebaseUtils.isAdministrator == true){
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.delete_menu).setVisible(true);
            enableEdit(true);
        }else{
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.delete_menu).setVisible(false);
            enableEdit(false);
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_REQ_CODE && requestCode == RESULT_OK){
            Uri imageUri = data.getData();
            StorageReference reference = FirebaseUtils.mStorageRefernce.child(imageUri.getLastPathSegment());
            reference.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getStorage().getDownloadUrl().toString();
//                    String pictureName = taskSnapshot.getStorage().getPath();
                    deal.setImageUrl(url);
//                    deal.setImageName(pictureName);
//                    showImage(url);
                }

            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this,"Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                Back();
                return  true;
            case R.id.delete_menu:
                Delete();
                 Toast.makeText(this,"Deal Deleted",Toast.LENGTH_SHORT).show();
                Back();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        if (deal.getId() == null) {
            mFirebaseReference.push().setValue(deal);
        }else{
            mFirebaseReference.child(deal.getId()).setValue(deal);
        }
    }
    private void Delete(){
        if(deal == null){
          Toast.makeText(this,"Please Save Deal",Toast.LENGTH_SHORT).show();
        }else{
            mFirebaseReference.child(deal.getId()).removeValue();
        }
    }
    private void Back(){
        Intent intent = new Intent(this,listActivity.class);
        startActivity(intent);
    }

    private  void enableEdit(boolean isEnable){
        txtTitle.setEnabled(isEnable);
        txtPrice.setEnabled(isEnable);
        txtDescription.setEnabled(isEnable);
    }

    private void showImage(String url){
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
    }
   }
}
