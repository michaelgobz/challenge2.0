package com.example.travelmantics;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Parcel;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {
    public  static ArrayList<TravelDeal> dealArrayList;
    public  static FirebaseDatabase mFirebaseDatabase;
    public  static DatabaseReference mFirebaseReference;
    public  static FirebaseAuth mfirebaseAuth;
    public  static  FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private static FirebaseUtils mFirebaseUtils;
    public  static FirebaseStorage mFirebaseStorage;
    public  static StorageReference mStorageRefernce;
    public static final  int RC_SIGN_IN = 123;
    public  static listActivity caller;
    public static boolean isAdministrator;
    public static ChildEventListener mChildEventListener;



    private FirebaseUtils(){}

    public static void openToReference (String reference , final listActivity callerActivity){
        if(mFirebaseUtils == null){
            mFirebaseUtils = new FirebaseUtils();
            mFirebaseDatabase =  FirebaseDatabase.getInstance();
            mfirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(mfirebaseAuth.getCurrentUser() == null) {
                        FirebaseUtils.signIn();
                    }else {
                        String userUid = firebaseAuth.getUid();
                        checkAdministrator(userUid);
                    }
                    Toast.makeText(callerActivity.getBaseContext() , "Welcome!",Toast.LENGTH_SHORT).show();
                }
            };
       connectToStorage();
        }
        dealArrayList = new ArrayList<TravelDeal>();
        mFirebaseReference = mFirebaseDatabase.getReference().child(reference);
    }

    private  static  void checkAdministrator(String userUid) {
       isAdministrator = false;
       mFirebaseReference = mFirebaseDatabase.getReference().child("administrators").child(userUid);
       mChildEventListener = new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               FirebaseUtils.isAdministrator = true;
               caller.showMenu();
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

    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

// Create and launch sign-in intent
        caller. startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    public  static void attachAuthListener(){
        mfirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    public static void detachAuthListener(){
        mfirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    public  static void connectToStorage (){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRefernce = mFirebaseStorage.getReference().child("deals_images");
    }

}
