package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.babybuy.Model.Item;
import com.example.babybuy.adapter.RecycleAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecycleAdapter.OnItemClickListener {
    FloatingActionButton addItem;

    private RecyclerView mRecyclerView;
    private RecycleAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Item> mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addItem = findViewById(R.id.addFab);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mItem = new ArrayList<>();
        mAdapter = new RecycleAdapter(HomeActivity.this, mItem);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnclickListener(HomeActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Items_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItem.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                    Item upload = itemSnapshot.getValue(Item.class);
                    upload.setKey(itemSnapshot.getKey());
                    mItem.add(upload);

                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });

    }
    private void openDetailActivity(String [] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY", data [0]);
        intent.putExtra("DESCRIPTION_KEY", data [1]);
        intent.putExtra("IMAGE_KEY", data [2]);
        startActivity(intent);
    }


    @Override
    public void onItemClick(int position) {
        Item clickedItem = mItem.get(position);
        String[] ItemData = {clickedItem.getItem_name(), clickedItem.getItem_description(), clickedItem.getImageUrl()};
        openDetailActivity(ItemData);

    }

    @Override
    public void onShowItemClick(int position) {

    }

    @Override
    public void onShowDeleteClick(int position) {
        Item selectedItem = mItem.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(HomeActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}