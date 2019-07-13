package com.kraftlabs.crm_new.Adapters;

import android.app.Dialog;
import android.content.Context;
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
import com.kraftlabs.crm_new.Models.CartItem;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.Models.Product;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajith on 29/7/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

  private final String TAG = "PRODUCT ADAPTER";
  private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  private Context context;
  private ArrayList<Product> products = new ArrayList<>();
  private Boolean isListing;
  private static final int REGULAR_ITEM = 0;
  private static final int TALL_ITEM = 1;

  public ProductAdapter(Context context, ArrayList<Product> products, Boolean isListing) {
    this.context = context;
    this.products = products;
    this.isListing = isListing;
  }

  public void update(ArrayList<Product> products) {
    this.products.clear();
    this.products = products;
    notifyDataSetChanged();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_product, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {

    final Product product = products.get(position);
    holder.txtProductName.setText(product.getProductName());
    holder.txtProductCode.setText(product.getProductCode());
    holder.txtCategory.setText(product.getCategory() + " / " + product.getDivision());
    holder.txtPrice.setText(product.getPriceString());

    if (!isListing) {
      final List<CartItem> cart = ShoppingCart.getCart();
      holder.product.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          final Dialog dialog = new Dialog(context);
          dialog.setContentView(R.layout.dialog_cart);
          dialog.setTitle(context.getResources().getString(R.string.enter_qty));
          dialog.getWindow().setBackgroundDrawableResource(R.color.windowBackGround);

          TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
          TextView txtPrice = (TextView) dialog.findViewById(R.id.txtPrice);
          EditText txtQuantity = (EditText) dialog.findViewById(R.id.txtQuantity);

          txtItemName.setText(product.getProductName());
          Customer customer = ShoppingCart.getCustomer();

          if (customer.getState().equalsIgnoreCase("kerala")) {
            txtPrice.setText(String.format("%.2f", product.getInsideKeralaPrice()));
          } else {
            txtPrice.setText(String.format("%.2f", product.getOutsideKeralaPrice()));
          }

          txtPrice.requestFocus();
          dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

          final int itemIndex = ShoppingCart.itemIndex(Integer.valueOf(product.getProductId()));
          if (itemIndex != -1) {
            txtQuantity.setText(String.valueOf(cart.get(itemIndex).getQuantity()));
          }

          Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
          Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
          // if button is clicked, close the custom dialog
          dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              TextView txtQuantity = (TextView) dialog.findViewById(R.id.txtQuantity);
              String str = txtQuantity.getText().toString();
              try {
                int quantity = Integer.parseInt(str);
                Customer customer = ShoppingCart.getCustomer();
                Double price;
                if (customer.getState().equalsIgnoreCase("kerala")) {
                  price = product.getInsideKeralaPrice();
                } else {
                  price = product.getOutsideKeralaPrice();
                }

                if (itemIndex == -1) {
                  CartItem cartItem = new CartItem(product.getProductId(), product.getProductName(),
                      product.getProductCode(), quantity, price, product.getCategory());
                  cart.add(cartItem);
                } else {
                  cart.get(itemIndex).setQuantity(quantity);
                }
                dialog.dismiss();
              } catch (Exception e) {
             //   Toast.makeText(context,
                 //   R.string.pls_enter_valid_qty,Toast.LENGTH_SHORT).show();

                Toast.makeText(context,context.getResources().getString(R.string.pls_enter_valid_qty), Toast.LENGTH_LONG).show();

              }
            }
          });

          dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
          });

          dialog.show();
        }
      });
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView txtProductName, txtProductCode, txtCategory, txtPrice;
    public ImageView imgAction;
    public RelativeLayout product;

    public MyViewHolder(View view) {
      super(view);
      txtProductName = (TextView) view.findViewById(R.id.txtProductName);
      txtProductCode = (TextView) view.findViewById(R.id.txtProductCode);
      txtCategory = (TextView) view.findViewById(R.id.txtCategory);
      txtPrice = (TextView) view.findViewById(R.id.txtPrice);
      imgAction = (ImageView) view.findViewById(R.id.imgAction);
      product = (RelativeLayout) view.findViewById(R.id.product);
    }
  }
}
