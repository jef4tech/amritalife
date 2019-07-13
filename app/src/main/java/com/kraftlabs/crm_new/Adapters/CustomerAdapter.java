package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Fragments.CustomerDetailsFragment;
import com.kraftlabs.crm_new.Fragments.OrderCreationFragment;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ajith
 * Date: 29/7/16
 * Time: 4:20 PM
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private final String TAG = "CUSTOMER ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<Customer> customers = new ArrayList<>();
    private Boolean isListing;
    private Boolean isToItemStatus;
    private SparseBooleanArray selectedItems;

    public CustomerAdapter(Context context, ArrayList<Customer> customers, Boolean isListing, Boolean isToItemStatus) {
        this.context = context;
        this.customers = customers;
        this.isListing = isListing;
        this.isToItemStatus=isToItemStatus;
    }

    public void update(ArrayList<Customer> customers) {
        this.customers.clear();
        this.customers = customers;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new MyViewHolder(itemView);
    }

    /*private void setAnimation(RelativeLayout container, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        container.startAnimation(animation);
    }*/

    @Override
    public int getItemCount() {
        return customers.size();
    }



    public interface onItemClickListener {
        public void itemDetailClick(Customer customer);
    }

    public void setSearchResult(List<Customer> result) {
        customers = (ArrayList<Customer>) result;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Customer customer = customers.get(position);

        holder.txtCustomerName.setText(customer.getCustomerName() + "(" + customer.getCustomerCode() + ")");

        holder.txtCity.setText(customer.getCity());
        if (customer.getAddress().isEmpty() || customer.getAddress().equals(null) || customer.getAddress().equals("null")) {
            holder.txtAddress.setVisibility(View.GONE);
        }else{
            holder.txtAddress.setVisibility(View.VISIBLE);
            holder.txtAddress.setText(customer.getAddress());
        }
        if (customer.getPhone().isEmpty() || customer.getPhone().equals(null) || customer.getPhone().equals("null")) {
            holder.txtPhone.setVisibility(View.GONE);
        } else{
            holder.txtPhone.setVisibility(View.VISIBLE);
            holder.txtPhone.setText(customer.getPhone());
        }
        if(customer.getCategory()!=null){
            holder.txtCategory.setVisibility(View.VISIBLE);
            holder.textCategory.setVisibility(View.VISIBLE);
            holder.txtCategory.setText(customer.getCategory());
        }
        else{
            holder.txtCategory.setVisibility(View.GONE);
            holder.textCategory.setVisibility(View.GONE);
        }
        holder.txtCustomerCode.setVisibility(View.GONE);

        if (isToItemStatus) {
            holder.customer.setClickable(false);
        }

        if (!isListing && !isToItemStatus) {
            holder.imgAction.setVisibility(View.VISIBLE);
            

            holder.customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ShoppingCart.setCustomer(customer);
                    Fragment fragment = new OrderCreationFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    String backStateName = fragment.getClass().getName();
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }
            });
        } if(isListing && !isToItemStatus) {
            holder.customer.setOnClickListener(view -> {

                Fragment fragment = CustomerDetailsFragment.newInstance(customer.getCustomerId());
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
        public TextView txtCustomerName, txtCustomerCode, txtAddress, txtPhone, txtCity,txtCategory,textCategory;
        public ImageView imgAction;
        public RelativeLayout customer;



        public MyViewHolder(View view) {
            super(view);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
            txtCustomerCode = (TextView) view.findViewById(R.id.txtCustomerCode);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtCity = (TextView) view.findViewById(R.id.txtCity);
            imgAction = (ImageView) view.findViewById(R.id.imgAction);
            txtCategory= (TextView) view.findViewById(R.id.txtCategory);
            textCategory = (TextView) view.findViewById(R.id.textCategory);
            customer = (RelativeLayout) view.findViewById(R.id.customer);

        }

        public void bind(MyViewHolder model) {
          //  txtSerchView.setText(model.get););
        }
    }

}

    
