package com.example.mytestapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.hardware.Camera;


public class MainActivity extends AppCompatActivity{

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private Camera mCamera;
    private CameraPreview mPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCameraAccess = findViewById(R.id.buttonCameraAccess);
        buttonCameraAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // Check Camera Permission already granted or not
                if (checkPermission()) {
                    Toast.makeText(MainActivity.this, "Camera permission is granted", Toast.LENGTH_SHORT).show();
                    startCamera();
                } else {
                    requestPermission();
                }
            }else {
                    Toast.makeText(MainActivity.this,"Camera feature is not available",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Camera  permission granted", Toast.LENGTH_SHORT).show();
             getCameraInstance();
            startCamera();
        } else {
            Toast.makeText(MainActivity.this, "Camera  permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }


    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


    private void startCamera(){
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(MainActivity.this,mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }


//    public void showMessageOKCancel() {
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        requestPermission();
//                    }
//                });
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
//    }


}
