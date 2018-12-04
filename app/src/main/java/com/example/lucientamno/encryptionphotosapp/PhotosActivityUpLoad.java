package com.example.lucientamno.encryptionphotosapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucientamno.encryptionphotosapp.model.Photo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static com.example.lucientamno.encryptionphotosapp.PhotosActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
//import static com.example.lucientamno.encryptionphotosapp.PhotosActivity.PICTURE_RESULT;


public class PhotosActivityUpLoad extends AppCompatActivity {

    public ImageView myPhoto;
    Button bntStopCam, bntSendPhoto;
    FloatingActionButton back2MainActivity ;
     CoordinatorLayout mCoordinatorLayout;




    public PhotosActivityUpLoad() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_upload);
        Toolbar toolbar = findViewById(R.id.toolbarID1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" HERE AM I - YOUR CAMERA PHOTO");

        Bitmap bitmap = this.getIntent().getParcelableExtra("Bitmap");
        ImageView image =  findViewById(R.id.image_CameraID);
        image.setImageBitmap(bitmap);
        finish();

        Save saveMyFile = new Save();
        try {
            saveMyFile.saveImage(this,SingletonBitmap.getInstance().getTakenImage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("file  no saved ", e.getMessage());
        }


        // beginning of the encryption process


        mCoordinatorLayout = findViewById(R.id.activity_CoordinatesUp);
        back2MainActivity = findViewById(R.id.back_mainActivity_id);
        back2MainActivity.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                                Intent intent = new Intent(PhotosActivityUpLoad.this, PhotosActivity.class);
                                               Snackbar.make(mCoordinatorLayout, "GO BACK TO PREVIOUS SCREEN", Snackbar.LENGTH_SHORT);
                                               startActivity(intent);
                                           }
                                       }
                                    );// Snack-bar  message to go back to photoActivity
        bntSendPhoto =  findViewById(R.id.button_send_photo_id);
        bntSendPhoto.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("*/jpg");
                                                Communication go2GoogleApi = new Communication();
                                             //   final File photoFile = new File(getFilesDir(), "foo.jpg");
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, (Serializable) go2GoogleApi);
                                                startActivity(Intent.createChooser(shareIntent, "Share image using"));
                                            }
                                        }
                                    );



        bntStopCam = findViewById(R.id.button_stop_cam_id);
        bntStopCam.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


                                              finish();
                                          }
                                      }
                                 );//button Stop Camera




    }





}
