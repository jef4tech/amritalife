package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.Route;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by ajith on 29/7/16.
 */

public class SpinnerRouteAdapter extends BaseAdapter {

    Context context;
    ArrayList<Route> routes;

    public SpinnerRouteAdapter(Context context, ArrayList<Route> routes) {
        this.context = context;
        this.routes = routes;

    }

    @Override
    public int getCount() {
        return this.routes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.spn_item_route, parent, false);
        } else {
            itemView = convertView;
        }
        TextView txtRouteName = (TextView) itemView.findViewById(R.id.txtRouteName);
        Route route = routes.get(position);
        txtRouteName.setText(route.getRouteName());
        return itemView;
    }
}
