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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.ExpenseAdapter;
import com.kraftlabs.crm_new.Db.ExpenseDB;
import com.kraftlabs.crm_new.Models.Expense;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;


public class ExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private ExpenseDB expenseDB;
    private TextView txtExpenseEmpty;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView lstExpense;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        txtExpenseEmpty = (TextView) view.findViewById(R.id.txtExpenseEmpty);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ExpenseFormFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });


        lstExpense = (RecyclerView) view.findViewById(R.id.lstMessage);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstExpense.setLayoutManager(mLayoutManager);
        lstExpense.setItemAnimator(new DefaultItemAnimator());
        lstExpense.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstExpense.setHasFixedSize(false);
        lstExpense.setItemViewCacheSize(20);
        lstExpense.setDrawingCacheEnabled(true);
        lstExpense.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        lstExpense.invalidate();
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.expense));
        expenseDB = new ExpenseDB(context);
        ArrayList<Expense> expenses = expenseDB.getExpenses(0, 50);
        expenseAdapter = new ExpenseAdapter(context, expenses);
        lstExpense.setAdapter(expenseAdapter);
        if (expenses.size() == 0) {
            txtExpenseEmpty.setVisibility(View.VISIBLE);
        } else {
            txtExpenseEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
