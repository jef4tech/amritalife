package com.kraftlabs.crm_new.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.OrderAdapter;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.OrdersDB;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.Models.Order;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class OrderFragment extends Fragment {

    private static final String TAG = "OrderFragment";
    private static String CUSTOMER_ID = "CUSTOMER_ID";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int customerId = 0;
    private Context context;
    private OrdersDB ordersDB;
    private ArrayList<Order> orders;
    private ArrayList<Order> drafts;
    private OrderAdapter orderAdapter;
    private RecyclerView lstOrder;
    private RecyclerView lstDraft;
    private TextView txtOrderEmpty;
    private TextView txtDraftEmpty;
    private RelativeLayout rltDrafts;
    private NestedScrollView scrollView;
    private LinearLayout lenLinearLayout;
    private Customer customer;
    private CustomersDB customersDB;
    // private Order
    private RouteFragment.OnFragmentInteractionListener mListener;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(int mCustomerId) {
        OrderFragment fragment = new OrderFragment();
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
        }
        context = getActivity();
        customersDB = new CustomersDB(context);
        Log.i(TAG, "newInstance: " + customerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        rltDrafts = view.findViewById(R.id.drafts);

        txtOrderEmpty = view.findViewById(R.id.txtOrderEmpty);
        txtDraftEmpty = view.findViewById(R.id.txtDraftEmpty);
        lstOrder = view.findViewById(R.id.lstOrders);
        lstDraft = view.findViewById(R.id.lstDrafts);
        scrollView = view.findViewById(R.id.scrollView);
        RecyclerView.LayoutManager mLayoutManagerOrder = new LinearLayoutManager(context);
        lstOrder.setLayoutManager(mLayoutManagerOrder);
        lstOrder.setItemAnimator(new DefaultItemAnimator());
        lstOrder.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstOrder.setHasFixedSize(false);
        lstOrder.setItemViewCacheSize(20);
        lstOrder.setDrawingCacheEnabled(true);
        lstOrder.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstDraft.setNestedScrollingEnabled(false);

        lstOrder.invalidate();

        RecyclerView.LayoutManager mLayoutManagerDraft = new LinearLayoutManager(context);
        lstDraft.setLayoutManager(mLayoutManagerDraft);
        lstDraft.setItemAnimator(new DefaultItemAnimator());
        lstDraft.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstDraft.setHasFixedSize(false);
        lstDraft.setItemViewCacheSize(20);
        lstDraft.setDrawingCacheEnabled(true);
        lstDraft.setNestedScrollingEnabled(false);
        lstDraft.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        lstDraft.invalidate();

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {

            if (customerId != 0) {
                customer = customersDB.getCustomerById(customerId);
                ShoppingCart.setCart(null);
                ShoppingCart.setCustomer(customer);
                Fragment fragment = new OrderCreationFragment();
                AppCompatActivity activity = (AppCompatActivity) view1.getContext();
                   FragmentManager fragmentManager = activity.getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();

            } else {
                ShoppingCart.setCart(null);
                ShoppingCart.setCustomer(null);
                Fragment fragment = CustomerFragment.newInstance(false,false);
                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });

        lstDraft.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        lstOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View mProgressView = getActivity().findViewById(R.id.login_progress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.orders));
        ordersDB = new OrdersDB(context);

        if (customerId == 0) {
            orders = ordersDB.getOrders("");
        } else {
            orders = ordersDB.getOrdersByCustomer(customerId, 0);
            rltDrafts.setVisibility(View.GONE);
        }

        orderAdapter = new OrderAdapter(context, orders);
        lstOrder.setAdapter(orderAdapter);

        drafts = ordersDB.getOrders("Draft");
        orderAdapter = new OrderAdapter(context, drafts);
        lstDraft.setAdapter(orderAdapter);

        if (orders.size() == 0) {
            txtOrderEmpty.setVisibility(View.VISIBLE);
        } else {
            txtOrderEmpty.setVisibility(View.GONE);
        }

        if (drafts.size() == 0) {
            txtDraftEmpty.setVisibility(View.VISIBLE);
        } else {
            txtDraftEmpty.setVisibility(View.GONE);
        }
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
