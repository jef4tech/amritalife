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

public class InvoicesDetailsAdapter extends RecyclerView.Adapter<InvoicesDetailsAdapter.MyViewHolder> {
    private static final String TAG = "Invoice Details Adapter";
    ArrayList<DespatchItem> despatchItems = new ArrayList<>();
    private Context context;
    private Boolean isListing;

    public InvoicesDetailsAdapter(Context context, ArrayList<DespatchItem> despatchItems) {
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
                .inflate(R.layout.item_invoice_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DespatchItem despatchItem = despatchItems.get(position);
        holder.txtProductName.setText(despatchItem.getItemName());
        holder.txtQty.setText(despatchItem.getQuantity() + "");
        holder.txtValue.setText(despatchItem.getValue() + "");

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
        TextView txtSKU, txtQty, txtValue, txtProductName;

        public MyViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtQty = (TextView) view.findViewById(R.id.txtQty);
            txtValue = (TextView) view.findViewById(R.id.txtValue);
        }
    }
}
