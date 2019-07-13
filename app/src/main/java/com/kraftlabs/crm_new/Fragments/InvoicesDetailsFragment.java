package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.InvoicesDetailsAdapter;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.Models.DespatchItem;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


public class InvoicesDetailsFragment extends Fragment {

    private static final String IS_LISTNG = "is_listing";
    private static final String TAG = "Invoice Details Fragment";
    private static final String INVOICE_ID = "INVOICE_ID ";
    private static boolean isListing = true;
    private int invoiceId;
    private Context context;
    private DespatchDB despatchDB;
    private OnFragmentInteractionListener mListener;
    private InvoicesDetailsAdapter invoicesDetailsAdapter;
    private RecyclerView lstInvoicesDetails;
    private Despatch despatch;
    private TextView txtInvoicesEmpty, txtTotal;

    public InvoicesDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoicesDetailsFragment newInstance(int invoiceId) {
        InvoicesDetailsFragment fragment = new InvoicesDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(INVOICE_ID, invoiceId);

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
            invoiceId = getArguments().getInt(INVOICE_ID);
            despatch = despatchDB.getDespatch(invoiceId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_invoices_details, container, false);

        TextView txtInvoiceNumber = (TextView) view.findViewById(R.id.txtInvoiceNumber);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
        TextView txtNumberOfItems = (TextView) view.findViewById(R.id.txtNumberOfItems);
        TextView txtAmount = (TextView) view.findViewById(R.id.txtAmount);

        txtInvoiceNumber.setText(despatch.getBillNumber());
        txtDate.setText(despatch.getBillDate());
        txtAmount.setText(despatch.getBillValue() + "");
        txtNumberOfItems.setText(despatch.getItemCount() + "");

        txtInvoicesEmpty = (TextView) view.findViewById(R.id.txtEmptyInvoiceDetails);
        lstInvoicesDetails = (RecyclerView) view.findViewById(R.id.lstInvoiceDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

        //TODO Check Total
        //   txtTotal.setComment(invoice.getTotal());

        lstInvoicesDetails.setLayoutManager(mLayoutManager);
        lstInvoicesDetails.setItemAnimator(new DefaultItemAnimator());
        lstInvoicesDetails.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstInvoicesDetails.setHasFixedSize(false);
        lstInvoicesDetails.setItemViewCacheSize(20);
        lstInvoicesDetails.setDrawingCacheEnabled(true);
        lstInvoicesDetails.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstInvoicesDetails.invalidate();
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
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getActivity().getResources().getString(R.string.invoice_details));

        ArrayList<DespatchItem> despatchItems = despatchDB.getInvoiceItems(invoiceId);
        invoicesDetailsAdapter = new InvoicesDetailsAdapter(context, despatchItems);
        lstInvoicesDetails.setAdapter(invoicesDetailsAdapter);
        if (despatchItems.size() == 0) {
            txtInvoicesEmpty.setVisibility(View.VISIBLE);
        } else {
            txtInvoicesEmpty.setVisibility(View.GONE);
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
