package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;
import com.kraftlabs.crm_new.AdditionalData.AdditionalInfoFragment;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.OutstandingDB;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.LocationAlertDialog;

import java.util.ArrayList;


/**
 * Created by ASHIK on 26-04-2017.
 */

public class CustomerDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = CustomerDetailsFragment.class.getName();

    private static String CUSTOMER_ID = "CUSTOMER_ID";
    Context context;

    @NonNull
    private int customerId;
    private Button btnOrderReport, btnInvoices, btnDespatch, btnCollection, btnfeedback, btnContact,
            btnAddCustomerDtls, btnShowCallHistory;
    private TextView txt0To30, txt31To60, txt61To90, txt91To180, txt181To365, txtAbove365;
    private OutstandingDB outstandingDB;
    private AdditionalInfoFragment mAdditionalInfoFragment;
    private String routeAddress;
    public CustomerDetailsFragment() {
    }

    public static CustomerDetailsFragment newInstance(int mCustomerId) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
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
              Log.i(TAG, "onCreate: CustomerID123 ===> " + getArguments());
            //  Log.i(TAG, "onCreate: CustomerID ===> " + customerId);
        }
        context = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(context.getResources().getString(R.string.customer_details));
        //  btnAddCustomerDtls.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);
        findViewById(view);
        context = getActivity();
        CustomersDB customersDB = new CustomersDB(context);
        Customer customer = customersDB.getCustomerByCode(String.valueOf(customerId));
        outstandingDB = new OutstandingDB(context);
        ArrayList<String> dues = outstandingDB.getOutstandings(customerId);
       

        Log.i(TAG, "onCreateView: out"+dues);

        try {
             routeAddress=customer.getCity()+"-"+customer.getAddress();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        txt0To30.setText(dues.get(0));
        txt31To60.setText(dues.get(1));
        txt61To90.setText(dues.get(2));
        txt91To180.setText(dues.get(3));
        txt181To365.setText(dues.get(4));
        txtAbove365.setText(dues.get(5));

        TextView txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        TextView txtCustomerCode = (TextView) view.findViewById(R.id.txtCustomerCode);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        TextView txtCity = (TextView) view.findViewById(R.id.txtCity);
        TextView txtPhone = (TextView) view.findViewById(R.id.txtPhone);

 /*       txtCustomerName.setText(customer.getCustomerName());
        txtCustomerCode.setText(customer.getCustomerCode());
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtPhone.setText(customer.getPhone());*/
        hideKeyboard(view);
        return view;
    }

    public void findViewById(View view) {
        btnOrderReport = (Button) view.findViewById(R.id.btnOrderDetails);
        btnInvoices = (Button) view.findViewById(R.id.btnInvoices);
        btnCollection = (Button) view.findViewById(R.id.btnCollection);
        btnDespatch = (Button) view.findViewById(R.id.btnDespatch);
        btnAddCustomerDtls = (Button) view.findViewById(R.id.btnAddCustomerDtls);
        btnShowCallHistory = view.findViewById(R.id.btnViewCallHistory);

        btnOrderReport.setOnClickListener(this);
        btnInvoices.setOnClickListener(this);
        btnCollection.setOnClickListener(this);
        btnDespatch.setOnClickListener(this);
        btnAddCustomerDtls.setOnClickListener(this);
        btnShowCallHistory.setOnClickListener(this);


        txt0To30 = (TextView) view.findViewById(R.id.txt0To30);
        txt31To60 = (TextView) view.findViewById(R.id.txt31To60);
        txt61To90 = (TextView) view.findViewById(R.id.txt61To90);
        txt91To180 = (TextView) view.findViewById(R.id.txt91To180);
        txt181To365 = (TextView) view.findViewById(R.id.txt181To365);
        txtAbove365 = (TextView) view.findViewById(R.id.txtAbove365);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;

        switch (view.getId()) {
            case R.id.btnOrderDetails:
                fragment = OrderFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;

            case R.id.btnInvoices:
                fragment = InvoiceFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;

            case R.id.btnCollection:
                fragment = CollectionFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;

            case R.id.btnDespatch:
                fragment = DespatchFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;

        /*Fragment fragment1 = new AdditionalInfoFragment();
        android.support.v4.app.FragmentManager fragmentManager =
            getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_main, fragment1);
        String backStateName = fragment1.getClass().getName();
        ft.addToBackStack(backStateName);
        ft.commit();*/
               /* fragment = AdditionalInfoFragment.newInstance();*//*customerId*//*
                replaceFragment(fragment);*/

            case R.id.btnAddCustomerDtls:
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    LocationAlertDialog.buildAlertMessageNoGps(context);
                    return;
                }else{
                    if(customerId != 0) {
                        fragment = AdditionalInfoFragment.newInstance(customerId);
                        replaceFragment(fragment);
                        break;
                    }
                }

            case R.id.btnViewCallHistory:
                fragment = CallFragment.newInstance(true, customerId);
                replaceFragment(fragment);
                break;

        }
    }

    /*public void ShowCallHistory(View view){
        Fragment fragment;
        fragment = CallFragment.newInstance(true);
        replaceFragment(fragment);
    }*/
    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}


























