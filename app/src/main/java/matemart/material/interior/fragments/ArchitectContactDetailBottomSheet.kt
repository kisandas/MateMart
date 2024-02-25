package matemart.material.interior.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import matemart.material.interior.adapter.StateSelectionAdapter
import matemart.material.interior.R
import matemart.material.interior.model.ArchitectContact

class ArchitectContactDetailBottomSheet(
    var type: String, var contact: ArchitectContact,var listener:onDismissListener
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismissed()
    }

}

interface onDismissListener{
    fun onDismissed()
}