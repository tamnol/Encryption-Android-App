package com.example.lucientamno.encryptionphotosapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

// class model of photo created by Lucien 10/12/18
public class Photo  {

    private int imageId;






    public Photo(){}


    public Photo(int imageId) {
        this.imageId = imageId;


    }

    public int getImageId() {
        return imageId;
    }


    public void setImageId(int imageId) {
        this.imageId = imageId;
    }




}
