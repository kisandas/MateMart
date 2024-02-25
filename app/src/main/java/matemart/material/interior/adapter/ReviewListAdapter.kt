package matemart.material.interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import matemart.material.interior.databinding.ItemReviewListBinding
import matemart.material.interior.model.Architect
import matemart.material.interior.model.Review

class ReviewListAdapter(
    var context: Context,
    var list: List<Review>
) :
    RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemReviewListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = list.get(position).uname
        holder.binding.tvDate.text = list.get(position).date
        holder.binding.tvRating.text = list.get(position).rating
        holder.binding.tvReview.text = list.get(position).review
        Glide.with(context).load(list.get(position).profile_image).into(holder.binding.ivProfile)

        if(position == list.size-1){
            holder.binding.viewBottom.visibility = GONE
        }


    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}