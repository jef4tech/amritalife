package com.kraftlabs.crm_new.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.kraftlabs.crm_new.Adapters.RouteCustomerAdapter;
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Db.RouteCustomersDB;
import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CallFragment extends Fragment {

    private static final String TAG = "CallFragment";
    private static final String ROUTE_ASSIGN_ID = "route__assign_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String IS_HISTORY = "is_listing";
    public static boolean isHistory;
    public static int customer_id;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    private RouteCustomersDB routeCustomersDB;
    private CallFragment.OnFragmentInteractionListener mListener;
    private RouteCustomerAdapter routeCustomerAdapter;
    private RecyclerView lstCall;
    private ArrayList<Call> routeCustomers;
    private TextView txtCallEmpty;
    private int routeAssignId;
    private CallCommentDB callCommentDB;
    private Call call;
    private Handler handler = null;

    public CallFragment() {
        // Required empty public constructor
    }

    public static CallFragment newInstance(int routeId) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putInt(ROUTE_ASSIGN_ID, routeId);
        fragment.setArguments(args);
        return fragment;
    }

    public static CallFragment newInstance(boolean isHistory, int customerId) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_HISTORY, isHistory);
        args.putInt(CUSTOMER_ID, customerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            routeAssignId = getArguments().getInt(ROUTE_ASSIGN_ID);
            isHistory = getArguments().getBoolean(IS_HISTORY);
            customer_id = getArguments().getInt(CUSTOMER_ID);
        }
        context = getActivity();
        callCommentDB = new CallCommentDB(context);
        call = new Call();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        lstCall = (RecyclerView) view.findViewById(R.id.lstCall);
        txtCallEmpty = (TextView) view.findViewById(R.id.txtCallEmpty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCall.setLayoutManager(mLayoutManager);
        lstCall.setItemAnimator(new DefaultItemAnimator());
        lstCall.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCall.setHasFixedSize(false);
        lstCall.setItemViewCacheSize(20);
        lstCall.setDrawingCacheEnabled(true);
        lstCall.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCall.invalidate();

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
        routeCustomersDB = new RouteCustomersDB(context);
        if (isHistory == true) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.call_history));
            //TODO:getCallHistory method from calls db
            //routeCustomers = routeCustomersDB.getRouteCustomers(routeAssignId, 0);
            routeCustomers = routeCustomersDB.getCallHistory(customer_id);
        } else if (isHistory != true) {
            getActivity().setTitle(getActivity().getResources().getString(R.string.call));
            routeCustomers = routeCustomersDB.getRouteCustomers(routeAssignId, 0);
        }

        //getActivity().setTitle(getActivity().getResources().getString(R.string.call));

        routeCustomerAdapter = new RouteCustomerAdapter(context, routeCustomers, isHistory);

        lstCall.setAdapter(routeCustomerAdapter);
        if (routeCustomers.size() == 0) {
            txtCallEmpty.setVisibility(View.VISIBLE);
        }
    }

    /* public boolean onKeyDown(int keyCode, KeyEvent event) {
         Intent intent;
         switch (keyCode) {
             case KeyEvent.KEYCODE_BACK:

                 AppCompatActivity activity = (AppCompatActivity) getContext();
                 Fragment fragment = CallFragment.newInstance(routeCustomerId);
                 android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                 android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                 ft.replace(R.id.content_main, fragment);
                 String backStateName = fragment.getClass().getName();
                 ft.addToBackStack(null);
                 ft.detach(fragment);
                 ft.attach(fragment);

                 ft.commit();
                 return true;
         }
         return false;
     }*/
    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
