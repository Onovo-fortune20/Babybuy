
package com.example.babybuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DetailsActivity extends AppCompatActivity {
    TextView nameDetailTextView,descriptionDetailTextView,priceDetailTextView;
    ImageView teacherDetailImageView;
    FloatingActionButton sendBtnID;

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 3;

    private void initializeWidgets(){
        nameDetailTextView= findViewById(R.id.itemDetailTextView);
        descriptionDetailTextView= findViewById(R.id.descriptionDetailTextView);
        priceDetailTextView= findViewById(R.id.priceDetailTextView);
        teacherDetailImageView=findViewById(R.id.imageView8);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        sendBtnID = findViewById(R.id.sendBtnId);

        sendBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextMessage();
            }
        });

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String name=i.getExtras().getString("NAME_KEY");
        String description=i.getExtras().getString("DESCRIPTION_KEY");
        String price=i.getExtras().getString("PRICE_KEY");
        String imageURL=i.getExtras().getString("IMAGE_KEY");

        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        descriptionDetailTextView.setText(description);
        priceDetailTextView.setText(price);
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.download)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);

    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void shareTextMessage(){
        String title = nameDetailTextView.getText().toString();
        String price = priceDetailTextView.getText().toString();
        String description = descriptionDetailTextView.getText().toString();
        String phoneNumber = "";

        String message = "Item Name: " + title + "\nItem Price: " + price + "\nItem Description: " + description;

        Toast.makeText(this, "sending " + message , Toast.LENGTH_SHORT).show();

        if (checkPermission(Manifest.permission.SEND_SMS)){
            String phone = phoneNumber;
            String smsBody = message;

            Intent smsIntent = new Intent(Intent.ACTION_SEND);
            smsIntent.setDataAndType(Uri.parse(phone),"text/plain");
            smsIntent.putExtra("sms_body", smsBody);

            PackageManager packageManager = getPackageManager();
            if (smsIntent.resolveActivity(packageManager) !=null){
                startActivity(Intent.createChooser(smsIntent, "send sms via:"));

                Toast.makeText(this, "send sms via", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "No app found to send sms. Please select from app settings", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "grant permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }
}