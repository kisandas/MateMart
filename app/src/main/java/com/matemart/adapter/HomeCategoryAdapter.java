package com.matemart.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.matemart.R;
import com.matemart.model.ViewListModel;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ItemViewHolder> {

    private ArrayList<ViewListModel> viewList;
    private Context mContext;

    public HomeCategoryAdapter(ArrayList<ViewListModel> viewList, Context context) {
        this.viewList = viewList;
        this.mContext = context;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_home_category, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return viewList == null ? 0 : viewList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(mContext).load(viewList.get(position).getImage()).placeholder(R.drawable.img_loading_image).into(holder.iv_category_image);
        holder.tv_lable.setText(viewList.get(position).getTitle());
        if (viewList.get(position).getColor() != null) {
            try {
                holder.iv_category_image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(viewList.get(position).getColor())));
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
            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_category_image;
        TextView tv_lable;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_category_image = itemView.findViewById(R.id.iv_category_image);
            tv_lable = itemView.findViewById(R.id.tv_lable);
        }

    }
}