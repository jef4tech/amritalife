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

import com.kraftlabs.crm_new.Db.CollectionDB;
import com.kraftlabs.crm_new.Fragments.CollectionFormFragment;
import com.kraftlabs.crm_new.Models.Collection;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


/**
 * Created by ASHIK on 01-05-2017.
 */

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.MyViewHolder> {
    private static final String TAG = "Collection List Adapter";
    ArrayList<Collection> collections;
    Collection collection;
    CollectionDB collectionDB;
    private int customerId;
    private Context context;

    public CollectionListAdapter(Context context, ArrayList<Collection> mCollections, int customerId) {
        this.context = context;
        this.collections = mCollections;
        this.customerId = customerId;
        collectionDB = new CollectionDB(context);


    }

    public void update(ArrayList<Collection> collections) {

        this.collections.clear();
        this.collections = collections;
        notifyDataSetChanged();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Collection collection = collections.get(position);
        holder.txtAmount.setText(collection.getAmount() + "");
        holder.txtDate.setText(collection.getDate());
        holder.txtPaymentMode.setText(collection.getPaymentMode() + "");
        holder.txtBillNo.setText(collection.getPaymentModeNo() + "");
        if (collection.getServerCollectionId() == 0) {
            holder.txtStatus.setText(context.getResources().getString(R.string.not_synced));
        } else {
            holder.txtStatus.setText(context.getResources().getString(R.string.synced));
        }
        holder.collection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = CollectionFormFragment.newInstance(collection.getId(), customerId);
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
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return collections.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout collection;
        TextView txtDate, txtPaymentMode, txtAmount, txtBillNo, txtStatus;

        public MyViewHolder(View view) {
            super(view);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            txtPaymentMode = (TextView) view.findViewById(R.id.txtPaymentMode);
            txtBillNo = (TextView) view.findViewById(R.id.txtBillNo);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            collection = (RelativeLayout) view.findViewById(R.id.collection);


        }
    }
}

