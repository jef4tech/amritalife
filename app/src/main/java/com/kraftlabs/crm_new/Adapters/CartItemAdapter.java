package com.kraftlabs.crm_new.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kraftlabs.crm_new.Fragments.OrderCreationFragment;
import com.kraftlabs.crm_new.Models.CartItem;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by ajith on 29/7/16.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private final String TAG = "ORDER ITEM ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private OrderCreationFragment fragment;


    public CartItemAdapter(Context context, ArrayList<CartItem> cartItems, OrderCreationFragment fragment) {
        this.context = context;
        this.cartItems = cartItems;
        this.fragment = fragment;
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

        holder.btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmRemove(cartItem);
            }
        });

        holder.orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_cart_item);
                dialog.setTitle(context.getResources().getString(R.string.enter_quantity));

                TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
                TextView txtPrice = (TextView) dialog.findViewById(R.id.txtPrice);
                EditText txtQuantity = (EditText) dialog.findViewById(R.id.txtQuantity);

                txtItemName.setText(cartItem.getProductName());
                txtPrice.setText(String.format("%.2f", cartItem.getPrice()));
                txtQuantity.setText(String.valueOf(cartItem.getQuantity()));
                txtPrice.requestFocus();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button dialogButtonRemove = (Button) dialog.findViewById(R.id.dialogButtonRemove);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtQuantity = (EditText) dialog.findViewById(R.id.txtQuantity);
                        String str = txtQuantity.getText().toString();
                        try {
                            int quantity = Integer.parseInt(str);
                            cartItem.setQuantity(quantity);
                            fragment.updateQuantityAndPrice();
                            update(cartItems);
                            dialog.dismiss();
                        } catch (Exception e) {
                          Toast.makeText(context, context.getResources().getString(R.string.pls_enter_valid_qty), Toast.LENGTH_LONG).show();
                           // Toasty.warning(context, context.getResources().getString(R.string.pls_enter_valid_qty), Toast.LENGTH_SHORT, true).show();

                        }

                    }
                });

                dialogButtonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmRemove(cartItem);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    private void confirmRemove(final CartItem cartItem) {
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.confirm))
                .setMessage(context.getResources().getString(R.string.do_you_remove))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        cartItems.remove(cartItem);
                        update(cartItems);
                        fragment.updateQuantityAndPrice();
                        Toast.makeText(context, context.getResources().getString(R.string.item_removed), Toast.LENGTH_SHORT).show();
                      //  Toasty.success(context, context.getResources().getString(R.string.item_removed), Toast.LENGTH_SHORT, true).show();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
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