package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.kraftlabs.crm_new.Adapters.CollectionListAdapter;
import com.kraftlabs.crm_new.Db.CollectionDB;
import com.kraftlabs.crm_new.Models.Collection;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * Created by AShik on 9/11/15.
 */

public class CollectionFragment extends Fragment {
    private static final String TAG = "COllection Fragment";
    private static final String CUSTOMER_ID = "CUSTOMER_ID ";
    private static boolean isListing = true;
    private int customerId = 0;

    private Context context;
    private CollectionDB collectionDB;
    private OnFragmentInteractionListener mListener;
    private CollectionListAdapter collectionListAdapter;
    private RecyclerView lstCollection;
    private ArrayList<Collection> mCollections;
    private TextView txtCollectionEmpty;

    public CollectionFragment() {
        // Required empty public constructor
    }

    public static CollectionFragment newInstance(int mCustomerId) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putInt(CUSTOMER_ID, mCustomerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            customerId = getArguments().getInt(CUSTOMER_ID);

            Log.i(TAG, "onCreate: CustomerID" + customerId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        lstCollection = (RecyclerView) view.findViewById(R.id.lstCollection);
        txtCollectionEmpty = (TextView) view.findViewById(R.id.txtCollectionEmpty);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = CollectionFormFragment.newInstance(0, customerId);
                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCollection.setLayoutManager(mLayoutManager);
        lstCollection.setItemAnimator(new DefaultItemAnimator());
        lstCollection.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCollection.setHasFixedSize(false);
        lstCollection.setItemViewCacheSize(20);
        lstCollection.setDrawingCacheEnabled(true);
        lstCollection.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCollection.invalidate();

        hideKeyboard(view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getActivity().getResources().getString(R.string.collection));

        collectionDB = new CollectionDB(context);
        mCollections = collectionDB.getCollectionByCustomer(customerId);
        collectionListAdapter = new CollectionListAdapter(context, mCollections, customerId);
        lstCollection.setAdapter(collectionListAdapter);
        if (mCollections.size() == 0) {
            txtCollectionEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCollectionEmpty.setVisibility(View.GONE);
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
