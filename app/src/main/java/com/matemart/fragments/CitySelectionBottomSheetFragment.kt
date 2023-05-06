package com.matemart.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.matemart.interfaces.DismissBottomSheet
import com.matemart.adapter.StateSelectionAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matemart.R
import java.util.ArrayList

class CitySelectionBottomSheetFragment(
    var type: String,
    var height: Int,
    var dismiss_listner: DismissBottomSheet,
    var stateList: ArrayList<String>
) : BottomSheetDialogFragment(), DismissBottomSheet {
    var ll_bottomsheet: ConstraintLayout? = null
    var iv_close: ImageView? = null
    var rcState: RecyclerView? = null
    var adapter: StateSelectionAdapter? = null
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.fragment_bottom_sheet_state_selection, null)
        ll_bottomsheet = view.findViewById(R.id.cl_bottom_layout)
        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 400)
        iv_close = view.findViewById(R.id.iv_close)
        rcState = view.findViewById(R.id.rcState)
        iv_close?.setOnClickListener(View.OnClickListener { dismiss() })
        val layoutManager = LinearLayoutManager(
            context
        )
        rcState?.setLayoutManager(layoutManager)
        adapter = StateSelectionAdapter(stateList,  this, type)
        rcState?.setAdapter(adapter)
        dialog.setContentView(view)
    }



    override fun DismissDialog(position: Int, state: String?, type: String?) {
        dismiss_listner.DismissDialog(position, state, type)
        dismiss()
    }
}