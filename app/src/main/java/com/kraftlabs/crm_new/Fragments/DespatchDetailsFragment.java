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

import com.kraftlabs.crm_new.Adapters.DespatchDetailsAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


public class DespatchDetailsFragment extends Fragment {
    public static final String DESPATCH_ID = "DESPATCH_ID";
    private static final String TAG = "DespatchDetailsFragment";
    private Context context;

    private int despatchId;
    private DespatchDB despatchDB;
    private OnFragmentInteractionListener mListener;
    private DespatchDetailsAdapter despatchDetailsAdapter;
    private RecyclerView lstDespatchDetails;
    private Despatch despatch;

    private TextView txtDespatchEmpty, txtNoOfBox, txtTransportDetails, txtLrDate, txtInvoiceNo, txtInvoiceDate;

    public DespatchDetailsFragment() {
        // Required empty public constructor
    }

    public static DespatchDetailsFragment newInstance(int despatchId) {
        DespatchDetailsFragment fragment = new DespatchDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(DESPATCH_ID, despatchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        despatchDB = new DespatchDB(context);
        despatch = new Despatch();
        if (getArguments() != null) {
            despatchId = getArguments().getInt(DESPATCH_ID);
            despatch = despatchDB.getDespatch(despatchId);
            Log.i(TAG, "onCreate: DespatchId" + despatchId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_despatch_details, container, false);
        lstDespatchDetails = (RecyclerView) view.findViewById(R.id.lstDespatchDetails);
        txtDespatchEmpty = (TextView) view.findViewById(R.id.txtDespatchEmpty);

        txtNoOfBox = (TextView) view.findViewById(R.id.txtNoOfBoxes);
        txtTransportDetails = (TextView) view.findViewById(R.id.txtTransDetails);
        txtLrDate = (TextView) view.findViewById(R.id.txtLrDate);
        txtInvoiceNo = (TextView) view.findViewById(R.id.txtInvoiceNo);
        txtInvoiceDate = (TextView) view.findViewById(R.id.txtInvoiceDate);

        txtNoOfBox.setText(String.valueOf(despatch.getNoOfBox()));
        txtTransportDetails.setText(despatch.getDetails());
        txtLrDate.setText(despatch.getLrDate());
        txtInvoiceNo.setText(despatch.getBillNumber());
        txtInvoiceDate.setText(despatch.getBillDate());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstDespatchDetails.setLayoutManager(mLayoutManager);
        lstDespatchDetails.setItemAnimator(new DefaultItemAnimator());
        lstDespatchDetails.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstDespatchDetails.setHasFixedSize(false);
        lstDespatchDetails.setItemViewCacheSize(20);
        lstDespatchDetails.setDrawingCacheEnabled(true);
        lstDespatchDetails.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstDespatchDetails.invalidate();

        hideKeyboard(view);
        // setData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.despatch_details));
        ArrayList<DespatchItem> despatchItems = despatchDB.getDetailDespatch(despatchId);
        despatchDetailsAdapter = new DespatchDetailsAdapter(context, despatchItems);
        lstDespatchDetails.setAdapter(despatchDetailsAdapter);
        if (despatchItems.size() == 0) {
            txtDespatchEmpty.setVisibility(View.VISIBLE);
        } else {
            txtDespatchEmpty.setVisibility(View.GONE);
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

    public void setData() {
        despatch = new Despatch();
        txtNoOfBox.setText(despatch.getNoOfBox());
        txtTransportDetails.setText(despatch.getDetails());
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
