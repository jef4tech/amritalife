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
import com.kraftlabs.crm_new.Adapters.InvoiceAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


/**
 * Created by Ashik on 9/11/15.
 */
public class InvoiceFragment extends Fragment {

    public static final String IS_LISTING = "is_listing";
    private static final String CUSTOMER_ID = "CUSTOMER_ID";
    private static final String TAG = "Invoice Fragment";
    public static boolean isLIsting = true;
    private Context context;
    private int customerId = 0;

    private DespatchDB despatchDB;
    private DespatchAdapter despatchAdapter;
    private RecyclerView lstInvoices;
    private InvoiceAdapter invoiceAdapter;

    private ArrayList<Despatch> invoices;
    private TextView txtInvoiceEmpty;


    private OnFragmentInteractionListener mListener;

    public InvoiceFragment() {
        // Required empty public constructor
    }


    public static InvoiceFragment newInstance(int mCustomerId) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putInt(CUSTOMER_ID, mCustomerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerId = getArguments().getInt(CUSTOMER_ID);
            Log.i(TAG, "onCreate: CustomerID" + customerId);
        }
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        lstInvoices = (RecyclerView) view.findViewById(R.id.lstInvoices);
        txtInvoiceEmpty = (TextView) view.findViewById(R.id.txtInvoiceEmpty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstInvoices.setLayoutManager(mLayoutManager);
        lstInvoices.setItemAnimator(new DefaultItemAnimator());
        lstInvoices.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstInvoices.setHasFixedSize(false);
        lstInvoices.setItemViewCacheSize(20);
        lstInvoices.setDrawingCacheEnabled(true);
        lstInvoices.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstInvoices.invalidate();
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
        super.onResume();
        if (isLIsting) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.invoice));
        } else {
            getActivity().setTitle(getActivity().getResources().getString(R.string.empty_invoices));
        }
        despatchDB = new DespatchDB(context);
        invoices = despatchDB.getDespatches(customerId, "");
        invoiceAdapter = new InvoiceAdapter(context, invoices);
        lstInvoices.setAdapter(invoiceAdapter);
        if (invoices.size() == 0) {
            txtInvoiceEmpty.setVisibility(View.VISIBLE);
        } else {
            txtInvoiceEmpty.setVisibility(View.GONE);
        }
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
