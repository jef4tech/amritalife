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

import com.kraftlabs.crm_new.Adapters.DespatchAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by AShik on 9/11/15.
 */

public class DespatchFragment extends Fragment {

    private static final String TAG = "DESPATCH FRAGMENT";
    private static final String CUSTOMER_ID = "CUSTOMER_ID";
    private Context context;
    private int customerId = 0;
    private DespatchDB despatchDB;
    private OnFragmentInteractionListener mListener;
    private DespatchAdapter despatchAdapter;
    private RecyclerView lstDespatch;
    private ArrayList<Despatch> despatches;
    private TextView txtEmptyDespatch;

    public DespatchFragment() {
    }

    public static DespatchFragment newInstance(int mCustomerId) {
        DespatchFragment fragment = new DespatchFragment();
        Bundle args = new Bundle();
        args.putInt(CUSTOMER_ID, mCustomerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        despatchDB = new DespatchDB(context);

        if (getArguments() != null) {
            customerId = getArguments().getInt(CUSTOMER_ID);
            despatches = despatchDB.getDespatches(customerId, "");
            Log.i(TAG, "onCreate: CustomerID" + customerId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_despatch, container, false);

        lstDespatch = (RecyclerView) view.findViewById(R.id.lstDespatch);
        txtEmptyDespatch = (TextView) view.findViewById(R.id.txtDespatchEmpty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstDespatch.setLayoutManager(mLayoutManager);
        lstDespatch.setItemAnimator(new DefaultItemAnimator());
        lstDespatch.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstDespatch.setHasFixedSize(false);
        lstDespatch.setItemViewCacheSize(20);
        lstDespatch.setDrawingCacheEnabled(true);
        lstDespatch.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstDespatch.invalidate();

        hideKeyboard(view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.despatches));
        despatches = despatchDB.getDespatches(customerId, "");
        if (despatches.size() > 0) {
            despatchAdapter = new DespatchAdapter(context, despatches);
            lstDespatch.setAdapter(despatchAdapter);
        } else {
            txtEmptyDespatch.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
