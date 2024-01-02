package com.matemart.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.matemart.interfaces.DismissBottomSheet
import com.matemart.adapter.StateSelectionAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matemart.R
import com.matemart.model.ArchitectContact
import java.util.ArrayList

class ArchitectContactDetailBottomSheet(
    var type: String,var contact: ArchitectContact
) : BottomSheetDialogFragment() {

    var adapter: StateSelectionAdapter? = null
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.fragment_bottom_sheet_architech_details, null)

        view.findViewById<EditText>(R.id.etName).setText(contact.name)
        view.findViewById<EditText>(R.id.etMobile).setText(contact.mobile)

        dialog.setContentView(view)
    }

}