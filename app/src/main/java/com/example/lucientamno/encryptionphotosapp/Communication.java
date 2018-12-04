package com.example.lucientamno.encryptionphotosapp;

import android.util.Log;
import android.widget.Toast;

import com.example.lucientamno.encryptionphotosapp.external.PhotosLibraryClientFactory;
import com.google.api.gax.rpc.ApiException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.MediaItem;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.proto.NewMediaItemResult;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.google.rpc.Code;
import com.google.rpc.Status;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;

public class Communication {
    public static void main(String args[]) {

        String PATH = "C:/Users/LucienTamno/AndroidStudioProjects/EncryptionPhotosApp/app/src/main/res/raw/";
        try {

            UploadMediaItemRequest uploadRequest = null;
            List<String> REQUIRED_SCOPES = new ArrayList<String>();
            REQUIRED_SCOPES.add("https://www.googleapis.com/auth/photoslibrary");
            try {
                uploadRequest = UploadMediaItemRequest.newBuilder()
                        //filename of the media item along with the file extension
                        .setFileName("photoImage*.jpg")
                        .setDataFile(new RandomAccessFile(PATH, "r"))
                        .build();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                Log.e("file not found", "Incorrect File");
            }
            ;
            PhotosLibraryClient photosLibraryClient =
                    PhotosLibraryClientFactory.createClient("C:/Users/LucienTamno/AndroidStudioProjects/EncryptionPhotosApp/app/src/main/assets/clientsecrets.json", REQUIRED_SCOPES);
            // Upload and capture the response
            UploadMediaItemResponse uploadResponse = photosLibraryClient.uploadMediaItem(uploadRequest);
            if (uploadResponse.getError().isPresent()) {
                // If the upload results in an error, handle it
                UploadMediaItemResponse.Error error = uploadResponse.getError().get();
            } else {
                // If the upload is successful, get the uploadToken
                String uploadToken = uploadResponse.getUploadToken().get();
                // Use this upload token to create a media item
                try {
                    // Create a NewMediaItem with the uploadToken obtained from the previous upload request, and a description
                    NewMediaItem newMediaItem = NewMediaItemFactory
                            .createNewMediaItem(uploadToken, "uploadingImage");
                    List<NewMediaItem> newItems = Arrays.asList(newMediaItem);

                    BatchCreateMediaItemsResponse response = photosLibraryClient.batchCreateMediaItems(newItems);
                    for (NewMediaItemResult itemsResponse : response.getNewMediaItemResultsList()) {
                        Status status = itemsResponse.getStatus();
                        if (status.getCode() == Code.OK_VALUE) {
                            // The item is successfully created in the user's library
                            MediaItem createdItem = itemsResponse.getMediaItem();

                        } else {
                            // The item could not be created. Check the status and try again
                            Log.d("The item could not be created","Check the status and try again");
                        }
                    }
                } catch (ApiException e) {
                    // Handle error
                    Log.e("ApiEXCEPTION", e.getMessage());
                }
            }

        } catch (Exception e) {
            Log.e("All Exceptions", e.getMessage());
        }
    }



}
