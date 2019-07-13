package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Fragments.CustomerFragment;
import com.kraftlabs.crm_new.Models.OrderItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


/**
 * User: ashik
 * Date: 13/12/17
 * Time: 5:09 PM
 */

public class ItemStatusAdapter extends RecyclerView.Adapter<ItemStatusAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<OrderItem> items = new ArrayList<>();

    public ItemStatusAdapter(Context context, ArrayList<OrderItem> items) {
        this.context = context;
        this.items = items;
    }


    public void update(ArrayList<OrderItem> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_status, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ItemStatusAdapter.MyViewHolder holder, int position) {
        final OrderItem item = items.get(position);
        holder.txtProductName.setText(item.getProductName());
        holder.txtProductCode.setText(item.getProductCode());
        holder.txtCategory.setText(item.getCategory().toString().trim());
        holder.txtPrice.setText(item.getPrice().toString());
        holder.txtDate.setText(item.getDate());
        holder.btnCount.setText(item.getItemCount()+"");
        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("PRODUCT_ID",item.getProductId());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = CustomerFragment.newInstance(false, true);
                fragment.setArguments(bundle);
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtProductCode, txtCategory, txtPrice, txtDate, btnCount;

        public RelativeLayout product;

        public MyViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtProductCode = (TextView) view.findViewById(R.id.txtProductCode);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            txtDate = view.findViewById(R.id.txtDate);
            btnCount=view.findViewById(R.id.btnCount);
            product = (RelativeLayout) view.findViewById(R.id.rlStatusProduct);
        }
    }
}
