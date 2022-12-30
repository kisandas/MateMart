package com.matemart.fragments;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.matemart.R;
import com.matemart.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.InputStream;

public class ChoosePictureBottomSheetFragment extends BottomSheetDialogFragment {

    ConstraintLayout ll_bottomsheet;
    String type;
    Context context;
    int height;
    ImageView iv_select_from_phone;
    ImageView iv_select_from_camera;
    ImageView iv_close;

    public static int REQUEST_IMAGE_CAPTURE = 109;

    public ChoosePictureBottomSheetFragment( String type, int height, Context context) {
        this.type = type;
        this.context = context;

        this.height = height;

    }

    boolean isCamera = false;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_upload_receipt, null);


        ll_bottomsheet = view.findViewById(R.id.cl_bottom_layout);
        iv_select_from_phone = view.findViewById(R.id.ll_galary);
        iv_select_from_camera = view.findViewById(R.id.ll_camera);
        iv_close = view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        iv_select_from_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasStoragePermissions = Utils.hasPermissions(context, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                });

                if (hasStoragePermissions) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    isCamera = false;
                    intent.putExtra("code", 201);
                    activityResultLauncher.launch(intent);
                } else {
//                    Intent i = new Intent(context, PermissionStorageActivity.class);
//                    startActivity(i);
//                    requestMultiplePermissions("phone");
                }

            }
        });

        iv_select_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasAndroidPermissions = Utils.hasPermissions(context, new String[]{
                        Manifest.permission.CAMERA
                });

                if (hasAndroidPermissions) {
                    isCamera = true;
                    dispatchTakePictureIntent();
                } else {
//                    Intent i = new Intent(context, PermissionCameraActivity.class);
//                    startActivity(i);
                }
            }
        });

        displayResult();
        dialog.setContentView(view);
    }


    ActivityResultLauncher activityResultLauncher;

    private void displayResult() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == RESULT_OK && !isCamera) {

                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    InputStream inputStream = context.getContentResolver().openInputStream(selectedImageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    String selectedImagePath = getPathFormatUri(selectedImageUri);
                                    // TODO: 11-10-2022 commented this for dismiss
//                                    listner.onImageSelection(selectedImagePath);
                                    CropImage.activity(selectedImageUri).setFixAspectRatio(true).setAspectRatio(1, 1)
                                            .start(getContext(), this);
                                } catch (Exception e) {

//                                    new Toaster.Builder(context)
//                                            .setTitle(ERROR_TITLE)
//                                            .setDescription(ERROR_MESSAGE)
//                                            .setDuration(5000)
//                                            .setStatus(Toaster.Status.ERROR)
//                                            .show();
                                }
                            }
                        }
                    } else {

                        if (fileUri != null) {
                            imgUri = Uri.fromFile(fileUri);
                            if (new File(imgUri.getPath()).length() > 0) {
                                // TODO: 11-10-2022 commented this for dismiss
                                CropImage.activity(imgUri).setFixAspectRatio(true).setAspectRatio(1, 1)
                                        .start(getContext(), this);
//                                listner.onImageSelection(imgUri.getPath());
                            }
                        }
                    }
                });
    }

    private String getPathFormatUri(Uri contentUri) {
        String filePath;
        Cursor cursor = context.getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }

        return filePath;
    }


    private File fileUri = null;
    private Uri imgUri;

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            fileUri = Utils.createImageFile(context);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getUriForFileProvider(context, fileUri));
        } catch (Exception ignored) {
        }
        activityResultLauncher.launch(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}