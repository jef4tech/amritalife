package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
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

import com.kraftlabs.crm_new.Fragments.CallFragment;
import com.kraftlabs.crm_new.Models.Route;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Created by ajith on 29/7/16.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {

    private final String TAG = "ROUTE ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private ArrayList<Route> routes = new ArrayList<>();

    public RouteAdapter(Context context, ArrayList<Route> routes) {
        this.context = context;
        this.routes = routes;

    }

    public void update(ArrayList<Route> routes) {
        this.routes.clear();
        this.routes = routes;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Route route = routes.get(position);
        holder.txtRouteName.setText(route.getRouteName());
        holder.txtDate.setText("Date : " + route.getDate());
        holder.txtCreatedUser.setText(context.getResources().getString(R.string.created_by) + route.getCreatedUserName());
        holder.txtStatus.setText(route.getStatus());

        holder.route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = CallFragment.newInstance(route.getRouteAssignId());
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRouteName, txtDate, txtCreatedUser, txtStatus;
        public RelativeLayout route;

        public MyViewHolder(View view) {
            super(view);
            txtRouteName = (TextView) view.findViewById(R.id.txtRouteName);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtCreatedUser = (TextView) view.findViewById(R.id.txtCreatedUser);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            route = (RelativeLayout) view.findViewById(R.id.route);

        }
    }

}
