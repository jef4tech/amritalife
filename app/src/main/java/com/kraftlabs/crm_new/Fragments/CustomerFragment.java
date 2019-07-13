package com.kraftlabs.crm_new.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kraftlabs.crm_new.Adapters.CustomerAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.libs.EndlessRecyclerOnScrollListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomerFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "CustomerFragment";
    private static final String IS_LISTNG = "is_listing";
    public static boolean isListing = true;
    private static boolean isToItemStatus;
    private static String IS_TO_ITEM_STATUS = "to_order_status";
    private static int firstVisibleInListview;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    MenuItem item;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private Context context;
    private CustomersDB customersDB;
    private CustomerFragment.OnFragmentInteractionListener mListener;
    private EditText txtCustomerHint;
    private CustomerAdapter customerAdapter;
    private RecyclerView lstCustomer;
    private ArrayList<Customer> customers;
    private TextView txtCustomerEmpty;
    private boolean loading = true;
    private EndlessRecyclerOnScrollListener scrollListener;
    private int productId = 0;

    public CustomerFragment() {
        // Required empty public constructor
    }

    public static CustomerFragment newInstance(boolean isListing, boolean isToItemStatus) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_LISTNG, isListing);
        args.putBoolean(IS_TO_ITEM_STATUS, isToItemStatus);
        fragment.setArguments(args);
        return fragment;
    }

    public void getDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_APP_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.get("status").toString();
                    if(status.equals("success")) {
                        Log.i(TAG, "onResponse: " + status);
                    }
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                return super.getPostParams();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isListing = true;
        if(getArguments() != null) {
            isListing = getArguments().getBoolean(IS_LISTNG);
            isToItemStatus = getArguments().getBoolean(IS_TO_ITEM_STATUS);
            productId = getArguments().getInt("PRODUCT_ID");
        }
        context = getActivity();
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
    }*/

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterItemList();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        // initSpruce();
        lstCustomer = (RecyclerView) view.findViewById(R.id.lstCustomer);
        txtCustomerEmpty = (TextView) view.findViewById(R.id.txtCustomerEmpty);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCustomer.setLayoutManager(mLayoutManager);
        lstCustomer.setItemAnimator(new DefaultItemAnimator());
        lstCustomer.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCustomer.setHasFixedSize(false);
        lstCustomer.setItemViewCacheSize(20);
        lstCustomer.setDrawingCacheEnabled(true);
        lstCustomer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCustomer.invalidate();
        txtCustomerHint = (EditText) view.findViewById(R.id.txtCustomerHint);
        txtCustomerHint.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    filterItemList();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        hideKeyboard(view);




       /* lstCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });*/
        return view;
    }

    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.sync_menu, menu);

    *//*dataCount();*//*
        MenuItem menuItem = menu.findItem(R.id.action_sync);


        super.onCreateOptionsMenu(menu, inflater);
    }*/

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View mProgressView = getActivity().findViewById(R.id.login_progress);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    // * Called when the fragment is visible to the user and actively running.
    public void onResume() {
        super.onResume();
        //    searchView.activityResumed();
//        String[] arr = getResources().getStringArray(R.array.suggestions);
        //  searchView.addSuggestions(arr);
        if(isListing && !isToItemStatus) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.customers));
        }
        if(!isListing && !isToItemStatus) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.select_customer));
        }
        if(isToItemStatus) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.customers));
        }
        customersDB = new CustomersDB(context);
        int i = customersDB.getCount();
        if(isToItemStatus) {
            customers = customersDB.getCustomerForItem(productId);
        } else {
            customers = customersDB.getCustomers("", 50);
        }
        customerAdapter = new CustomerAdapter(context, customers, isListing, isToItemStatus);
        lstCustomer.setAdapter(customerAdapter);
        if(customers.size() == 0) {
            txtCustomerEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCustomerEmpty.setVisibility(View.GONE);
        }
    }

    public void filterItemList() {
        String hint = txtCustomerHint.getText().toString();
        ArrayList<Customer> customers = null;
        try {
            customers = customersDB.getCustomers(hint, 50);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        customerAdapter = new CustomerAdapter(context, customers, isListing, isToItemStatus);
        lstCustomer.setAdapter(customerAdapter);
        if(customers.size() == 0) {
            txtCustomerEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCustomerEmpty.setVisibility(View.GONE);
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
 /* private void initSpruce() {
    spruceAnimator = new Spruce.SpruceBuilder(lstCustomer)
        .sortWith(new DefaultSort(100))
        .animateWith(DefaultAnimations.shrinkAnimator(lstCustomer, 800),
            ObjectAnimator.ofFloat(lstCustomer, "translationX", lstCustomer.getWidth(), 0f)
                .setDuration(800))
        .start();
  }*/
}
