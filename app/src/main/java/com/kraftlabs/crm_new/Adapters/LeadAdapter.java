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

import com.kraftlabs.crm_new.Db.LeadDB;
import com.kraftlabs.crm_new.Fragments.NewCallFormFragment;
import com.kraftlabs.crm_new.Models.Lead;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by ajith on 29/7/16.
 */

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.MyViewHolder> {

    private final String TAG = "TASK ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    private ArrayList<Lead> leads = new ArrayList<>();
    private LeadDB leadDB;

    public LeadAdapter(Context context, ArrayList<Lead> leads) {
        this.context = context;
        this.leads = leads;
        leadDB = new LeadDB(context);
    }

    public void update(ArrayList<Lead> leads) {
        this.leads.clear();
        this.leads = leads;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lead, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return leads.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Lead lead = leads.get(position);
        holder.txtName.setText(lead.getName());
        holder.txtDate.setText(lead.getDate());
        holder.txtPhone.setText("" + lead.getPhone());

        if (lead.getServerLeadId() == 0) {
            holder.txtStatus.setText(context.getResources().getString(R.string.not_send));

        } else {
            holder.txtStatus.setText(context.getResources().getString(R.string.send));
        }
        holder.lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = NewCallFormFragment.newInstance(lead.getId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
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
    public long getItemId(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtDate, txtPhone, txtStatus;
        public RelativeLayout lead;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            lead = (RelativeLayout) view.findViewById(R.id.lead);
        }
    }
}
