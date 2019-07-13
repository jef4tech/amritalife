package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kraftlabs.crm_new.Adapters.OrderItemAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.OrderItemsDB;
import com.kraftlabs.crm_new.Db.OrdersDB;
import com.kraftlabs.crm_new.Models.CartItem;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.Models.Order;
import com.kraftlabs.crm_new.Models.OrderItem;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;
import com.kraftlabs.crm_new.Util.ServiceManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class OrderDetailsFragment extends Fragment {

    private static final String LOCAL_ORDER_ID = "local_order_id";
    private static String TAG = "OrderDetailsFragment";
    private int localOrderId;
    private Context context;
    private Button btnSyncOrder;
    private Button btnSupplyStatus;
    private ProgressDialog prDialog;
    private Order order;
    private RecyclerView lstCart;
    private ArrayList<CartItem> mCartItems;
    private OrderItemAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private String orderStatus = "";
    private TextView txtSyncStatus;
    private TextView txtOrderItemEmpty;
    String asdf;

    private ArrayList<Order> orders;

    private OrdersDB ordersDB;
    private CustomersDB customersDB;
    private OrderItemsDB orderItemsDB;
    private Gson gson = new Gson();

    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public OrderDetailsFragment(Context context) {
        this.context = context;
    }

    public static OrderDetailsFragment newInstance(int localOrderId) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(LOCAL_ORDER_ID, localOrderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        ordersDB = new OrdersDB(context);
        customersDB = new CustomersDB(context);
        orderItemsDB = new OrderItemsDB(context);
        if (getArguments() != null) {
            localOrderId = getArguments().getInt(LOCAL_ORDER_ID);
            Log.i(TAG, "onCreate: Local ID" + localOrderId);
//            Log.i(TAG, "onCreate: Order ID"+order.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        txtOrderItemEmpty = (TextView) view.findViewById(R.id.txtOrderItemEmpty);

        OrdersDB ordersDB = new OrdersDB(getActivity());
        order = ordersDB.getOrder(localOrderId);

        TextView txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        TextView txtOrderNumber = (TextView) view.findViewById(R.id.txtOrderNumber);
        TextView txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
        TextView txtNumberOfItems = (TextView) view.findViewById(R.id.txtNumberOfItems);
        TextView txtGrossAmount = (TextView) view.findViewById(R.id.txtGrossAmount);
        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtSyncStatus = (TextView) view.findViewById(R.id.txtSyncStatus);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnSyncOrder = (Button) view.findViewById(R.id.btnSyncOrder);
        btnSupplyStatus = (Button) view.findViewById(R.id.btnSupplyStatus);

        if (order.getOrderStatus().equals("Saved")) {
            btnSyncOrder.setVisibility(View.VISIBLE);
        } else {
            btnSyncOrder.setVisibility(View.INVISIBLE);
        }

        txtCustomerName.setText(order.getCustomerName() + "(" + order.getCustomerCode() + ")");
        txtOrderNumber.setText(context.getResources().getString(R.string.order_no) + (
                TextUtils.isEmpty(order.getOrderNumber()) ? "" : order.getOrderNumber()));
        if (TextUtils.isEmpty(order.getOrderSyncDate())) {
            txtOrderDate.setText(order.getOrderDate());
        } else {
            txtOrderDate.setText(order.getOrderSyncDate());
        }
        txtNumberOfItems.setText(order.getNumberofItems() + "");
        txtGrossAmount.setText(order.getGrossAmount() + "");
        txtStatus.setText(order.getOrderStatus());
        //showHideSyncButton(order.getOrderStatus());

        lstCart = (RecyclerView) view.findViewById(R.id.lstCart);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCart.setLayoutManager(mLayoutManager);
        lstCart.setItemAnimator(new DefaultItemAnimator());
        lstCart.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCart.setHasFixedSize(false);
        lstCart.setItemViewCacheSize(20);
        lstCart.setDrawingCacheEnabled(true);
        lstCart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCart.invalidate();

        btnSupplyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OrdersDB ordersDB = new OrdersDB(getActivity());
                Order order = ordersDB.getOrder(localOrderId);
                Fragment fragment = TabFragment.newInstance(order.getOrderNumber());
                String backStateName = fragment.getClass().getName();
                   FragmentTransaction fragmentManager =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.content_main, fragment);
                fragmentManager.addToBackStack(backStateName);
                fragmentManager.commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderFragment();
                   FragmentManager fragmentManager =
                        getActivity().getSupportFragmentManager();
                   FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });

        btnSyncOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet(context)) {

                    btnSyncOrder.setVisibility(View.INVISIBLE);

                    OrdersDB ordersDB = new OrdersDB(context);
                    Order mOrder = ordersDB.getOrder(localOrderId);
                    CustomersDB customersDB = new CustomersDB(context);
                    Customer customer = customersDB.getCustomerByCode(mOrder.getCustomerCode());
                    ShoppingCart.setCustomer(customer);

                    OrderItemsDB orderItemsDB = new OrderItemsDB(context);
                    ArrayList<OrderItem> orderItems = orderItemsDB.getOrderItems(localOrderId);
                    ArrayList<CartItem> cart = new ArrayList<>();
                    for (int i = 0; i < orderItems.size(); i++) {
                        CartItem cartItem =
                                new CartItem(orderItems.get(i).getProductId(), orderItems.get(i).getProductName(),
                                             orderItems.get(i).getProductCode(),
                                             orderItems.get(i).getQuantity(), orderItems.get(i).getPrice(),
                                             orderItems.get(i).getCategory()
                                );
                        cart.add(cartItem);
                    }
                    ShoppingCart.setCart(cart);
                   /* saveToServer();*/
                    sync();
                } else {
                    Toast.makeText(context,
                                 context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        hideKeyboard(view);
        return view;
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }

    public int sync() {
        try {
            ordersDB = new OrdersDB(context);
            customersDB = new CustomersDB(context);
        } catch (NullPointerException e) {
            ordersDB = new OrdersDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }

        orders = ordersDB.getUnsentData();
        //Log.i(TAG, "sync: OrderDb Context" + context);
        //Log.i(TAG, "sync: " + orders);
        if (orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                order = orders.get(i);
                // setCart();
                saveToServer(order.getId());
                Log.i(TAG, "sync:+Local OrderId=======>" + localOrderId);
            }
        }

        return orders.size();
    }

    public int syncCount() {
        try {
            ordersDB = new OrdersDB(context);
            customersDB = new CustomersDB(context);
        } catch (NullPointerException e) {
            ordersDB = new OrdersDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        orders = ordersDB.getUnsentData();
        return orders.size();
    }

    public void saveToServer(final int id) {
        Log.i(TAG, "saveToServer: First");
        //    ((SalesRepActivity) context).showProgress(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ORDER_URL,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.i(TAG, s);

                                                                try {

                                                                    JSONObject object = new JSONObject(s);
                                                                    String status = object.get("status").toString();
                                                                    String orderId = object.get("order_id").toString();
                                                                    String invoiceNumber = object.get("invoice_number").toString();
                                                                    String date = object.get("date").toString();
                                                                    orderStatus = "Synced";

                                                                    order = ordersDB.getOrder(id);
                                                                    ordersDB.update(id, orderId, invoiceNumber, date, orderStatus);
                                                                    Log.i(TAG, "onResponse: ");
                                                                    //    Toast.makeText(getActivity().getApplicationContext(), "Order Synced successfully", Toast.LENGTH_LONG).show();
                  /* Toasty.success(getActivity().getApplicationContext(),
                                  context.getResources().getString(R.string.order_synced), Toast.LENGTH_SHORT, true
                   )
                           .show();*/
                                                                } catch (JSONException e) {
                  /* Toasty.error(getActivity().getApplicationContext(),
                                context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT,
                                true
                   ).show();*/

                                                                    Log.i(TAG, e.getMessage());
                                                                } catch (NullPointerException e) {
                  /* Toasty.error(context,
                                context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT,
                                true
                   ).show();*/

                                                                    Log.i(TAG, e.getMessage());
                                                                }
                                                                //showHideSyncButton(orderStatus);
                                                                //   ((SalesRepActivity) context).showProgress(false);
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                //            showHideSyncButton(orderStatus);
              /* Toasty.error(context,
                            context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT, true).show();*/

                                                                //    ((SalesRepActivity) context).showProgress(false);
                                                            }
                                                        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*order = ordersDB.getOrderById(id);*/
                /*Customer customer = ShoppingCart.getCustomer();*/
                /*customer=customersDB.getCustomerByCode(order.getCustomerCode());*/

                Map<String, String> params = null;

                OrdersDB ordersDB = new OrdersDB(context);
                CustomersDB customersDB = new CustomersDB(context);
                OrderItemsDB orderItemsDB = new OrderItemsDB(context);
                Order order = ordersDB.getOrder(id);

                Customer customer = customersDB.getCustomerByCode(order.getCustomerCode());

                ArrayList<OrderItem> orderItems = orderItemsDB.getOrderItems(order.getId());
                ArrayList<CartItem> cart = new ArrayList<CartItem>();
                for (int i = 0; i < orderItems.size(); i++) {
                    CartItem cartItem = new CartItem(orderItems.get(i).getProductId(),
                                                     orderItems.get(i).getProductName(), orderItems.get(i).getProductCode(),
                                                     orderItems.get(i).getQuantity(), orderItems.get(i).getPrice(),
                                                     orderItems.get(i).getCategory()
                    );
                    cart.add(cartItem);
                }
                ShoppingCart.setCart(cart);

                int userId = PrefUtils.getCurrentUser(context).getUserId();
                params = new Hashtable<String, String>();
                params.put("storeId", customer.getCustomerId() + "");
                params.put("areaId", customer.getAreaId() + "");
                params.put("divisionCode", customer.getDivisionCode() + "");
                params.put("items", ShoppingCart.cartItemsToJSON());
                params.put("user_id", Integer.toString(userId));
                Log.i(TAG, "getParams: " + params);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.order_details));
        OrderItemsDB orderItemsDB = new OrderItemsDB(getActivity());
        ArrayList<OrderItem> orderItems = orderItemsDB.getOrderItems(localOrderId);
        ArrayList<CartItem> cart = new ArrayList<>();
        for (int i = 0; i < orderItems.size(); i++) {
            CartItem cartItem =
                    new CartItem(orderItems.get(i).getProductId(), orderItems.get(i).getProductName(),
                                 orderItems.get(i).getProductCode(),
                                 orderItems.get(i).getQuantity(), orderItems.get(i).getPrice(),
                                 orderItems.get(i).getCategory()
                    );
            cart.add(cartItem);
        }
        ShoppingCart.setCart(cart);
        mCartItems = ShoppingCart.getCart();
        adapter = new OrderItemAdapter(getActivity(), mCartItems);
        lstCart.setAdapter(adapter);

        if (mCartItems.size() == 0) {
            txtOrderItemEmpty.setVisibility(View.VISIBLE);
        } else {
            txtOrderItemEmpty.setVisibility(View.GONE);
        }
        if (order.getOrderStatus().equals("Saved")) {
            btnSyncOrder.setVisibility(View.VISIBLE);
        } else if (order.getOrderStatus().equals("Synced")) {
            btnSyncOrder.setVisibility(View.GONE);
        }
    }

  /*
  private void showHideSyncButton(String orderStatus) {
    if (orderStatus.equals("Synced")) {
      btnSyncOrder.setVisibility(View.INVISIBLE);
      txtSyncStatus.setText("");
    } else {
      btnSyncOrder.setVisibility(View.VISIBLE);
      txtSyncStatus.setText(context.getResources().getString(R.string.not_send));
    }
  }
  */

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
             /*
Order order = ordersDB.getOrder(localOrderId);
  CustomersDB customersDB = new CustomersDB(context);
  Customer customer = customersDB.getCustomerByCode(order.getCustomerCode());
OrderItemsDB orderItemsDB = new OrderItemsDB(context);
  ArrayList<OrderItem> orderItems = orderItemsDB.getOrderItems(order.getId());
  ArrayList<CartItem> cart = new ArrayList<CartItem>();
  for (int i = 0; i < orderItems.size(); i++) {
  CartItem cartItem = new CartItem(orderItems.get(i).getProductId(), orderItems.get(i).getProductName(), orderItems.get(i).getProductCode(),
  orderItems.get(i).getQuantity(), orderItems.get(i).getPrice(), orderItems.get(i).getCategory());
  cart.add(cartItem);
  }
ShoppingCart.setCart(cart)
              */
