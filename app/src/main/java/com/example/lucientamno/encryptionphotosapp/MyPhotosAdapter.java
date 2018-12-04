package com.example.lucientamno.encryptionphotosapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucientamno.encryptionphotosapp.Interface.PhotoClickListener;
import com.example.lucientamno.encryptionphotosapp.PhotoSelected.PhotoSelected;
import com.example.lucientamno.encryptionphotosapp.model.Photo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
//import static com.example.lucientamno.encryptionphotosapp.PhotosActivity.PICTURE_RESULT;
/*
 * Adapter to populate the RecyclerView of photos on the user photo activity class
 *      create by Lucien 10/4/2018
 * */

public class MyPhotosAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private LayoutInflater inflater;
    private List<Photo> myPhotoList;
    Context context;
    SerialDeserialization serialization = new SerialDeserialization();





    public MyPhotosAdapter(List<Photo> myPhotoList, Context context) {
        this.context = context;
        this.myPhotoList = myPhotoList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroupParent, int viewType) {
        View viewGroupChild = inflater.inflate(R.layout.layout_photos, viewGroupParent, false);// check later
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(viewGroupChild);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder eachViewHolder, int viewType) {
         final Photo listPhotos = myPhotoList.get(viewType);
         int id = listPhotos.getImageId();




         // Bind data
        // eachViewHolder.myPhotoView.setImageResource(id);







        eachViewHolder.setItemPhotoListener(new PhotoClickListener() {
            @Override
            public void onclick( View v, int position) {

                    Toast.makeText(context, " WHAOU! SELECTED ", Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();

            }

        });

    }

    @Override
    public int getItemCount() {
        return myPhotoList.size();

    }


}



//class contains photos viewed
class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView myPhotoView;


    PhotoClickListener itemPhotoListener;


    public void setItemPhotoListener(PhotoClickListener itemPhotoListener) {
        this.itemPhotoListener = itemPhotoListener;



    }

    public PhotoViewHolder(View itemView) {
        super(itemView);
     //  myPhotoView = (ImageView) itemView.findViewById(R.id.image_CameraID);
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        itemPhotoListener.onclick(v,getAdapterPosition());


    }





}