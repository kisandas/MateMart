package matemart.material.Interior.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import matemart.material.Interior.R
import matemart.material.Interior.adapter.StateSelectionAdapter
import matemart.material.Interior.interfaces.DismissBottomSheet
import matemart.material.Interior.model.LabourFilter

class LabourFilterBottomSheet(
    var list: MutableList<LabourFilter>,
    var dismiss_listner: DismissBottomSheet
) : BottomSheetDialogFragment(), DismissBottomSheet {

    var ll_bottomsheet: ConstraintLayout? = null
    var iv_close: ImageView? = null
    var rcState: RecyclerView? = null
    var adapter: StateSelectionAdapter? = null
    var stateList: ArrayList<String> = arrayListOf()


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        for (labour in list) {
            labour.name?.let { stateList.add(it) }
        }
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
        adapter = StateSelectionAdapter(stateList, this, "")
        rcState?.setAdapter(adapter)
        dialog.setContentView(view)
    }

    override fun DismissDialog(position: Int, state: String?, type: String?) {
        Log.d("Bottomsheet", position.toString() + " " + state + " " + type)
        dismiss_listner.DismissDialog(position, state, type)
        dismiss()
    }


}