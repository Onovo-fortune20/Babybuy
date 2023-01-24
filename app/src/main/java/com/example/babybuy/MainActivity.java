package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.babybuy.Model.item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

private  static  final int PICK_IMAGE_REQUEST = 1;

    private Button savebtn;
    private EditText itemName,itemPrice, itemDescription;
    private ImageView imageupload;
    private ProgressBar uploadProgressBar;

    private Uri mImageUri;

    private StorageReference mStrorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        savebtn = findViewById(R.id.button);
        itemName = findViewById(R.id.loginMail);
        itemPrice = findViewById(R.id.loginPass);
        itemDescription = findViewById(R.id.itemDesc);
        imageupload= findViewById(R.id.imageView3);
        uploadProgressBar = findViewById(R.id.progress_bar);

        mStrorageRef = FirebaseStorage.getInstance().getReference("items_upload");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items upload");

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "Upload still in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(imageupload);
        }
    }

    private String getFileExtension (Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType (uri));
    }

    private void uploadFile (){
        if (mImageUri != null){
            StorageReference fileReferences = mStrorageRef.child(System.currentTimeMillis()
            + "."+ getFileExtension(mImageUri));

            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);

            mUploadTask = fileReferences.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadProgressBar.setVisibility(View.VISIBLE);
                            uploadProgressBar.setIndeterminate(false);
                            uploadProgressBar.setProgress(0);
                        }
                    }, 500);
                    Toast.makeText(MainActivity.this, "items upload successful", Toast.LENGTH_SHORT).show();
                    item Item = new item(itemName.getText().toString(),
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(),
                    itemDescription.getText().toString()
                    );

            String button = mDatabaseRef.push().getKey();
            mDatabaseRef.child(button).setValue(Item);

            uploadProgressBar.setVisibility(View.INVISIBLE);
            openImagesActivity ();


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            uploadProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            uploadProgressBar.setProgress((int) progress);
                        }
                    });

        } else{
            Toast.makeText(this, "You haven't selected any file", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}

