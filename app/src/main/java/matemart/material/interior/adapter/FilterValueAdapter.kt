package matemart.material.interior.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import matemart.material.interior.R
import matemart.material.interior.activities.SearchProductFromCategoryActivity.Companion.maxPrice
import matemart.material.interior.activities.SearchProductFromCategoryActivity.Companion.minPrice
import matemart.material.interior.activities.SearchProductFromCategoryActivity.Companion.selectedMap
import matemart.material.interior.model.FilterBody


class FilterValueAdapter(
    private var valueList: ArrayList<FilterBody>,
    private var keyValue: String,
    private val onClickValue: (key: String, data: FilterBody, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view: View
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.item_rc_value_inner, parent, false)
            return ViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_rc_price_value_inner, parent, false)
            return PriceViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            (holder).bindData(valueList[position],position)
        } else {

        }
    }

    override fun getItemCount(): Int {
        return this.valueList.size
    }

    fun setData(valueList: List<FilterBody>, keyValue: String) {
        this.valueList.clear()
        Log.e("checkSizeeeee", "clear size: " + this.valueList.size)
        this.valueList.addAll(valueList)
        this.keyValue = keyValue
        notifyDataSetChanged()
        Log.e("checkSizeeeee", "set size: " + this.valueList.size)

    }

    // This method is called when binding the data to the views being created in RecyclerView


    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvState: TextView
        val icCheckBox: CheckBox

        init {
            tvState = itemView.findViewById(R.id.tvState)
            icCheckBox = itemView.findViewById(R.id.icCheckBox)

        }

        fun bindData(filterBody: FilterBody,position: Int) {

            tvState.text = filterBody.name.toString()

            selectedMap?.get(keyValue)?.let { itemList ->
                var found = false // Track if a match is found

                for (item in itemList) {
                    if (item.name == filterBody.name) {
                        found = true // Set found to true if a match is found
                        break // Exit the loop once a match is found
                    }
                }

                // Modify the checkbox status after the loop
                icCheckBox.isChecked = found
            }
            Log.e("chhhhhhhhhhhhhh", "bindData: "+filterBody.name +"        state: "+filterBody.is_disabled)
            if (filterBody.is_disabled) {
                icCheckBox.isClickable = false
                icCheckBox.isEnabled = false
                tvState.isClickable = false
                tvState.isEnabled = false
            } else {
                icCheckBox.isClickable = true
                icCheckBox.isEnabled = true
                tvState.isClickable = true
                tvState.isEnabled = true
            }

            tvState.setOnClickListener {
                if (filterBody.is_disabled) {

                } else {
                    icCheckBox.isChecked = !icCheckBox.isChecked
                    onClickValue(keyValue, filterBody, icCheckBox.isChecked)
                }

            }

            icCheckBox.setOnClickListener {
                if (filterBody.is_disabled) {

                } else {
                    icCheckBox.isChecked = !icCheckBox.isChecked
                    onClickValue(keyValue, filterBody, icCheckBox.isChecked)
                }
            }
            itemView.setOnClickListener {
                if (filterBody.is_disabled) {

                } else {
                    icCheckBox.isChecked = !icCheckBox.isChecked
                    onClickValue(keyValue, filterBody, icCheckBox.isChecked)
                }
            }
            icCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

//                onClickValue(keyValue, filterBody, icCheckBox.isChecked)
            }
        }
    }


    inner class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etMinPrice: EditText
        val etMaxPrice: EditText

        init {
            etMinPrice = itemView.findViewById(R.id.etMinPrice)
            etMaxPrice = itemView.findViewById(R.id.etMaxPrice)


            etMinPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // This method is called before the text is changed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // This method is called when the text is being changed
                    val text = s.toString()
                    updateActualMap(etMinPrice.text.toString(), etMaxPrice.text.toString())


                    // Do something with the changed text
                    // For example, you might want to update a variable with the new minimum price
                }

                override fun afterTextChanged(s: Editable?) {
                    // This method is called after the text has been changed
                }
            })

            etMaxPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // This method is called before the text is changed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // This method is called when the text is being changed
                    val text = s.toString()
                    updateActualMap(etMinPrice.text.toString(), etMaxPrice.text.toString())

                    // Do something with the changed text
                    // For example, you might want to update a variable with the new minimum price
                }

                override fun afterTextChanged(s: Editable?) {
                    // This method is called after the text has been changed
                }
            })
        }

        private fun updateActualMap(text1: String, text2: String) {
            val doubleText1 = text1.toDoubleOrNull()
            val doubleText2 = text2.toDoubleOrNull()


            // Check for null or empty conditions before converting to doubles
            if (text1.isNotEmpty() && doubleText1 != null && text2.isNotEmpty() && doubleText2 != null) {

                minPrice = text1.toDouble()
                maxPrice = text2.toDouble()
//                val filterBodyList = mutableListOf<FilterBody>()
//
//                // Ensure the list size remains fixed at 2 elements
//                if (filterBodyList.size < 2) {
//                    filterBodyList.add(FilterBody(doubleText1)) // Replace FilterBody with your actual class
//                    filterBodyList.add(FilterBody(doubleText2)) // Replace FilterBody with your actual class
//                } else {
//                    filterBodyList[0] = FilterBody(doubleText1)
//                    filterBodyList[1] = FilterBody(doubleText2)
//                }
//
//                // Update actualMap with the "price" key and the filterBodyList
//                actualMap?.put("price", filterBodyList)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        if (keyValue == "price") {
            return 1
        } else {
            return 0
        }

    }
}