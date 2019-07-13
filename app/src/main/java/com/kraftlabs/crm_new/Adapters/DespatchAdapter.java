package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Fragments.DespatchDetailsFragment;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by ASHIK on 10-05-2017.
 */

public class DespatchAdapter extends RecyclerView.Adapter<DespatchAdapter.MyViewHolder> {
    private final String TAG = "DespatchAdapter";
    ArrayList<Despatch> despatches = new ArrayList<Despatch>();
    private Context context;


    public DespatchAdapter(Context context, ArrayList<Despatch> despatches) {
        this.context = context;
        this.despatches = despatches;
    }

    public void update(ArrayList<Despatch> despatches) {
        this.despatches.clear();
        this.despatches = despatches;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_despatch, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Despatch despatch = despatches.get(position);
        holder.txtLrNo.setText(despatch.getLrNo());
        holder.txtDate.setText(despatch.getLrDate());

        holder.despatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = DespatchDetailsFragment.newInstance(despatch.getId());
                String backStateName = fragment.getClass().getName();
                FragmentTransaction fragmentManager =
                        activity.getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.content_main, fragment);
                fragmentManager.addToBackStack(backStateName);
                fragmentManager.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return despatches.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout despatch;
        TextView txtAppOrderNo, txtLrNo, txtDate;

        public MyViewHolder(View view) {
            super(view);
            despatch = (RelativeLayout) view.findViewById(R.id.despatch);
            txtLrNo = (TextView) view.findViewById(R.id.txtLRNo);
            txtDate = (TextView) view.findViewById(R.id.txtDate);

        }
    }
}
