package matemart.material.Interior.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import matemart.material.Interior.R
import matemart.material.Interior.interfaces.DismissBottomSheet
import java.util.ArrayList

class StateSelectionAdapter     // Counstructor for the Class
     (// List to store all the contact details
    private val cityList: ArrayList<String>?,
    var dismiss_listner: DismissBottomSheet,
    var type: String
) : RecyclerView.Adapter<StateSelectionAdapter.ViewHolder>() {
    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        val view = layoutInflater.inflate(R.layout.item_rc_state_city, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityList?.size ?: 0
    }

    // This method is called when binding the data to the views being created in RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvState.text = cityList!![position]
        holder.tvState.setOnClickListener {
            dismiss_listner.DismissDialog(
                position,
                cityList[position],
                type
            )
        }
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
    }

    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val tvState: TextView

        init {
            tvState = itemView.findViewById(R.id.tvState)
        }
    }
}