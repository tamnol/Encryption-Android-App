package com.example.lucientamno.encryptionphotosapp.PhotoSelected;

import com.example.lucientamno.encryptionphotosapp.R;





/* Class created by Lucien to Hold global variables*/


public class PhotoSelected  {

    int [] samplesPhotos = {  R.drawable.interior, R.drawable.impossible, R.drawable.titanic,

                              R.drawable.control, R.drawable.highkick, R.drawable.marraton,

                             R.drawable.miterrunner, R.drawable.photoplayer, R.drawable.play,

                             R.drawable.smallkicker, R.drawable.soccerhit, R.drawable.priere,

                             R.drawable.ford, R.drawable.dreamhouse, R.drawable.nissan,

                             R.drawable.flower};



    public PhotoSelected(){ }

    public PhotoSelected(int[] samplesPhotos) {
        this.samplesPhotos = samplesPhotos;
    }

    public int[] getSamplesPhotos() {
        return samplesPhotos;
    }


}
