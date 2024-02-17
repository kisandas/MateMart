package matemart.material.Interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import matemart.material.Interior.R
import matemart.material.Interior.model.FilterBody


class FilterKeyAdapter(private val context:Context,
    private var keyList: LinkedHashMap<String, List<FilterBody>>,
    private var key: String,
    private val onClick: (key: String, list: ArrayList<FilterBody>?) -> Unit
) :
    RecyclerView.Adapter<FilterKeyAdapter.ViewHolder>() {

    private var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.item_rc_key_filter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    fun setData(keyList: LinkedHashMap<String, List<FilterBody>>,key:String) {
        this.keyList.clear()
        this.keyList = LinkedHashMap(keyList)
        this.key = key

        notifyDataSetChanged()
    }

    fun getData(): LinkedHashMap<String, List<FilterBody>> {
        return keyList
    }

    // This method is called when binding the data to the views being created in RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keys = keyList.keys.toList()
        holder.bindData(keys[position])
    }

    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvState: CheckBox

        init {
            tvState = itemView.findViewById(R.id.tvState)
        }

        fun bindData(keyValue: String) {
            tvState.text = keyValue

//            if(keyValue == key){
//            tvState.background =context.getDrawable(R.drawable.white_drawable)
//            }else{
//                tvState.background =context.getDrawable(R.drawable.default_background)
//            }



            tvState.setOnClickListener {
                val position = adapterPosition


                onClick(
                    keyValue,
                    ArrayList(keyList[keyValue]!!)
                )
                // Update selected position
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position
                    notifyDataSetChanged() // Refresh the view to apply the selector
                }
//                tvState.background =context.getDrawable(R.drawable.white_drawable)

            }
            tvState.isChecked = adapterPosition == selectedPosition
        }
    }
}