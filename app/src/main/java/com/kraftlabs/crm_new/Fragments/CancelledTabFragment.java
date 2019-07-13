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

import com.kraftlabs.crm_new.Adapters.DespatchTabAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

public class CancelledTabFragment extends Fragment {

    public static final String TAG = "CancelledTabFragment";
    private static final String ORDER_NUMBER = "ORDER_NUMBER";
    private Context context;
    private String orderNumber;
    private DespatchDB despatchDB;
    private OnFragmentInteractionListener mListener;
    private DespatchTabAdapter despatchTabAdapter;
    private RecyclerView lstCancelledItem;
    private Despatch despatch;

    public CancelledTabFragment() {
        // Required empty public constructor
    }

    public static CancelledTabFragment newInstance(String orderNumber) {
        CancelledTabFragment fragment = new CancelledTabFragment();
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
            //despatch = despatchDB.getDespatch(orderNumber);
            Log.i(TAG, "onCreate: Order Number" + orderNumber);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelled_tab, container, false);

        lstCancelledItem = (RecyclerView) view.findViewById(R.id.lstCancelledItem);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCancelledItem.setLayoutManager(mLayoutManager);
        lstCancelledItem.setItemAnimator(new DefaultItemAnimator());
        lstCancelledItem.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCancelledItem.setHasFixedSize(false);
        lstCancelledItem.setItemViewCacheSize(20);
        lstCancelledItem.setDrawingCacheEnabled(true);
        lstCancelledItem.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCancelledItem.invalidate();
        hideKeyboard(view);
        return view;
    }

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
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.supply_status));
        ArrayList<DespatchItem> cancelledItem = despatchDB.getCancelledItems(orderNumber);
        despatchTabAdapter = new DespatchTabAdapter(context, cancelledItem);
        lstCancelledItem.setAdapter(despatchTabAdapter);
        Log.i(TAG, "onResume: CancelledItems" + cancelledItem);

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
