package com.example.lucientamno.encryptionphotosapp;

import android.graphics.Bitmap;

class SingletonBitmap {

    private static final SingletonBitmap ourInstance = new SingletonBitmap();

    private Bitmap takenImage;

    static SingletonBitmap getInstance() {
        return ourInstance;
    }

    private SingletonBitmap() {

    }


    public Bitmap getTakenImage() {
        return takenImage;
    }

    public void setTakenImage(Bitmap takenImage) {
        this.takenImage = takenImage;
    }
}
