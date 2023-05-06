package com.matemart.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.theartofdev.edmodo.cropper.CropImage
import com.matemart.R
import com.matemart.utils.Utils
import java.io.File
import java.lang.Exception

class ChoosePictureBottomSheetFragment(var type: String, var height: Int) :
    BottomSheetDialogFragment() {
    var ll_bottomsheet: ConstraintLayout? = null
    var iv_select_from_phone: ImageView? = null
    var iv_select_from_camera: ImageView? = null
    var iv_close: ImageView? = null
    var isCamera = false
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.fragment_bottom_sheet_upload_receipt, null)
        ll_bottomsheet = view.findViewById(R.id.cl_bottom_layout)
        iv_select_from_phone = view.findViewById(R.id.ll_galary)
        iv_select_from_camera = view.findViewById(R.id.ll_camera)
        iv_close = view.findViewById(R.id.iv_close)
        iv_close?.setOnClickListener(View.OnClickListener { dismiss() })
        iv_select_from_phone?.setOnClickListener(View.OnClickListener {
            val hasStoragePermissions = Utils.hasPermissions(
                context, *arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            if (hasStoragePermissions) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                isCamera = false
                intent.putExtra("code", 201)
//                activityResultLauncher!!.launch(intent)
            } else {
//                    Intent i = new Intent(context, PermissionStorageActivity.class);
//                    startActivity(i);
//                    requestMultiplePermissions("phone");
            }
        })
        iv_select_from_camera?.setOnClickListener(View.OnClickListener {
            val hasAndroidPermissions = Utils.hasPermissions(
                context, *arrayOf(
                    Manifest.permission.CAMERA
                )
            )
            if (hasAndroidPermissions) {
                isCamera = true
                dispatchTakePictureIntent()
            } else {
//                    Intent i = new Intent(context, PermissionCameraActivity.class);
//                    startActivity(i);
            }
        })
        displayResult()
        dialog.setContentView(view)
    }

    var activityResultLauncher: ActivityResultLauncher<*>? = null
    private fun displayResult() {
        activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && !isCamera) {
                if (data != null) {
                    val selectedImageUri = data.data
                    if (selectedImageUri != null) {
                        try {
                            val inputStream =
                                context?.contentResolver?.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val selectedImagePath = getPathFormatUri(selectedImageUri)
                            // TODO: 11-10-2022 commented this for dismiss
//                                    listner.onImageSelection(selectedImagePath);
                            CropImage.activity(selectedImageUri).setFixAspectRatio(true)
                                .setAspectRatio(1, 1)
                                .start(requireContext(), this)
                        } catch (e: Exception) {

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
                    imgUri = Uri.fromFile(fileUri)
                    if (File(imgUri?.getPath()).length() > 0) {
                        // TODO: 11-10-2022 commented this for dismiss
                        CropImage.activity(imgUri).setFixAspectRatio(true).setAspectRatio(1, 1)
                            .start(requireContext(), this)
                        //                                listner.onImageSelection(imgUri.getPath());
                    }
                }
            }
        }
    }

    private fun getPathFormatUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = context?.contentResolver?.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private var fileUri: File? = null
    private var imgUri: Uri? = null
    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            fileUri = Utils.createImageFile(context)
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT, Utils.getUriForFileProvider(
                    context, fileUri
                )
            )
        } catch (ignored: Exception) {
        }
//        activityResultLauncher!!.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    companion object {
        var REQUEST_IMAGE_CAPTURE = 109
    }
}