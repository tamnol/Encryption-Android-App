package com.example.lucientamno.encryptionphotosapp;

import com.example.lucientamno.encryptionphotosapp.model.Photo;
import com.google.gson.Gson;

public class SerialDeserialization {


    public SerialDeserialization() {


    }

 public String photoSerializer( Photo  photoSend ){

         photoSend = new Photo(1
                );

        Gson gson = new Gson();
        String jsonSend  =gson.toJson(photoSend);

        return jsonSend;
    }

   public void photoDeserializer(){

        String jsonReceive = "{'imageId':5}";

        Gson gson = new Gson();
        gson.fromJson(jsonReceive, Photo.class);

    }
}

