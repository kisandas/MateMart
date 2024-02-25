package matemart.material.interior.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import matemart.material.interior.R;
import matemart.material.interior.activities.ItemClickListener;

import java.util.ArrayList;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.ItemViewHolder> {

    private ArrayList<String> categoryList;
    private Context mContext;
    ItemClickListener listner;

    public SearchResultListAdapter(ArrayList<String> categoryList, Context context, ItemClickListener listner) {
        this.categoryList = categoryList;
        this.mContext = context;
        this.listner = listner;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_result_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tv_lable.setText(categoryList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onSearchItemClick(categoryList.get(position));
//                if (viewList.get(position).getClickID() != null && !viewList.get(position).getClickID().equalsIgnoreCase("null") && !viewList.get(position).getClickID().isEmpty()) {
//                    Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), viewList.get(position).getClickID(), holder, false, true);
//                }

            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_lable;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_lable = itemView.findViewById(R.id.tv_lable);
        }

    }
}