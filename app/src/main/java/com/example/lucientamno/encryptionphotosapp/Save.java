package com.example.lucientamno.encryptionphotosapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import static java.lang.System.exit;

public class Save {


    private Context theContext;
    private String nameFolder = "/raw";
    private String nameFile = "photoImage";
    private final String SECRET = "352066060926230";// to be replaced by each phone IMEI( international mobile equipment identity)
    private int intSecret;

    public void saveImage(Context context, Bitmap imageToSave) throws Exception {
        theContext = context;
        String photoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+nameFolder;
        String currentDateTime = getCurrentDateTime();

        File dir = new File(photoPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir,nameFile + currentDateTime+".jpg");

        try {
                FileOutputStream fileOut = new FileOutputStream(file);
                ByteArrayOutputStream bytesFile = new ByteArrayOutputStream();
                Cipher cipherFile = encrypt(SECRET);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(bytesFile, cipherFile);
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 40, cipherOutputStream);
                cipherOutputStream.write(SECRET.getBytes());
                cipherOutputStream.close();
                intSecret = Integer.parseInt(Base64.encodeToString(bytesFile.toByteArray(), Base64.DEFAULT));
                fileOut.write(intSecret);
                fileOut.flush();
                fileOut.close();
                makeSureFile_Created_Available(file); // method to make sure a photo was created
                availableToSave(); // method to inform a user that the picture was taken

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            unAvailableToSave();
        }

    }




    private void makeSureFile_Created_Available(File file) {

        MediaScannerConnection.scanFile(theContext, new String[]{file.toString()},null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage","Scanned"+path+":");
                        Log.e("ExternalStorage","-> Uri = "+ uri);

                    }
                });

    }



    private String getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        String currentDateTime = simpleDateFormat.format(calendar.getTime());

        return currentDateTime;
    }


    private void availableToSave(){

        Toast.makeText(theContext,"finally, this Photo was  saved", Toast.LENGTH_SHORT).show();
    }

    private void unAvailableToSave(){

        Toast.makeText(theContext,"Oups! something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    public Cipher encrypt(String secret) throws Exception {

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry =
                (KeyStore.PrivateKeyEntry) ks.getEntry("@Entry", null);

        Cipher inputCipher = Cipher.getInstance("RSA");
        try {
            inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        }catch (Exception e){

            Log.e("WHAT IS HERE", e.getMessage());
        }

        return inputCipher;
    }


}
