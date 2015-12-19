package com.example.shashankshekhar.application3s1.Camera;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.shashankshekhar.application3s1.R;
@SuppressWarnings("deprecation") // we need to support api level till 19 so we are using deprecated class.
public class CameraActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Camera camera = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        startCamera();
    }
    @Override
    protected void onPause () {
        super.onPause();
        Log.d("sparta", "surface destroyed 2");
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("sparta", "surface destroyed 3");
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void startCamera() {
        if (doesCameraExist(this)) {
            final Camera camera = getcameraInstance();
            final CameraPreview cameraPreview = new CameraPreview(this,camera);
            final Button captureButton = (Button)findViewById(R.id.button_capture1);
            FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview1);
            preview.addView(cameraPreview);

            captureButton.setOnClickListener(
                    new View.OnClickListener () {
                        @Override
                        public void onClick (View view) {
                            captureButton.setEnabled(false);
                            camera.takePicture(null, null, mPicture);

                        }
                    }
            );
        }
        else {
            showToast("camera unavailable");
        }
    }


    private boolean doesCameraExist(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    public  Camera getcameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            showToast("could not start camera. Release camera used by any other application");
        }
        return camera;
    }
    private  void showToast (String string) {
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
        toast.show();
    }
    private Camera.PictureCallback mPicture= new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("sparta", "error creating media file, check permissions");
            }
            try {
                FileOutputStream fos =  new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                final Button captureButton = (Button)findViewById(R.id.button_capture1);
                captureButton.setEnabled(true);
                camera.startPreview();
                Log.d("sparta","file write successful");
            } catch (FileNotFoundException ex) {
                Log.d("sparta","file not found" + ex.getMessage());
            } catch (IOException ex) {
                Log.d("sparta","io exception" + ex.getMessage());
            }
        }
    };
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        Log.d("sparta","file path is "+mediaStorageDir);
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "SmartCampusIMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
