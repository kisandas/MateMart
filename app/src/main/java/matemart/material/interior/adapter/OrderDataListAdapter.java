package matemart.material.interior.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import matemart.material.interior.R;
import matemart.material.interior.model.OrderData;

import java.util.ArrayList;

public class OrderDataListAdapter extends RecyclerView.Adapter<OrderDataListAdapter.ItemViewHolder> {

    private ArrayList<OrderData> orderData;
    private Context mContext;

    public OrderDataListAdapter(ArrayList<OrderData> orderData, Context context) {

        this.orderData = orderData;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_order_outer_layout, parent, false);


        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return orderData == null ? 0 : orderData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvOrderDate.setText(orderData.get(position).getTitle());
        Log.e("kkkkk", "onBindViewHolder: " + orderData.get(position).getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        OrderInnerListAdapter adapter = new OrderInnerListAdapter(orderData.get(position).getContent(), mContext);

        holder.rcOrderInner.setLayoutManager(layoutManager);
        holder.rcOrderInner.setAdapter(adapter);


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderDate;
        RecyclerView rcOrderInner;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            rcOrderInner = itemView.findViewById(R.id.rcOrderInner);
        }

    }
}