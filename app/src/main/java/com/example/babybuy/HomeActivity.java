
package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.babybuy.Model.Item;
import com.example.babybuy.adapter.RecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    FloatingActionButton addItemBtn;
    private List<Item> itemList;
    TextView greeting;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("PRICE_KEY",data[2]);
        intent.putExtra("IMAGE_KEY",data[3]);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );

        addItemBtn = findViewById(R.id.addFab);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        greeting = findViewById(R.id.tvViewGreeting);
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);

        if (time < 12){
            greeting.setText("Good Morning");
        }else if (time < 16){
            greeting.setText("Good Afternoon");
        }else if (time < 21){
            greeting.setText("Good Evening");
        }else {
            greeting.setText("Good Night");
        }

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        itemList = new ArrayList<> ();
        mAdapter = new RecyclerAdapter (HomeActivity.this, itemList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(HomeActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itemList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item upload = itemSnapshot.getValue(Item.class);
                    upload.setKey(itemSnapshot.getKey());
                    itemList.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void onItemClick(int position) {
        Item clickedItem= itemList.get(position);
        String[] itemData={clickedItem.getName(),clickedItem.getDescription(),clickedItem.getPrice(),clickedItem.getImageUrl()};
        openDetailActivity(itemData);
    }

    @Override
    public void onShowItemClick(int position) {
        Item clickedItem= itemList.get(position);
        String[] itemData={clickedItem.getName(),clickedItem.getDescription(), clickedItem.getPrice(),clickedItem.getImageUrl()};
        openDetailActivity(itemData);
    }



    @Override
    public void onDeleteItemClick(int position) {
        Item selectedItem = itemList.get(position);
        final String selectedKey = selectedItem.getKey();

        getAndDeleteItem(selectedKey, HomeActivity.this);
    }

    public void getAndDeleteItem(String itemKey, final Context context) {
        // Get a reference to the item
//        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child(itemKey);
        //StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items_uploads");
        // Get the item
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Do something with the item, such as displaying its information on the screen
                Item item = dataSnapshot.getValue(Item.class);
//                Log.d("ITEM", item.toString());
                Toast.makeText(context, "Item.... " + item, Toast.LENGTH_SHORT).show();

                // Delete the item
                dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Item not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(context, "Item " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ITEM", databaseError.getMessage());
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}