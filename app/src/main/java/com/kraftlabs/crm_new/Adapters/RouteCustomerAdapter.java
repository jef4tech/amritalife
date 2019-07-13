package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kraftlabs.crm_new.Fragments.CallFormFragment;
import com.kraftlabs.crm_new.Helper.DateHelper;
import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;




/**
 * Created by ajith on 29/7/16.
 */

public class RouteCustomerAdapter extends RecyclerView.Adapter<RouteCustomerAdapter.MyViewHolder> {

    private final String TAG = "ROUTE CUSTOMER ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<Call> routeCustomers = new ArrayList<>();
    private DateHelper dateHelper = new DateHelper();
    private Boolean isHistory;
    private String routeAddress;

    public RouteCustomerAdapter(Context context, ArrayList<Call> routeCustomers, Boolean isHistory) {
        this.context = context;
        this.routeCustomers = routeCustomers;
        this.isHistory = isHistory;

    }

    public void update(ArrayList<Call> routeCustomers) {
        this.routeCustomers.clear();
        this.routeCustomers = routeCustomers;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_customer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return routeCustomers.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Call routeCustomer = routeCustomers.get(position);
        holder.txtCustomerName.setText(routeCustomer.getCustomerName());
        holder.txtAddress.setText(routeCustomer.getAddress());
        holder.txtPlace.setText(routeCustomer.getPlace());
        holder.txtType.setText(routeCustomer.getType());
        holder.txtPhone.setText(routeCustomer.getPhone());
        holder.txtCity.setText(routeCustomer.getCity());
        holder.txtStatus.setText(routeCustomer.getStatus());
        holder.txtCountComment.setText(context.getString(R.string.comments) + " (" + routeCustomer.getCommentCount() + ")");
        holder.txtDate.setText("Date : " + routeCustomer.getDate());
        holder.imgMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.imgMoreOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.route_customer_more_option);
                
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_call:
                                String mob = routeCustomer.getPhone();
                                if (mob != null) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mob, null));
                                    context.startActivity(callIntent);
                                } else {
                                    Toast.makeText(context, "Mobile number not found", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.menu_show_route:
                                if (!routeAddress.equals(null)) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("google.navigation:q=+" + routeAddress));
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Can't get address", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.menu_sort:

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        if (routeCustomer.getAddress().isEmpty() || routeCustomer.getAddress().equals(null) || routeCustomer.getAddress().equals("null")) {
            holder.txtAddress.setVisibility(View.GONE);
        }
        if (routeCustomer.getPhone().isEmpty() || routeCustomer.getPhone().equals(null) || routeCustomer.getPhone().equals("null")) {
            holder.txtPhone.setVisibility(View.GONE);
        }
        holder.txtSyncStatus.setVisibility(View.GONE);
        if (routeCustomer.getCallId() != 0 && routeCustomer.getSyncStatus() == 0) {
            holder.txtSyncStatus.setVisibility(View.VISIBLE);
            holder.txtSyncStatus.setText(context.getResources().getString(R.string.not_send));
        }
        routeAddress = routeCustomer.getAddress();
        /*String[] values=null;
        if(routeAddress.contains(",")){
             values = routeAddress.split(",");
        }
        if(routeAddress.contains("-")){
            values = routeAddress.split("-");
        }
        String address=*/
        try {
            holder.txtPlace.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.rlRouteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // int dateDiff=dateHelper.dateDiff(routeCustomer.getDate());
                // Log.i(TAG, "onClick: "+dateDiff);
                //dateDiff(routeCustomer.getDate());
                int limit = -1;
               /* if(dateDiff>0){
                    Toasty.error(context, context.getString(R.string.cant_access_route), Toast.LENGTH_SHORT, true).show();


                }else if((dateDiff)>=(limit)){*/
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = CallFormFragment.newInstance(routeCustomer.getRouteId(), routeCustomer.getRouteCustomerId(), routeCustomer.getCustomerId(), isHistory);
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
               /* }
               else {
                    Toasty.error(context, context.getString(R.string.cant_access_route), Toast.LENGTH_SHORT, true).show();


                }*/
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int dateDiff(String expiryDate)

    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date expDate = null;
        //String expiryDate = "2017-09-20";
        int diff = 0;
        try

        {
            expDate = formatter.parse(expiryDate);
            //logger.info("Expiry Date is " + expDate);
            // logger.info(formatter.format(expDate));
            Calendar cal = Calendar.getInstance();
            int today = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(expDate);
            diff = cal.get(Calendar.DAY_OF_MONTH) - today;

        } catch (
                ParseException e)

        {
            e.printStackTrace();
        }
        Log.i(TAG, "dateDiff: " + diff);
        return diff;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCustomerName, txtType, txtPlace, txtAddress, txtPhone, txtCity, txtStatus, txtSyncStatus, txtDate, txtCountComment;
        public ImageButton imgMoreOption;
        public RelativeLayout rlRouteCustomer;


        public MyViewHolder(View view) {
            super(view);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
            txtType = (TextView) view.findViewById(R.id.txtType);
            txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtCity = (TextView) view.findViewById(R.id.txtCity);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtSyncStatus = (TextView) view.findViewById(R.id.txtSyncStatus);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtCountComment = (TextView) view.findViewById(R.id.txtCommentCount);
            rlRouteCustomer = (RelativeLayout) view.findViewById(R.id.route_customer);
            imgMoreOption = view.findViewById(R.id.btnMoreOption);

        }
    }



}
