package com.kraftlabs.crm_new.Fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Activities.SalesRepActivity;
import com.kraftlabs.crm_new.Adapters.CartItemAdapter;
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


public class OrderCreationFragment extends Fragment {
    private static final String TAG = "OrderCreationFragment";
    private static final String LOCAL_ORDER_ID = "local_order_id";
    private static CartItemAdapter adapter;
    private static int localOrderId = 0;
    private TextView numberOfItems;
    private TextView grossAmount;
    private OnFragmentInteractionListener mListener;
    private ArrayList<CartItem> mCartItems;
    private boolean isPartiallySaved = false;
    private ProgressDialog prDialog;
    private Button btnSyncOrder;
    private Button btnPartialSave;
    private Button btnAddItems;
    private Button btnOk;
    private TextView txtOrderItemEmpty;
    private Context context;
    private RecyclerView lstCart;
    private String orderStatus;

    public OrderCreationFragment() {
        // Required empty public constructor
    }

    public static OrderCreationFragment newInstance(int localOrderId) {
        OrderCreationFragment fragment = new OrderCreationFragment();
        Bundle args = new Bundle();
        args.putInt(LOCAL_ORDER_ID, localOrderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if(getArguments() != null) {
            localOrderId = getArguments().getInt(LOCAL_ORDER_ID);
            OrdersDB ordersDB = new OrdersDB(context);
            Order order = ordersDB.getOrder(localOrderId);
            CustomersDB customersDB = new CustomersDB(context);
            Customer customer = customersDB.getCustomerByCode(order.getCustomerCode());
            OrderItemsDB orderItemsDB = new OrderItemsDB(context);
            ArrayList<OrderItem> orderItems = orderItemsDB.getOrderItems(order.getId());
            ArrayList<CartItem> cart = new ArrayList<CartItem>();
            for(int i = 0; i < orderItems.size(); i++) {
                CartItem cartItem = new CartItem(orderItems.get(i).getProductId(), orderItems.get(i).getProductName(), orderItems.get(i).getProductCode(),
                        orderItems.get(i).getQuantity(), orderItems.get(i).getPrice(), orderItems.get(i).getCategory()
                );
                cart.add(cartItem);
            }
            ShoppingCart.setCustomer(customer);
            ShoppingCart.setCart(cart);
            isPartiallySaved = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_creation, container, false);
        txtOrderItemEmpty = (TextView) view.findViewById(R.id.txtOrderItemEmpty);
        Customer customer = ShoppingCart.getCustomer();
        TextView txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        TextView txtCustomerCode = (TextView) view.findViewById(R.id.txtCustomerCode);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        TextView txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        TextView txtCity = (TextView) view.findViewById(R.id.txtCity);
        txtCustomerName.setText(String.format("%s (%s)", customer.getCustomerName(), customer.getCustomerCode()));
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());
        txtCity.setText(customer.getCity());
        if(customer.getAddress().isEmpty() || customer.getAddress().equals(null) || customer.getAddress().equals("null")) {
            txtAddress.setVisibility(View.GONE);
        }
        if(customer.getPhone().isEmpty() || customer.getPhone().equals(null) || customer.getPhone().equals("null")) {
            txtPhone.setVisibility(View.GONE);
        }
        txtCustomerCode.setVisibility(View.GONE);
        numberOfItems = (TextView) view.findViewById(R.id.txtNumberOfItems);
        grossAmount = (TextView) view.findViewById(R.id.txtGrossAmount);
        lstCart = (RecyclerView) view.findViewById(R.id.lstCart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstCart.setLayoutManager(mLayoutManager);
        lstCart.setItemAnimator(new DefaultItemAnimator());
        lstCart.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstCart.setHasFixedSize(false);
        lstCart.setItemViewCacheSize(20);
        lstCart.setDrawingCacheEnabled(true);
        lstCart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstCart.invalidate();
        mCartItems = ShoppingCart.getCart();
        updateQuantityAndPrice();
        btnAddItems = (Button) view.findViewById(R.id.btnAddItems);
        btnSyncOrder = (Button) view.findViewById(R.id.btnSave);
        btnPartialSave = (Button) view.findViewById(R.id.btnPartialSave);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ProductFragment.newInstance(false);
                String backStateName = fragment.getClass().getName();
                   FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.content_main, fragment);
                fragmentManager.addToBackStack(backStateName);
                fragmentManager.commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });
        btnSyncOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet(context)) {
                    if(ShoppingCart.getCart().size() != 0) {
                        saveOrderLocal();
                        saveToServer();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_LONG).show();
                     //   Toasty.info(getActivity().getApplicationContext(), context.getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    if(ShoppingCart.getCart().size() != 0) {
                        saveOrderLocal();
                        btnSyncOrder.setVisibility(View.GONE);
                        btnPartialSave.setVisibility(View.GONE);
                        btnAddItems.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.data_inserted_into_local, Toast.LENGTH_SHORT).show();
                        btnOk.setVisibility(View.VISIBLE);
                        /*ShoppingCart.setCart(null);
                        Fragment fragment = OrderDetailsFragment.newInstance(localOrderId);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        String backStateName = fragment.getClass().getName();
                        ft.addToBackStack(backStateName);
                        ft.commit();*/
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_SHORT).show();
                    }

                    /*if (ShoppingCart.getCart().size() != 0) {
                        Toasty.error(context,
                                     context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT,
                                     true
                        ).show();
                    }*/
                }
            }
        });
        btnPartialSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShoppingCart.getCart().size() != 0) {
                    saveOrderPartial();
                    btnSyncOrder.setVisibility(View.GONE);
                    btnPartialSave.setVisibility(View.GONE);
                    btnAddItems.setVisibility(View.GONE);
                    btnOk.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity().getApplicationContext(),context.getResources().getString(R.string.no_items_in_cart) , Toast.LENGTH_LONG).show();
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });
        hideKeyboard(view);
        return view;
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }

    public void updateQuantityAndPrice() {
        Log.i(TAG, "test: " + mCartItems.size());
        numberOfItems.setText(String.valueOf(mCartItems.size()));
        grossAmount.setText(String.valueOf(ShoppingCart.getTotal()));
    }

    private void saveOrderPartial() {
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        String userName = PrefUtils.getCurrentUser(context).getName();
        Customer customer = ShoppingCart.getCustomer();
        int customerId = Integer.valueOf(customer.getCustomerId());
        String customerName = customer.getCustomerName();
        String customerCode = customer.getCustomerCode();
        float grossAmount = ShoppingCart.getTotal();
        OrdersDB ordersDB = new OrdersDB(getActivity());
        OrderItemsDB orderItemsDB = new OrderItemsDB(getActivity());
        String orderStatus = "Draft";
        ArrayList<CartItem> cartItems = ShoppingCart.getCart();
        if(isPartiallySaved) {
            ordersDB.updateOrder(userId, userName, customerId, customerName, customerCode, grossAmount, orderStatus, localOrderId, cartItems.size());
            orderItemsDB.delete(localOrderId);
        } else {
            localOrderId = ordersDB.insert(userId, userName, customerId, customerName, customerCode, grossAmount, orderStatus, cartItems.size());
        }
        for(int i = 0; i < cartItems.size(); i++) {
            orderItemsDB.insert(localOrderId, cartItems.get(i).getProductName(), cartItems.get(i).getProductCode(),
                    cartItems.get(i).getQuantity(), cartItems.get(i).getPrice(), cartItems.get(i).getProductId(), cartItems.get(i).getCategory()
            );
        }
        isPartiallySaved = true;
        Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.order_added_as_draft), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.order_added_as_draft), Toast.LENGTH_LONG).show();
    }

    private void saveOrderLocal() {
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        String userName = PrefUtils.getCurrentUser(context).getName();
        Customer customer = ShoppingCart.getCustomer();
        int customerId = Integer.valueOf(customer.getCustomerId());
        String customerName = customer.getCustomerName();
        String customerCode = customer.getCustomerCode();
        float grossAmount = ShoppingCart.getTotal();
        OrdersDB ordersDB = new OrdersDB(getActivity());
        OrderItemsDB orderItemsDB = new OrderItemsDB(getActivity());
        orderStatus = "Saved";
        ArrayList<CartItem> cartItems = ShoppingCart.getCart();
        if(isPartiallySaved) {
            ordersDB.updateOrder(userId, userName, customerId, customerName, customerCode, grossAmount, orderStatus, localOrderId, cartItems.size());
            orderItemsDB.delete(localOrderId);
        } else {
            localOrderId = ordersDB.insert(userId, userName, customerId, customerName, customerCode, grossAmount, orderStatus, cartItems.size());
            Log.i(TAG, "saveOrderLocal: " + localOrderId);
        }
        for(int i = 0; i < cartItems.size(); i++) {
            orderItemsDB.insert(localOrderId, cartItems.get(i).getProductName(), cartItems.get(i).getProductCode(),
                    cartItems.get(i).getQuantity(), cartItems.get(i).getPrice(), cartItems.get(i).getProductId(), cartItems.get(i).getCategory()
            );
        }
    }

    private void showHideSyncButton(String orderStatus) {
        if(orderStatus.equals("Saved")) {
            btnAddItems.setVisibility(View.GONE);
            btnPartialSave.setVisibility(View.GONE);
        } else if(orderStatus.equals("Synced")) {
            btnSyncOrder.setVisibility(View.GONE);
            btnAddItems.setVisibility(View.GONE);
            btnPartialSave.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
        } else {
            btnSyncOrder.setVisibility(View.VISIBLE);
        }
    }

    public void saveToServer() {
        ((SalesRepActivity) context).showProgress(true);
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
                            OrdersDB ordersDB = new OrdersDB(getActivity());
                            ordersDB.update(localOrderId, orderId, invoiceNumber, date, orderStatus);
                            //Toast.makeText(getActivity().getApplicationContext(), "Order Synced successfully", Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.order_synced), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            //   Toast.makeText(getActivity().getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
                            Log.i(TAG, e.getMessage());
                        } catch (NullPointerException e) {
                            Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            //    Toast.makeText(getActivity().getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
                            Log.i(TAG, e.getMessage());
                        }
                        showHideSyncButton(orderStatus);
                        ((SalesRepActivity) context).showProgress(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showHideSyncButton(orderStatus);
                        Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getActivity().getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
                        ((SalesRepActivity) context).showProgress(false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Customer customer = ShoppingCart.getCustomer();
                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();
                params.put("storeId", customer.getCustomerId() + "");
                params.put("areaId", customer.getAreaId() + "");
                params.put("divisionCode", customer.getDivisionCode() + "");
                params.put("items", ShoppingCart.cartItemsToJSON());
                params.put("user_id", Integer.toString(userId));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void onButtonPressed(Uri uri) {
        if(mListener != null) {
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
        getActivity().setTitle(getActivity().getResources().getString(R.string.create_order));
        mCartItems = ShoppingCart.getCart();
        adapter = new CartItemAdapter(getActivity(), mCartItems, this);
        lstCart.setAdapter(adapter);
        if(mCartItems.size() == 0) {
            txtOrderItemEmpty.setVisibility(View.VISIBLE);
        } else {
            txtOrderItemEmpty.setVisibility(View.GONE);
        }
        btnSyncOrder.setVisibility(View.VISIBLE);
        btnPartialSave.setVisibility(View.VISIBLE);
        btnAddItems.setVisibility(View.VISIBLE);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
