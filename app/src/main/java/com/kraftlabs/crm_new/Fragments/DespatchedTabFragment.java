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

public class DespatchedTabFragment extends Fragment {

    private static final String ORDER_NUMBER = "ORDER_NUMBER";
    private static final String TAG = "DespatchesTabFragment";
    private Context context;
    private String orderNumber;
    private DespatchDB despatchDB;
    private OnFragmentInteractionListener mListener;
    private DespatchTabAdapter despatchTabAdapter;
    private Despatch despatch;
    private RecyclerView lstDespatches;
    private TextView txtEmpty;

    public DespatchedTabFragment() {
    }

    public static DespatchedTabFragment newInstance(String orderNumber) {
        DespatchedTabFragment fragment = new DespatchedTabFragment();
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
            //  despatch = despatchDB.getDespatch(orderNumber);
            Log.i(TAG, "onCreate: Order Number" + orderNumber);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_despatched_tab, container, false);
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstDespatches = (RecyclerView) view.findViewById(R.id.lstDespatches);
        lstDespatches.setLayoutManager(mLayoutManager);
        lstDespatches.setItemAnimator(new DefaultItemAnimator());
        lstDespatches.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstDespatches.setHasFixedSize(false);
        lstDespatches.setItemViewCacheSize(20);
        lstDespatches.setDrawingCacheEnabled(true);
        lstDespatches.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstDespatches.invalidate();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.supply_status));

        ArrayList<DespatchItem> despatchItems = despatchDB.getDespatchItems(orderNumber);
        despatchTabAdapter = new DespatchTabAdapter(context, despatchItems);
        lstDespatches.setAdapter(despatchTabAdapter);
        Log.i(TAG, "onResume: Despatched Items" + despatchItems);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
