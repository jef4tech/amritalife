package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by ASHIK on 13-05-2017.
 */

public class DespatchDetailsAdapter extends RecyclerView.Adapter<DespatchDetailsAdapter.MyViewHolder> {
    private static final String TAG = "Despatch Details Adapter";
    ArrayList<DespatchItem> despatchItems = new ArrayList<>();
    private Context context;

    public DespatchDetailsAdapter(Context context, ArrayList<DespatchItem> despatchItems) {
        this.context = context;
        this.despatchItems = despatchItems;
    }

    public void update(ArrayList<DespatchItem> despatchItems) {
        this.despatchItems.clear();
        this.despatchItems = despatchItems;
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_despatch_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DespatchItem despatchItem = despatchItems.get(position);
        holder.txtItemCount.setText(String.valueOf(despatchItem.getQuantity()));
        holder.txtProduct.setText(despatchItem.getItemName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return despatchItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtProduct, txtItemCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProductName);
            txtItemCount = (TextView) itemView.findViewById(R.id.txtItemCount);

        }
    }
}
