package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.NotificationAdapter;
import com.kraftlabs.crm_new.Db.NotificationDB;
import com.kraftlabs.crm_new.Models.NotificationModel;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private NotificationDB notificationDB;
    private NotificationAdapter notificationAdapter;
    private RecyclerView notifRecyclerView;
    private ArrayList<NotificationModel> notificationModels;
    private TextView txtNotificationEmpty;
    private NotificationModel notificationModel;
    private Context context;

    public NotificationFragment() {
        
    }
    

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
               notificationDB =new NotificationDB(context);
               notificationModel=new NotificationModel();
               
        if(getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        notifRecyclerView = (RecyclerView) view.findViewById(R.id.lstNotification);
        txtNotificationEmpty = (TextView) view.findViewById(R.id.txtNotificationEmpty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        notifRecyclerView.setLayoutManager(mLayoutManager);
        notifRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notifRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        notifRecyclerView.setHasFixedSize(false);
        notifRecyclerView.setItemViewCacheSize(20);
        notifRecyclerView.setDrawingCacheEnabled(true);
        notifRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        notifRecyclerView.invalidate();
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.notifications));
        notificationModels =notificationDB.getNotifications();
        notificationAdapter=new NotificationAdapter(context,notificationModels);
        notifRecyclerView.setAdapter(notificationAdapter);
        if(notificationModels.size()==0){
            txtNotificationEmpty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
