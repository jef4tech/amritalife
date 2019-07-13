package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kraftlabs.crm_new.Activities.SalesRepActivity;
import com.kraftlabs.crm_new.Adapters.LeadAdapter;
import com.kraftlabs.crm_new.Adapters.RouteCustomerAdapter;
import com.kraftlabs.crm_new.Adapters.TaskAdapter;
import com.kraftlabs.crm_new.AdditionalData.AdditionalInfoFragment;
import com.kraftlabs.crm_new.AdditionalData.ImageData.ImageFragment;
import com.kraftlabs.crm_new.Db.LeadDB;
import com.kraftlabs.crm_new.Db.RouteCustomersDB;
import com.kraftlabs.crm_new.Db.RouteDB;
import com.kraftlabs.crm_new.Db.TaskDB;
import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.Models.Lead;
import com.kraftlabs.crm_new.Models.Task;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.libs.expandableLayout.ExpandableLayout.ExpandableLayout;
import com.refresh.menuitem.RefreshMenuItemHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class
HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    LayerDrawable icon;
    RefreshMenuItemHelper refreshHelper;
    Drawable imgDown;
    Drawable imgUp;
    Intent countIntent;
    private String mParam1;
    private String mParam2;
    private Context context;
    private RouteCustomersDB routeCustomersDB;
    private TaskDB taskDB;
    private LeadDB leadDB;
    private ArrayList<Call> routeCustomers;
    private ArrayList<Task> tasks;
    private ArrayList<Lead> leads;
    private RouteCustomerAdapter routeCustomerAdapter;
    private LeadAdapter leadAdapter;
    private RecyclerView lstCall;
    private RecyclerView lstTask;
    private RecyclerView lstLead;
    private TextView txtTaskEmpty;
    private TextView txtCallEmpty;
    private TextView txtLeadEmpty;
    private SalesRepActivity salesRepActivity;
    private int mCount;
    private OnFragmentInteractionListener mListener;
    private CallFormFragment callFormFragment;
    private CallCommentFragment callCommentFragment;
    private TaskAdapter taskAdapter;
    private ExpenseFormFragment expenseFormFragment;
    private NewCallFormFragment newCallFormFragment;
    private LeadCommentFragment leadCommentFragment;
    private CollectionFormFragment collectionFormFragment;
    private OrderDetailsFragment orderDetailsFragment;
   // private LoggingExceptionHandler loggingExceptionHandler;
    private AdditionalInfoFragment additionalInfoFragment;
    private ImageFragment imageFragment;
    //  private GpsService gpsService;
    private Handler handler = null;
    private Menu menu;
    private Task task;
    private ScrollView scrollView;
    private TextView txtCallsHeading, txtTaskHeading, txtLeadHeading;
    private ExpandableLayout expandableLayout1, expandableLayout2, expandableLayout3;
    private TextView txtLastSync;

    public HomeFragment() {
        /* Required empty public constructor */
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        context = getActivity();
        task = new Task();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtCallEmpty = (TextView) view.findViewById(R.id.txtCallEmpty);
        txtTaskEmpty = (TextView) view.findViewById(R.id.txtTaskEmpty);
        txtLeadEmpty = (TextView) view.findViewById(R.id.txtLeadEmpty);
        txtCallsHeading = (TextView) view.findViewById(R.id.txtCallHeading);
        txtTaskHeading = (TextView) view.findViewById(R.id.txtTaskHeading);
        txtLeadHeading = (TextView) view.findViewById(R.id.txtLeadHeading);
        lstCall = (RecyclerView) view.findViewById(R.id.lstCall);
        lstTask = (RecyclerView) view.findViewById(R.id.lstTask);
        lstLead = (RecyclerView) view.findViewById(R.id.lstLead);
        expandableLayout1 = (ExpandableLayout) view.findViewById(R.id.expandable_layout_1);
        expandableLayout2 = (ExpandableLayout) view.findViewById(R.id.expandable_layout_2);
        expandableLayout3 = (ExpandableLayout) view.findViewById(R.id.expandable_layout_3);
        txtLastSync = view.findViewById(R.id.txtLastSync);
        imgDown = getContext().getResources().getDrawable(R.mipmap.ic_keyboard_arrow_down_blue_24dp);
        imgUp = getContext().getResources().getDrawable(R.mipmap.ic_keyboard_arrow_up_blue_24dp);
        imgDown.setBounds(0, 0, 60, 60);
        imgUp.setBounds(0, 0, 60, 60);
        txtCallsHeading.setCompoundDrawables(null, null, imgUp, null);
        txtTaskHeading.setCompoundDrawables(null, null, imgDown, null);
        txtLeadHeading.setCompoundDrawables(null, null, imgDown, null);

        expandableLayout1.setOnExpansionUpdateListener((expansionFraction, state) -> Log.i("ExpandableLayout1", "State: " + state));
        txtCallsHeading.setOnClickListener(view1 -> {
            if (expandableLayout1.isExpanded()) {
                expandableLayout1.collapse();
                txtCallsHeading.setCompoundDrawables(null, null, imgDown, null);
            } else {
                txtCallsHeading.setCompoundDrawables(null, null, imgUp, null);
                expandableLayout1.expand();
                expandableLayout2.collapse();
                expandableLayout3.collapse();
            }
            txtTaskHeading.setCompoundDrawables(null, null, imgDown, null);
            txtLeadHeading.setCompoundDrawables(null, null, imgDown, null);

        });
        txtTaskHeading.setOnClickListener(view14 -> {
            if (expandableLayout2.isExpanded()) {
                txtTaskHeading.setCompoundDrawables(null, null, imgDown, null);
                expandableLayout2.collapse();
            } else {
                txtTaskHeading.setCompoundDrawables(null, null, imgUp, null);
                expandableLayout2.expand();
                expandableLayout1.collapse();
                expandableLayout3.collapse();
            }
            txtCallsHeading.setCompoundDrawables(null, null, imgDown, null);
            txtLeadHeading.setCompoundDrawables(null, null, imgDown, null);
        });
        txtLeadHeading.setOnClickListener(view13 -> {
            if (expandableLayout3.isExpanded()) {
                txtLeadHeading.setCompoundDrawables(null, null, imgDown, null);
                expandableLayout3.collapse();
            } else {
                txtLeadHeading.setCompoundDrawables(null, null, imgUp, null);
                expandableLayout3.expand();
                expandableLayout2.collapse();
                expandableLayout1.collapse();
            }
            txtCallsHeading.setCompoundDrawables(null, null, imgDown, null);
            txtTaskHeading.setCompoundDrawables(null, null, imgDown, null);
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCall.setLayoutManager(mLayoutManager);
        lstCall.setItemAnimator(new DefaultItemAnimator());
        lstCall.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCall.setHasFixedSize(false);
        lstCall.setItemViewCacheSize(20);
        lstCall.setDrawingCacheEnabled(true);
        lstCall.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCall.setNestedScrollingEnabled(true);
        lstCall.invalidate();
        RecyclerView.LayoutManager mLayoutManagerTask = new LinearLayoutManager(context);
        lstTask.setLayoutManager(mLayoutManagerTask);
        lstTask.setItemAnimator(new DefaultItemAnimator());
        lstTask.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstTask.setHasFixedSize(false);
        lstTask.setItemViewCacheSize(20);
        lstTask.setDrawingCacheEnabled(true);
        lstTask.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstTask.setNestedScrollingEnabled(true);
        lstTask.invalidate();
        RecyclerView.LayoutManager mLayoutManagerLead = new LinearLayoutManager(context);
        lstLead.setLayoutManager(mLayoutManagerLead);
        lstLead.setItemAnimator(new DefaultItemAnimator());
        lstLead.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstLead.setHasFixedSize(false);
        lstLead.setItemViewCacheSize(20);
        lstLead.setDrawingCacheEnabled(true);
        lstLead.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstLead.setNestedScrollingEnabled(true);
        lstLead.invalidate();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view12 -> {
            Fragment fragment = new NewCallFormFragment();
               FragmentManager fragmentManager =
                    getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            String backStateName = fragment.getClass().getName();
            ft.addToBackStack(backStateName);
            ft.commit();
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.home));
        refreshHelper = new RefreshMenuItemHelper();
        newCallFormFragment = new NewCallFormFragment(context);
        leadCommentFragment = new LeadCommentFragment(context);
        expenseFormFragment = new ExpenseFormFragment(context);
        collectionFormFragment = new CollectionFormFragment(context);
        callFormFragment = new CallFormFragment(context);
        callCommentFragment = new CallCommentFragment(context);
        taskAdapter = new TaskAdapter(context);
        orderDetailsFragment = new OrderDetailsFragment(context);
        //  gpsService = new GpsService(context);
      //  loggingExceptionHandler = new LoggingExceptionHandler(context);
        imageFragment = new ImageFragment(context);
        additionalInfoFragment = new AdditionalInfoFragment(context);
        salesRepActivity = new SalesRepActivity();
        RouteDB routeDB = new RouteDB(context);
        int routeAssignId = routeDB.getTodaysRoute();
        routeCustomersDB = new RouteCustomersDB(context);
        routeCustomers = routeCustomersDB.getRouteCustomers(routeAssignId, 0);
        routeCustomerAdapter = new RouteCustomerAdapter(context, routeCustomers, false);
        lstCall.setAdapter(routeCustomerAdapter);
        taskDB = new TaskDB(context);
        tasks = taskDB.getTasks("", true, 0, 100);
        taskAdapter = new TaskAdapter(context, tasks);
        lstTask.setAdapter(taskAdapter);
        leadDB = new LeadDB(context);
        leads = leadDB.getLeads(0, 100);
        leadAdapter = new LeadAdapter(context, leads);
        lstLead.setAdapter(leadAdapter);
        if (tasks.size() == 0) {
            txtTaskEmpty.setVisibility(View.VISIBLE);
        } else {
            txtTaskEmpty.setVisibility(View.GONE);
        }
        if (routeCustomers.size() == 0) {
            txtCallEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCallEmpty.setVisibility(View.GONE);
        }
        if (leads.size() == 0) {
            txtLeadEmpty.setVisibility(View.VISIBLE);
        } else {
            txtLeadEmpty.setVisibility(View.GONE);
        }
      //  new FetchCountTask().execute();

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

    public void showOverflowMenu() {
        if (menu == null) {
            return;
        }
        menu.setGroupVisible(R.id.done_menu_group, false);
        menu.setGroupVisible(R.id.sync_menu_group, true);
    }

    public int dataCount() {
        int callsCount = callFormFragment.syncCount();
        int callCommentCount = callCommentFragment.syncCount();
        int expenseCount = expenseFormFragment.syncCount();
        int newCallCount = newCallFormFragment.syncCount();
        int leadCommentCount = leadCommentFragment.syncCount();
        int collectionCont = collectionFormFragment.syncCount();
        int orderCount = orderDetailsFragment.syncCount();
        //int locationData = gpsService.syncCount();
       // int exceptionCount = loggingExceptionHandler.syncCount();
        int imageCount = imageFragment.syncCount();
        int additionalDataCount = additionalInfoFragment.syncCount();
        //TODO:Task sync
        int count = callsCount
                + callCommentCount
                + expenseCount
                + newCallCount
                + leadCommentCount
                + collectionCont + orderCount  + imageCount + additionalDataCount;
        Log.i(TAG, "dataCount: callsCount==>" + callsCount
                + "\n callCommentCount==> " + callCommentCount
                + "\n expenseCount==>" + expenseCount
                + "\n newCallCount==>" + newCallCount
                + "\n leadCommentCount==>" + leadCommentCount
                + "\n collectionCont==>" + collectionCont
                + "\n orderCount==>" + orderCount
                + "\n exceptionCount==>"
                + "\n imageCount==>" + imageCount
                + "\n additionalDataCount==>" + additionalDataCount);
        return count;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        showOverflowMenu();
        this.menu = menu;
        inflater.inflate(R.menu.sync_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_sync);
        icon = (LayerDrawable) menuItem.getIcon();
      //  Utils.setBadgeCount(context, icon, dataCount());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                Toast.makeText(context,R.string.sync_data, Toast.LENGTH_SHORT).show();
                refreshHelper.setMenuItem(item);
             //   new FetchCountTask().execute();

                /*new CallFormFragment(context).sync();
                new CallCommentFragment(context).sync();
                new ExpenseFormFragment(context).sync();
                new NewCallFormFragment(context).sync();
                new LeadCommentFragment(context).sync();
                new CollectionFormFragment(context).sync();
                new OrderDetailsFragment(context).sync();
                new AdditionalInfoFragment(context).sync();*/


                // new GpsService(context).sync();

               /* new LoggingExceptionHandler(context).sync();
                new ImageFragment(context).sync();

                icon = (LayerDrawable) item.getIcon();
                Utils.setBadgeCount(context, icon, dataCount());*/
                return true;
        }
      //  Utils.setBadgeCount(context, icon, dataCount());
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class FetchCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshHelper.startLoading();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            return dataCount();
        }

        @Override
        public void onPostExecute(Integer count) {
           // Utils.setBadgeCount(context, icon, dataCount());
            refreshHelper.stopLoading();
        }
    }
}
