package com.example.myapplication.Utils;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

public class CameraHelper {

    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_READ_REQUEST_CODE = 102;

    private Activity activity;
    private Uri imageUri;
    private OnImageSelectedListener onImageSelectedListener;

    public CameraHelper(Activity activity) {
        this.activity = activity;
    }
    public interface OnImageSelectedListener {
        void onImageSelected(Uri imageUri);
    }

    public void pickImageCamera(ActivityResultLauncher<Intent> cameraActivityResultLauncher) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample description");
        imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!cameraAccepted) {
                        Toast.makeText(activity, "Camera & Storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case STORAGE_READ_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageReadAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if (!storageReadAccepted) {
                        Toast.makeText(activity, "Storage read permissions are required", Toast.LENGTH_SHORT).show();
                    } else {
                        if (onImageSelectedListener != null) {
                            onImageSelectedListener.onImageSelected(imageUri);
                        }
                    }
                } else {
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}

