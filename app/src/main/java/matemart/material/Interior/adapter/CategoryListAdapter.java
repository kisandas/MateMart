package matemart.material.Interior.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.example.CategoryModel;
import matemart.material.Interior.R;
import matemart.material.Interior.activities.SearchProductFromCategoryActivity;
import matemart.material.Interior.activities.SubCategoryActivityList;
import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ItemViewHolder> {

    private ArrayList<CategoryModel> categoryList;
    private Context mContext;
    boolean isSubCategory;

    public CategoryListAdapter(ArrayList<CategoryModel> categoryList, Context context,boolean isSubCategory) {
        this.categoryList = categoryList;
        this.mContext = context;
        this.isSubCategory = isSubCategory;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view;
        if(isSubCategory){
            view = layoutInflater.inflate(R.layout.item_sub_category_adapter, parent, false);

        }else {
            view = layoutInflater.inflate(R.layout.item_category_adapter, parent, false);

        }
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(mContext).load(categoryList.get(position).getImage()).placeholder(R.drawable.place_holder_image).into(holder.iv_category_image);
        holder.tv_lable.setText(categoryList.get(position).getTitle());
        if (categoryList.get(position).getColor() != null && !isSubCategory) {
            try {
//                holder.iv_category_image.setBackgroundColor(Color.parseColor(categoryList.get(position).getColor()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

//        holder.tv_sub_lable.setText(viewList.get(position).getSubLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (viewList.get(position).getClickID() != null && !viewList.get(position).getClickID().equalsIgnoreCase("null") && !viewList.get(position).getClickID().isEmpty()) {
//                    Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), viewList.get(position).getClickID(), holder, false, true);
//                }

                if(isSubCategory){
                    mContext.startActivity(new Intent(mContext, SearchProductFromCategoryActivity.class).putExtra("c_id",categoryList.get(position).getCId()).putExtra("title",categoryList.get(position).getTitle()));

                }else {
                    mContext.startActivity(new Intent(mContext, SubCategoryActivityList.class).putExtra("c_id",categoryList.get(position).getCId()).putExtra("title",categoryList.get(position).getTitle()));
                }
            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_category_image;
        TextView tv_lable;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_category_image = itemView.findViewById(R.id.iv_product_image);
            tv_lable = itemView.findViewById(R.id.tv_lable);
        }

    }
}