package matemart.material.interior.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import matemart.material.interior.R

class ChoosePictureBottomSheetFragment(var type: String, var height: Int) :
    BottomSheetDialogFragment() {
    var ll_bottomsheet: ConstraintLayout? = null
    var iv_select_from_phone: LinearLayout? = null
    var iv_select_from_camera: LinearLayout? = null
    var iv_close: ImageView? = null
    var isCamera = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }
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
            val hasStoragePermissions = matemart.material.interior.utils.Utils.hasPermissions(
                context, *arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            if (hasStoragePermissions) {

            } else {
//                ask Permission
            }
        })
        iv_select_from_camera?.setOnClickListener(View.OnClickListener {
            val hasAndroidPermissions = matemart.material.interior.utils.Utils.hasPermissions(
                context, *arrayOf(
                    Manifest.permission.CAMERA
                )
            )
            if (hasAndroidPermissions) {

            } else {
//                 ask Permission
            }
        })

        dialog.setContentView(view)
    }






}