package com.example.myapplication.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.Utils.CameraHelper;
import com.example.myapplication.R;
import com.example.myapplication.dao.MomentDao;
import com.example.myapplication.entitys.MomentEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewMomentFragment extends Fragment {

    private static final int REQUEST_IMAGE_CROP = 3;
    private EditText editTextChapter;
    private EditText editTextPage;
    private Spinner editTextCategory;
    private EditText editTextMoment;

    private ImageView editPhoto;
    private int personalBookId;

    private Uri imageUri;

    private TextRecognizer textRecognizer;
    private ProgressDialog progressDialog;

    private CameraHelper cameraHelper;

    private ActivityResultLauncher<Intent> cropLauncher;

    public void setPersonalBookId(int personalBookId) {
        this.personalBookId = personalBookId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newmoment, container, false);
        progressDialog = new ProgressDialog(requireContext());

        Spinner spinnerCategory = view.findViewById(R.id.spinner);
        Button buttonCamera = view.findViewById(R.id.cameraButton);
        Button buttonText = view.findViewById(R.id.textButton);
        editPhoto = view.findViewById(R.id.editPhoto);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        registerCropActivityResult();

        cameraHelper = new CameraHelper(requireActivity());
        Button buttonTakePhoto = view.findViewById(R.id.takePhotoButton);
        ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = cameraHelper.getImageUri();
                        performCrop(imageUri);
                        editPhoto.setImageURI(imageUri);
                    }
                });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraHelper.pickImageCamera(cameraLauncher);
            }
        });
        Button recognizeButton = view.findViewById(R.id.recognizeButton);

        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizeTextFromImage();
            }
        });

        editTextChapter = view.findViewById(R.id.editChapter);
        editTextPage = view.findViewById(R.id.editPage);
        editTextCategory = view.findViewById(R.id.spinner);
        editTextMoment = view.findViewById(R.id.editTextMoment);

        Log.d("NewMomentFragment", "personalBookId: " + personalBookId);

        String[] categories = {"Emotional", "Romantic", "Inspirational","Mysterious",
                "Humorous","Informative","Adventure","Magical","Philosophical","Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhoto.setVisibility(View.VISIBLE);
                editTextMoment.setVisibility(View.INVISIBLE);
            }
        });

        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhoto.setVisibility(View.INVISIBLE);
                editTextMoment.setVisibility(View.VISIBLE);
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button saveButton = view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMoment();
            }
        });

        return view;
    }

    private void registerCropActivityResult() {
        cropLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap croppedBitmap = extras.getParcelable("data");
                            editPhoto.setImageBitmap(croppedBitmap);
                            imageUri = getImageUri(requireContext(), croppedBitmap);
                        }
                    }
                });
    }

    private void saveMoment() {
        String chapter = editTextChapter.getText().toString();
        String page = editTextPage.getText().toString();
        String category = editTextCategory.getSelectedItem().toString();
        String body = editTextMoment.getText().toString();

        MomentEntity momentEntity = new MomentEntity();
        momentEntity.setChapter("Chapter: "+chapter);
        momentEntity.setPage("Page: "+page);
        momentEntity.setCategory(category);
        momentEntity.setBody(body);
        momentEntity.setPersonalBookId(personalBookId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
                MomentDao momentDao = appDatabase.momentDao();
                momentDao.insertMoment(momentEntity);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
            }
        }).start();
    }

    private void recognizeTextFromImage() {
        progressDialog.setMessage("Preparing image");
        progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(requireContext(), imageUri);
            progressDialog.setMessage("Recognizing text");

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String recognizedTextSuccess = text.getText();
                            editTextMoment.setText(recognizedTextSuccess);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(requireContext(), "Failed preparing image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);

            cropLauncher.launch(cropIntent);

        } catch (ActivityNotFoundException e) {
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}
