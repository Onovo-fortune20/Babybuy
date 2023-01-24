package com.example.babybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    TextView itemDetailTextView,descriptionDetailTextView,priceDetailTextView;
    ImageView imageView8;

    private void initializeWidgets(){
        itemDetailTextView = findViewById(R.id.itemDetailTextView );
        descriptionDetailTextView = findViewById(R.id.descriptionDetailTextView );;
        priceDetailTextView = findViewById(R.id.priceDetailTextView );

        imageView8 = findViewById(R.id.imageView8);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeWidgets();

        Intent i = this.getIntent();

        String item = i.getExtras().getString("NAME_KEY");
        String description = i.getExtras().getString("DESCRIPTION_KEY");
        String image = i.getExtras().getString("IMAGE_KEY");
        String price = i.getExtras().getString("PRICE_KEY");

        itemDetailTextView.setText(item);
        descriptionDetailTextView.setText(description);
        priceDetailTextView.setText(price);
        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.babybuy2023)
                .fit()
                .centerCrop()
                .into(imageView8);


    }
}