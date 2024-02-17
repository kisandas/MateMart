package matemart.material.Interior.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import matemart.material.Interior.R;
import matemart.material.Interior.activities.MyOrderDetailActivity;
import matemart.material.Interior.model.Content;

import matemart.material.Interior.activities.MyOrderDetailActivity;
import matemart.material.Interior.model.Content;
import matemart.material.Interior.activities.MyOrderDetailActivity;
import matemart.material.Interior.model.Content;

import java.util.ArrayList;

public class OrderInnerListAdapter extends RecyclerView.Adapter<OrderInnerListAdapter.ItemViewHolder> {

    private ArrayList<Content> data;
    private Context mContext;

    public OrderInnerListAdapter(ArrayList<Content> data, Context context) {
        this.data = data;
        this.mContext = context;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_layout_order_fragment, parent, false);


        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
//        return 20;
        return data == null ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_order_no.setText("" + data.get(position).getOrderNumber());
        holder.tv_price.setText("₹" + data.get(position).getPrice());
        holder.tv_qty.setText("" + data.get(position).getQty());

        if (data.get(position).getDate() != null)
            holder.tv_date.setText("" + data.get(position).getDate());

        if (data.get(position).getOdstatus() == 4) {
            holder.tv_status.setVisibility(View.VISIBLE);
        } else {
            holder.tv_status.setVisibility(View.INVISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MyOrderDetailActivity.class).putExtra("o_id",data.get(position).getOId()));
            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_order_no;
        TextView tv_product_name;
        TextView tv_price;
        TextView tv_qty;
        TextView tv_status;
        TextView tv_date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_date = itemView.findViewById(R.id.tv_date);

        }

    }
}