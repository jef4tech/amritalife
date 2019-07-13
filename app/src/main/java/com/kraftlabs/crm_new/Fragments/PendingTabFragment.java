package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.DespatchTabAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


public class PendingTabFragment extends Fragment {
    private static final String ORDER_NUMBER = "ORDER_NUMBER";
    private static final String TAG = "PendingTabFragment";
    private Context context;
    private String orderNumber;
    private DespatchDB despatchDB;
    private DespatchTabAdapter despatchTabAdapter;

    private RecyclerView lstPendingItems;
    private OnFragmentInteractionListener mListener;
    private TextView txtEmpty;
    private Despatch despatch;

    public PendingTabFragment() {

    }

    public static PendingTabFragment newInstance(String orderNumber) {
        PendingTabFragment fragment = new PendingTabFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_NUMBER, orderNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        despatchDB = new DespatchDB(context);
        if (getArguments() != null) {
            orderNumber = getArguments().getString(ORDER_NUMBER);
            Log.i(TAG, "onCreate: Order Number" + orderNumber);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_tab, container, false);

        lstPendingItems = (RecyclerView) view.findViewById(R.id.lstPendingTab);
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstPendingItems.setLayoutManager(mLayoutManager);
        lstPendingItems.setItemAnimator(new DefaultItemAnimator());
        lstPendingItems.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstPendingItems.setHasFixedSize(false);
        lstPendingItems.setItemViewCacheSize(20);
        lstPendingItems.setDrawingCacheEnabled(true);
        lstPendingItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstPendingItems.invalidate();
        hideKeyboard(view);
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {

        getActivity().setTitle(getActivity().getResources().getString(R.string.supply_status));
        super.onResume();


        ArrayList<DespatchItem> pendingItems = despatchDB.getPendingItems(orderNumber);
        despatchTabAdapter = new DespatchTabAdapter(context, pendingItems);

        lstPendingItems.setAdapter(despatchTabAdapter);
        Log.i(TAG, "onResume: Pendint Items" + pendingItems);


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
