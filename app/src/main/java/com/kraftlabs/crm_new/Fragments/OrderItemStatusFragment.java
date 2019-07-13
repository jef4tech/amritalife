package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.ItemStatusAdapter;
import com.kraftlabs.crm_new.Db.OrderItemsDB;
import com.kraftlabs.crm_new.Models.OrderItem;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.libs.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderItemStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderItemStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderItemStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayAdapter<CharSequence> adapter;
    private Context context;
    private OrderItemsDB orderItemsDB;
    private TextView txtItemEmpty;
    private ItemStatusAdapter itemStatusAdapter;
    private RecyclerView lstItemStatus;
    private ArrayList<OrderItem> items;
    private Spinner spinner;
    private EndlessRecyclerOnScrollListener listener;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderItemStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderItemStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderItemStatusFragment newInstance(String param1, String param2) {
        OrderItemStatusFragment fragment = new OrderItemStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_item_status, container, false);
        txtItemEmpty = (TextView) view.findViewById(R.id.txtOrderStatusEmpty);
        lstItemStatus = view.findViewById(R.id.lstOrderItemStatus);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstItemStatus.setLayoutManager(mLayoutManager);
        listener=new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

            }
        };
        lstItemStatus.setItemAnimator(new DefaultItemAnimator());
        lstItemStatus.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstItemStatus.setHasFixedSize(false);
        lstItemStatus.setItemViewCacheSize(20);
        lstItemStatus.setDrawingCacheEnabled(true);
        lstItemStatus.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstItemStatus.invalidate();
        return view;
        //return inflater.inflate(R.layout.fragment_order_item_status, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        getActivity().setTitle(getActivity().getResources().getString(R.string.order_status));
        orderItemsDB = new OrderItemsDB(context);
        setItemStatusAdapter("01");
        isEmpty();

    }

    public void setItemStatusAdapter(String interval) {
        items = orderItemsDB.getOrderItemsByDate(interval);
        itemStatusAdapter = new ItemStatusAdapter(context, items);
        lstItemStatus.setAdapter(itemStatusAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.spn_month, menu);
        MenuItem menuItem = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(menuItem);
        adapter = ArrayAdapter.createFromResource(context,
                R.array.sales_status, R.layout.spnr_month_item);
        adapter.setDropDownViewResource(R.layout.spn_month_drop_down);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://jan
                        setItemStatusAdapter("01");
                        isEmpty();
                        break;
                    case 1://feb
                        setItemStatusAdapter("02");
                        isEmpty();
                        break;
                    case 2://Mar
                        setItemStatusAdapter("03");
                        isEmpty();
                        break;
                    case 3://Apr
                        setItemStatusAdapter("04");
                        isEmpty();
                        break;
                    case 4://May
                        setItemStatusAdapter("05");
                        isEmpty();
                        break;
                    case 5://Jun
                        setItemStatusAdapter("06");
                        isEmpty();
                        break;
                    case 6://Jul
                        setItemStatusAdapter("07");
                        isEmpty();
                        break;
                    case 7://Aug
                        setItemStatusAdapter("08");
                        isEmpty();
                        break;
                    case 8://Sep
                        setItemStatusAdapter("09");
                        isEmpty();
                        break;
                    case 9://Oct
                        setItemStatusAdapter("10");
                        isEmpty();
                        break;
                    case 10://nov
                        setItemStatusAdapter("11");
                        isEmpty();
                        break;
                    case 11://Dec
                        setItemStatusAdapter("12");
                        isEmpty();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        isEmpty();
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void isEmpty() {
        if (items.size() == 0) {
            txtItemEmpty.setVisibility(View.VISIBLE);
        } else {
            txtItemEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.spinner:


                break;
        }
       /* switch (item.getItemId()) {
            case R.id.spinner:
                

          break;
        }*/
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
