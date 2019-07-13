package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Fragments.OrderCreationFragment;
import com.kraftlabs.crm_new.Fragments.OrderDetailsFragment;
import com.kraftlabs.crm_new.Models.Order;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ajith on 29/7/16.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private final String TAG = "ORDER ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<Order> orders = new ArrayList<>();


    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;

    }

    public void update(ArrayList<Order> orders) {
        this.orders.clear();
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Order order = orders.get(position);
        holder.txtCustomerName.setText(order.getCustomerName() + "(" + order.getCustomerCode() + ")");
        holder.txtOrderNumber.setText(context.getResources().getString(R.string.order_no) + (TextUtils.isEmpty(order.getOrderNumber()) ? "" : order.getOrderNumber()));
        if (TextUtils.isEmpty(order.getOrderSyncDate())) {
            holder.txtOrderDate.setText(order.getOrderDate());
        } else {
            holder.txtOrderDate.setText(order.getOrderSyncDate());
        }
        holder.txtNumberOfItems.setText(order.getNumberofItems() + "");
        holder.txtGrossAmount.setText(order.getGrossAmount() + "");
        if (order.getOrderStatus().equals(context.getResources().getString(R.string.synced))) {
            holder.txtStatus.setText(context.getResources().getString(R.string.saved));
        } else {
            holder.txtStatus.setText(order.getOrderStatus());
        }

        if (order.getOrderStatus().equals(context.getResources().getString(R.string.saved))) {
            holder.txtSyncStatus.setText(context.getResources().getString(R.string.not_send));
        }
        if (order.getOrderStatus().equals("Synced") || order.getOrderStatus().equals("Saved")) {
            holder.order.setOnClickListener(view -> {
                ShoppingCart.setCart(null);
                Fragment fragment = OrderDetailsFragment.newInstance((int) order.getId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
              FragmentManager fragmentManager = activity.getSupportFragmentManager();
                 FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            });
        } else {
            holder.order.setOnClickListener(view -> {
                ShoppingCart.setCart(null);
                Fragment fragment = OrderCreationFragment.newInstance((int) order.getId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCustomerName, txtOrderNumber, txtOrderDate, txtNumberOfItems, txtGrossAmount, txtStatus, txtSyncStatus;
        public RelativeLayout order;

        public MyViewHolder(View view) {
            super(view);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
            txtOrderNumber = (TextView) view.findViewById(R.id.txtOrderNumber);
            txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
            txtNumberOfItems = (TextView) view.findViewById(R.id.txtNumberOfItems);
            txtGrossAmount = (TextView) view.findViewById(R.id.txtGrossAmount);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtSyncStatus = (TextView) view.findViewById(R.id.txtSyncStatus);
            order = (RelativeLayout) view.findViewById(R.id.order);
        }
    }


}
