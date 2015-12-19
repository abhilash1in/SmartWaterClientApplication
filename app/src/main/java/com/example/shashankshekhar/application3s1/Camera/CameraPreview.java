package com.example.shashankshekhar.application3s1.Camera;

import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.security.Policy;

/**
 * Created by shashankshekhar on 29/10/15.
 */
@SuppressWarnings("deprecation") // we need to support api level till 19 so we are using deprecated class.
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    public CameraPreview (Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);

    }
    public void surfaceCreated (SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("sparta","error setting camera preview");

        }
    }
    public void surfaceDestroyed (SurfaceHolder surfaceHolder) {
        // take care of releasing the camera
        Log.d("sparta","surface destroyed 1");
        if (mCamera!=null) {
            mCamera.release();
            mCamera = null;
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
//        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        Display display1 =
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(w,h);
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("sparta", "Error starting camera preview: " + e.getMessage());
        }
    }
}

