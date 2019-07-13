package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CollectionDB;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Models.Collection;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



public class CollectionFormFragment extends Fragment {
    private static final String COLLECTION_ID = "collection_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String[] paymentMode = {" ", "Cheque", "DD", "Cash"};
    private static EditText edtDate;
    private static String TAG = "COLLECTION_FORM";
    private Context context;
    private int collectionId;
    private boolean editMode = false;
    private Collection collection;
    private CollectionDB collectionDB;
    private EditText edtChqDdNo, edtPaymentMode;
    private TextView edtAmount;
    private Spinner spnPaymentMode;
    private OnFragmentInteractionListener mListener;
    private Button btnSubmit;
    private TextInputLayout txtLayoutDate, txtLayoutAmount, txtLAyoutChqDdNo;
    private int customerId;
    private ArrayList<Collection> collections;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String today = sdf.format(new Date());

    public CollectionFormFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CollectionFormFragment(Context context) {
        this.context = context;
    }

    public static CollectionFormFragment newInstance(int collectionId, int customerId) {
        CollectionFormFragment fragment = new CollectionFormFragment();
        Bundle args = new Bundle();
        args.putInt(COLLECTION_ID, collectionId);
        args.putInt(CUSTOMER_ID, customerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        collectionDB = new CollectionDB(context);
        collection = new Collection();
        if (getArguments() != null) {
            collectionId = getArguments().getInt(COLLECTION_ID);
            customerId = getArguments().getInt(CUSTOMER_ID);
            editMode = true;
            collection = collectionDB.getCollection(collectionId);
            if (collectionId == 0) {
                editMode = false;
            }
        } else {
            collection = new Collection();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collection_form, container, false);

        edtDate = (EditText) view.findViewById(R.id.txtDate);
        edtAmount = (TextView) view.findViewById(R.id.txtAmount);
        spnPaymentMode = (Spinner) view.findViewById(R.id.spnPaymentMode);
        edtChqDdNo = (EditText) view.findViewById(R.id.txtChqDdNo);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        txtLayoutDate = (TextInputLayout) view.findViewById(R.id.txtLayoutDate);
        txtLayoutAmount = (TextInputLayout) view.findViewById(R.id.txtLayoutAmount);
        txtLAyoutChqDdNo = (TextInputLayout) view.findViewById(R.id.txtLayoutChqDdNo);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCollection();

                hideKeyboard(view);
            }
        });
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, paymentMode);
        spnPaymentMode.setAdapter(adapter);
        spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    collection.setPaymentMode("");
                    txtLAyoutChqDdNo.setVisibility(View.GONE);
                } else if (position == 1) {
                    collection.setPaymentMode(context.getResources().getString(R.string.chq));
                    txtLAyoutChqDdNo.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    collection.setPaymentMode(context.getResources().getString(R.string.dd));
                    txtLAyoutChqDdNo.setVisibility(View.VISIBLE);
                } else {
                    collection.setPaymentMode(context.getResources().getString(R.string.cash));
                    txtLAyoutChqDdNo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                collection.setPaymentMode("");
            }
        });

        Button btnPickDate = (Button) view.findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        hideKeyboard(view);
        return view;
    }

    public void saveCollection() {
        CustomersDB customersDB = new CustomersDB(context);
        Customer customer = customersDB.getCustomerById(customerId);

        int userId = PrefUtils.getCurrentUser(context).getUserId();

        collection.setUserId(userId + "");
        //collection.setCustomerName();
        collection.setCustomerId(customerId);

        try {
            collection.setAmount(Integer.parseInt(edtAmount.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.pls_enter_valid_amount,
                    Toast.LENGTH_SHORT).show();
            return;
        } catch (NullPointerException e1) {
            Toast.makeText(context, R.string.pls_enter_amount,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (collection.getPaymentMode().equals("")) {
            Toast.makeText(context,R.string.pls_select_payment_mode,
                    Toast.LENGTH_SHORT).show();
            return;

        }
        if ((edtChqDdNo.getVisibility() == View.VISIBLE) && (edtChqDdNo.getText().equals(""))) {
            Toast.makeText(context,R.string.pls_enter_payment_no,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtDate.getText().toString().equals("")) {
            Toast.makeText(context, R.string.pls_enter_date,
                    Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            collection.setPaymentModeNo(edtChqDdNo.getText().toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if ((edtAmount.getText().length() != 0) && (!edtDate.getText().equals(""))) {
            if (editMode) {
                collection.setServerCollectionId(0);
                collectionDB.update(collection);
                Toast.makeText(context,R.string.data_updated,
                        Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, context.getResources().getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            } else {
                collection.setDate(today);


                
                collectionId = collectionDB.insert(collection);
                collection.setId(collectionId);
                editMode = true;
                Toast.makeText(context,R.string.data_inserted,
                        Toast.LENGTH_SHORT
                ).show();
                //            Toast.makeText(context, context.getResources().getString(R.string.data_inserted), Toast.LENGTH_SHORT).show();

            }
            sync();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Fragment fragment = CollectionFragment.newInstance(customerId);
                    // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
                       FragmentManager fragmentManager =
                            getActivity().getSupportFragmentManager();
                       FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    String backStateName = "CollectionFragment";
                    ft.addToBackStack(backStateName);
                    ft.detach(fragment);
                    ft.attach(fragment);
                    ft.commit();
                }
            }, 600);

        }


    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.collection_details));

        if (editMode) {
            edtDate.setText(collection.getDate());
            edtAmount.setText(collection.getAmount() + "");
            edtChqDdNo.setText(collection.getPaymentModeNo());

            if (collection.getPaymentMode().equals("")) {
                spnPaymentMode.setSelection(0);
                edtChqDdNo.setVisibility(View.GONE);
            } else if (collection.getPaymentMode()
                    .equals(context.getResources().getString(R.string.chq))) {
                spnPaymentMode.setSelection(1);
                edtChqDdNo.setVisibility(View.VISIBLE);
            } else if (collection.getPaymentMode()
                    .equals(context.getResources().getString(R.string.dd))) {

                spnPaymentMode.setSelection(2);
                edtChqDdNo.setVisibility(View.VISIBLE);
            } else {
                spnPaymentMode.setSelection(3);//cash
                edtChqDdNo.setVisibility(View.VISIBLE);
            }
        } else {
            spnPaymentMode.setSelection(3);  //type cash  \
            edtDate.setVisibility(View.VISIBLE);
            edtAmount.setVisibility(View.VISIBLE);
            edtDate.setText(today);

        }
    }

    public void sync() {
        try {
            collectionDB = new CollectionDB(context);
        } catch (NullPointerException e) {
            collectionDB = new CollectionDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        collections = collectionDB.getUnsentData();
        if(collections.size() > 0) {
            for(int i = 0; i < collections.size(); i++) {
                collection = collections.get(i);
                saveToServer(collection.getId());
            }
        }
    }

    public int syncCount() {
        try {
            collectionDB = new CollectionDB(context);
        } catch (NullPointerException e) {
            collectionDB = new CollectionDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        collections = collectionDB.getUnsentData();

        return collections.size();
    }
    public void saveToServer(final int id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_COLLECTION,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, s);
                        try {
                            JSONObject object = new JSONObject(s);
                            String status = object.get("status").toString();

                            if (status.equals("success")) {
                                collection = collectionDB.getCollection(id);
                                int serverCollectionId = object.getInt("collection_id");
                                collection.setServerCollectionId(serverCollectionId);
                                collectionDB.update(collection);
                            }
                        } catch (JSONException e) {
                            Log.i(TAG, e.getMessage());
                        } catch (NullPointerException e) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();

                collection = collectionDB.getCollection(id);
                params.put("customer_id", "" + collection.getCustomerId());
                params.put("date", collection.getDate());
                params.put("amount", "" + collection.getAmount());
                params.put("payment_mode", collection.getPaymentMode());
                params.put("payment_no", "" + collection.getPaymentModeNo());
                params.put("user_id", "" + userId);
                params.put("collection_id", "" + collection.getServerCollectionId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            edtDate.setText(
                    "" + year + (month < 9 ? "-0" : "-") + (month + 1) + (day < 10 ? "-0" : "-") + day);
        }
    }
}
