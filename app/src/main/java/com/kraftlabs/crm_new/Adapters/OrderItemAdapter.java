package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.CartItem;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ajith on 29/7/16.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {

    private final String TAG = "ORDER ITEM ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<CartItem> cartItems = new ArrayList<>();


    public OrderItemAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public void update(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final CartItem cartItem = cartItems.get(position);


        holder.txtProductName.setText(cartItem.getProductName());
        holder.txtQuantity.setText("" + cartItem.getQuantity());
        holder.txtPrice.setText("" + cartItem.getPrice());
        holder.txtAmount.setText("" + cartItem.getPrice() * cartItem.getQuantity());

        holder.btnRemoveFromCart.setVisibility(View.GONE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtQuantity, txtPrice, txtAmount;
        public ImageView btnRemoveFromCart;
        public RelativeLayout orderItem;

        public MyViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.itemName);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            txtPrice = (TextView) view.findViewById(R.id.price);
            txtAmount = (TextView) view.findViewById(R.id.amount);
            btnRemoveFromCart = (ImageView) view.findViewById(R.id.btnRemoveFromCart);
            orderItem = (RelativeLayout) view.findViewById(R.id.orderItem);
        }
    }


}
