package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by ASHIK on 12-05-2017.
 */

public class DespatchTabAdapter extends RecyclerView.Adapter<DespatchTabAdapter.MyViewHolder> {
    private final String TAG = "DESPATCHEDTABADAPTER";
    ArrayList<DespatchItem> despatchItems = new ArrayList<>();
    private Context context;


    public DespatchTabAdapter(Context context, ArrayList<DespatchItem> despatchItems) {
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
                .inflate(R.layout.item_despatch_tab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DespatchItem despatchItem = despatchItems.get(position);

        holder.txtProductName.setText(despatchItem.getItemName());
        holder.txtLrNoDate.setText(despatchItem.getLrNumber() + " / " + despatchItem.getLrDate());
        holder.txtQuantity.setText(despatchItem.getQuantity() + "");

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
        TextView txtLrNoDate, txtQuantity, txtProductName;
        RelativeLayout despatchTabInvoicesDetails;

        public MyViewHolder(View view) {

            super(view);

            txtLrNoDate = (TextView) view.findViewById(R.id.txtLrNoDate);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            despatchTabInvoicesDetails = (RelativeLayout) view.findViewById(R.id.despatchedTabInvoicesDetails);

        }
    }
}
