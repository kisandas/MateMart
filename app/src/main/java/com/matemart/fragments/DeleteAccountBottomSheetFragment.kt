package com.matemart.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.matemart.R
import com.matemart.activities.onDeleteClickListener
import com.matemart.adapter.StateSelectionAdapter
import com.matemart.model.ArchitectContact


class DeleteAccountBottomSheetFragment(
    var type: String, private val listener: onDeleteClickListener
) : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.fragment_bottom_sheet_delete_account, null)

        view.findViewById<TextView>(R.id.btn_delete).setOnClickListener {
            listener.onDeleteClicked()
            dismiss()
        }
        view.findViewById<TextView>(R.id.btn_cancel).setOnClickListener { dismiss() }
        view.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dismiss() }

        dialog.setContentView(view)
    }

}