package com.example.myfavouritemap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
public Button next_btn;
public ImageView camera_photo;
public ImageView gallery_photo;
public ImageView image_preview;
private static final int REQUEST_CAPTURE_IMAGE = 100;
private static final int PICK_IMAGE_REQUEST = 234;
public String x;

    //Firebase
    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        FirebaseApp.initializeApp(this);
        image_preview=findViewById(R.id.image_preview);
        gallery_photo=findViewById(R.id.add_photo);
        camera_photo=findViewById(R.id.camera_photo);

        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mStorageRef=FirebaseStorage.getInstance().getReference();





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        camera_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

        gallery_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchGalleryIntent();
            }
        });

        next_btn = findViewById(R.id.next_button);
        next_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                uploadFile();
               Intent i = new Intent(PhotoActivity.this, PhotoDetailActivity.class);
               i.putExtra("URI",x);
               startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 0)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {}
        }
    }

    private void dispatchGalleryIntent()
    {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void dispatchTakePictureIntent()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_preview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = mStorageRef.child("uploads/"+ System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dismissing the progress dialog
                    progressDialog.dismiss();

                    //displaying success toast
                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return sRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();

                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


                        SharedPreferences pref = getApplicationContext().getSharedPreferences("My", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();


                        String x=downUri.toString();

                        editor.putString("URI", x);

                        editor.commit();
                        //creating the upload object to store uploaded image details
                        Uploads upload = new Uploads(downUri.toString());

                        //adding an upload to firebase database
                        mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                    }
                }
            });
        }

        else {
            //display an error if no file is selected
            Toast t= Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_LONG);
            t.show();
        }
    }

}
