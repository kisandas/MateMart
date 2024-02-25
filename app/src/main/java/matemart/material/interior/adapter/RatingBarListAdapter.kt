package matemart.material.interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import matemart.material.interior.databinding.ItemRatingBarLayoutBinding
import matemart.material.interior.model.Architect
import matemart.material.interior.model.Rating

class RatingBarListAdapter(
    var context: Context,
    var list: List<Rating>,
    var totalRating:Double
) :
    RecyclerView.Adapter<RatingBarListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemRatingBarLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRatingBarLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return 5

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.Rating.text = "${position+1}"
        if(list.isNotEmpty()) {
            holder.binding.ratingCount.text = list[position].count.toString()
            holder.binding.progressRating.max = totalRating.toInt()
            holder.binding.progressRating.progress = list[position].count
        }

    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}