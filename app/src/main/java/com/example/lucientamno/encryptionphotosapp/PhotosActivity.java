package com.example.lucientamno.encryptionphotosapp;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.lucientamno.encryptionphotosapp.Interface.OnDialogButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Bitmap.Config.ARGB_4444;
import static android.graphics.Bitmap.Config.RGB_565;


public class PhotosActivity extends AppCompatActivity {

    //    Layout feature holding the list of photos


    private FloatingActionButton takeAction2;
    private CoordinatorLayout mCoordinatorLayout;
    private Button bntSendPhoto;





    public PhotosActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toolbar toolbar = findViewById(R.id.toolbarID0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" USE ME TO TAKE A PICTURE");





        mCoordinatorLayout = findViewById(R.id.activity_Coordinates);
        takeAction2 = findViewById(R.id.camera_id);

        takeAction2.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               Intent intent = new Intent();;
                                               onLaunchCamera(v);
                                               onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK, intent);
                                               intent.setClass(PhotosActivity.this,PhotosActivityUpLoad.class);
                                               intent.putExtra("Bitmap", SingletonBitmap.getInstance().getTakenImage());
                                               startActivity(intent);
                                           }
                                       }
        );




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, PhotosActivity.this);
        } else {
            // Open your camera here.
        }

    }



    /* module to take the photo with the camera, store and encrypt it*/

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =  1034;
    public String photoFileName = " ";
    File photoFile;



    // 1st part: Camera function  capturing a photo
    public boolean onLaunchCamera(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PhotosActivity.this, "com.example.lucientamno.encryptionphotosapp.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        try {

            if (intent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                return true;

            }

        } catch (Exception e){

            Log.e("WHAT IS HERE : ", e.getMessage());
        }
        return false;

    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_" + System.currentTimeMillis() + ".jpg");

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    // 2nd part: Camera function  capturing a photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE  ) {
            if (resultCode == RESULT_OK) {

                // by this point we have the camera photo on disk
              Bitmap bmp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
              SingletonBitmap.getInstance().setTakenImage(bmp);


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


// module - permissions to run the camera

public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
/*requests for runtime time permissions*/

    String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;

    String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;

    String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    // type of dialog opened in MainActivity
    @IntDef({DialogType.DIALOG_DENY, DialogType.DIALOG_NEVER_ASK})
    @Retention(RetentionPolicy.SOURCE)
    @interface DialogType {
        int DIALOG_DENY = 0, DIALOG_NEVER_ASK = 1; }

    // for security permissions
    @DialogType
    private int mDialogType;
    private String mRequestPermissions = "We are requesting the camera and Gallery permission as it is absolutely necessary for the app to perform it\'s functionality.\nPlease select \"Grant Permission\" to try again and \"Cancel \" to exit the application.";
    private String mRequsetSettings = "You have rejected the camera and Gallery permission for the application. As it is absolutely necessary for the app to perform you need to enable it in the settings of your device.\nPlease select \"Go to settings\" to go to application settings in your device and \"Cancel \" to exit the application.";
    private String mGrantPermissions = "Grant Permissions";
    private String mCancel = "Cancel";
    private String mGoToSettings = "Go To Settings";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                } else {
                    boolean showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION);
                    boolean showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION);
                    boolean showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION);
                    if (showRationale1 && showRationale2 && showRationale3) {
                        //explain to user why we need the permissions
                        mDialogType = DialogType.DIALOG_DENY;
                        // Show dialog with
                        openAlertDialog(mRequestPermissions, mGrantPermissions, mCancel, new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked() {

                                switch (mDialogType) {
                                    case DialogType.DIALOG_DENY:
                                        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, PhotosActivity.this);
                                        break;
                                    case DialogType.DIALOG_NEVER_ASK:
                                        redirectToAppSettings(PhotosActivity.this);
                                        break;

                                }
                                Toast.makeText(getApplicationContext(), "Permissions denied", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onNegativeButtonClicked() {
                                Toast.makeText(getApplicationContext(), "Permissions accepted", Toast.LENGTH_SHORT);

                            }
                        }, PhotosActivity.this);
                    } else {
                        //explain to user why we need the permissions and ask him to go to settings to enable it
                        mDialogType = DialogType.DIALOG_NEVER_ASK;
                        openAlertDialog(mRequsetSettings, mGoToSettings, mCancel, new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked() {

                                switch (mDialogType) {
                                    case DialogType.DIALOG_DENY:
                                        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, PhotosActivity.this);
                                        break;
                                    case DialogType.DIALOG_NEVER_ASK:
                                        redirectToAppSettings(PhotosActivity.this);
                                        break;

                                }
                                Toast.makeText(getApplicationContext(), "NEVER ASK AGAIN", Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onNegativeButtonClicked() {
                                Toast.makeText(getApplicationContext(), "WILL ASK NEXT TIME", Toast.LENGTH_SHORT);

                            }
                        }, PhotosActivity.this);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            Log.e("CAMERA IN PROCESS", "YES");
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void openAlertDialog(String message, String positiveBtnText, String negativeBtnText,
                                       final OnDialogButtonClickListener listener,Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onPositiveButtonClicked();
            }
        });
        builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onNegativeButtonClicked();
            }
        });

        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.create().show();
    }

    // function to redirect user to PhotoActivity Settings

    private void redirectToAppSettings(PhotosActivity photosActivity) {

        String packageName = this.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }



    /* below are functions generated by AppCompatActivity*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
