package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.LeadCommentDB;
import com.kraftlabs.crm_new.Db.LeadDB;
import com.kraftlabs.crm_new.Models.Lead;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import static com.kraftlabs.crm_new.R.string.call;

public class NewCallFormFragment extends Fragment implements View.OnClickListener {

    private static final String LEAD_ID = "LEAD_ID";
    private static final String TAG = "CallFormFragment";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OnFragmentInteractionListener mListener;
    private int leadId;
    private Context context;
    private LeadDB leadDB;
    private Lead lead;
    private TextView txtCustomerName, txtAddress, txtPhone, txtDate, txtInfo, txtDetails;
    private Boolean editMode = false;
    private Button btnSubmit, btnViewComment;
    private int countComment;
    private LeadCommentDB leadCommentDB;
    private ArrayList<Lead> leads;
   /* OnFragmentTouched listener;*/
    public NewCallFormFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public NewCallFormFragment(Context context) {
        this.context = context;
    }

    public static NewCallFormFragment newInstance(int centerX, int centerY, int color) {
        Bundle args = new Bundle();
        args.putInt("cx", centerX);
        args.putInt("cy", centerY);
        args.putInt("color", color);
        NewCallFormFragment fragment = new NewCallFormFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public static NewCallFormFragment newInstance(int leadId) {
        NewCallFormFragment fragment = new NewCallFormFragment();
        Bundle args = new Bundle();
        args.putInt(LEAD_ID, leadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        leadDB = new LeadDB(context);
        leadCommentDB = new LeadCommentDB(context);
        lead = new Lead();
        if (getArguments() != null) {
            leadId = getArguments().getInt(LEAD_ID);
            editMode = true;

            lead = leadDB.getLead(leadId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_call_form, container, false);


        /* view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
             @Override
             public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                 v.removeOnLayoutChangeListener(this);
                 int cx = getArguments().getInt("cx");
                 int cy = getArguments().getInt("cy");

                 // get the hypothenuse so the radius is from one corner to the other
                 int radius = (int) Math.hypot(right, bottom);

                 Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                 reveal.setInterpolator(new DecelerateInterpolator(2f));
                 reveal.setDuration(1000);
                 reveal.start();
             }
         });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (listener != null) {
                        listener.onFragmentTouched(NewCallFormFragment.this, event.getX(), event.getY());
                    }
                    return true;

                }
            });*/







        txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtInfo = (TextView) view.findViewById(R.id.txtRemarks);
        txtDetails = (TextView) view.findViewById(R.id.txtDetails);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnViewComment = (Button) view.findViewById(R.id.btnViewComment);
        btnViewComment.setOnClickListener(this);
        hideKeyboard(view);
        return view;
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentTouched) {
            listener = (OnFragmentTouched) activity;
        }
    }*/

   /* public Animator prepareUnrevealAnimator(float cx, float cy) {
        int radius = getEnclosingCircleRadius(getView(), (int) cx, (int) cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) cx, (int) cy, radius, 0);
        anim.setInterpolator(new AccelerateInterpolator(2f));
        anim.setDuration(1000);
        return anim;
    }*/

  /*  private int getEnclosingCircleRadius(View v, int cx, int cy) {
        int realCenterX = cx + v.getLeft();
        int realCenterY = cy + v.getTop();
        int distanceTopLeft = (int) Math.hypot(realCenterX - v.getLeft(), realCenterY - v.getTop());
        int distanceTopRight = (int) Math.hypot(v.getRight() - realCenterX, realCenterY - v.getTop());
        int distanceBottomLeft = (int) Math.hypot(realCenterX - v.getLeft(), v.getBottom() - realCenterY);
        int distanceBottomRight = (int) Math.hypot(v.getRight() - realCenterX, v.getBottom() - realCenterY);

        Integer[] distances = new Integer[]{distanceTopLeft, distanceTopRight, distanceBottomLeft,
                distanceBottomRight};

        return Collections.max(Arrays.asList(distances));
    }*/
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
        getActivity().setTitle(getActivity().getResources().getString(call));
        if (lead.getServerLeadId() == 0) {
            btnViewComment.setVisibility(View.GONE);
        } else {
            btnViewComment.setVisibility(View.VISIBLE);
        }

        if (editMode) {
            txtCustomerName.setText(lead.getName());
            txtPhone.setText(lead.getPhone());
            txtAddress.setText(lead.getAddress());
            txtDetails.setText(lead.getDetails());
            txtInfo.setText(lead.getInfo());
            countComment = leadCommentDB.getCount(lead.getServerLeadId());
            btnViewComment.setText(
                    context.getResources().getString(R.string.view_comments) + " (" + countComment + ")");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {

            if (txtCustomerName.getText().toString().equals("")) {
               // Toast.info(context, context.getResources().getString(R.string.add_customer_details),Toast.LENGTH_SHORT, true).show();
                Toast.makeText(context, R.string.add_customer_details, Toast.LENGTH_SHORT).show();
            } else {
                lead.setName(txtCustomerName.getText().toString());
                lead.setPhone(txtPhone.getText().toString());
                lead.setAddress(txtAddress.getText().toString());
                lead.setDetails(txtDetails.getText().toString());
                lead.setInfo(txtInfo.getText().toString());
                String today = formatter.format(new Date());
                lead.setDate(today);
                if (editMode) {
                    lead.setServerLeadId(0);
                    leadDB.update(lead);
                 //   Toasty.success(context, context.getResources().getString(R.string.updated1), Toast.LENGTH_SHORT, true).show();

                    Toast.makeText(context, context.getResources().getString(R.string.updated1), Toast.LENGTH_SHORT).show();
                } else {
                    leadId = (int) leadDB.insert(lead);
                    lead.setId(leadId);
                    editMode = true;
                   // Toasty.success(context, context.getResources().getString(R.string.inserted), Toast.LENGTH_SHORT, true).show();
                  Toast.makeText(context, context.getResources().getString(R.string.inserted), Toast.LENGTH_SHORT).show();
                }
                //  saveToServer();
                sync();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fragment fragment = new HomeFragment();
                        // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
                           FragmentManager fragmentManager =
                                getActivity().getSupportFragmentManager();
                           FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        /*String backStateName = fragment.getClass().getName();
                        ft.addToBackStack(backStateName);
                        ft.detach(fragment);
                        ft.attach(fragment);*/
                        ft.commit();

                    }
                }, 600);

            }
        }

        if (view.getId() == R.id.btnViewComment) {
            //  Toast.makeText(context, R.string.submit_before_comments, Toast.LENGTH_SHORT).show();
            btnViewComment.setVisibility(View.VISIBLE);
            Fragment fragment = LeadCommentFragment.newInstance(lead.getServerLeadId());
            // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
               FragmentManager fragmentManager =
                    getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            /*String backStateName = fragment.getClass().getName();
            ft.addToBackStack(backStateName);
            ft.detach(fragment);
            ft.attach(fragment);*/
            ft.commit();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void sync() {
        try {
            leadDB = new LeadDB(context);
        } catch (Exception e) {
            leadDB = new LeadDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        leads = leadDB.getUnsentData();
        if(leads.size() > 0) {
            for(int i = 0; i < leads.size(); i++) {
                lead = leads.get(i);
                saveToServer(lead.getId());
            }
        }
    }


    public int syncCount() {

        try {
            leadDB = new LeadDB(context);
        } catch (Exception e) {

            leadDB = new LeadDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        leads = leadDB.getUnsentData();
       
        return leads.size();
    }

    public void saveToServer(final int leadId) {
        //   ((SalesRepActivity) context).showProgress(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_LEAD,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.i(TAG, s);
                                                                try {
                                                                    JSONObject object = new JSONObject(s);
                                                                    String status = object.get("status").toString();

                                                                    if (status.equals("success")) {

                                                                        lead = leadDB.getLeadById(leadId);
                                                                        lead.setServerLeadId(object.getInt("lead_id"));
                                                                        leadDB.update(lead);
                                                                    }
                                                                } catch (JSONException e) {
                                                                    Log.i(TAG, e.getMessage());
                                                                } catch (NullPointerException e) {
                                                                    Log.i(TAG, e.getMessage());
                                                                }
                                                                //                        ((SalesRepActivity) context).showProgress(false);

                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                try {
                                                                    ((SalesRepActivity) context).showProgress(false);
                                                                } catch (ClassCastException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();
                lead = leadDB.getLeadById(leadId);

                params.put("id", "" + lead.getServerLeadId());
                params.put("user_id", Integer.toString(userId));
                params.put("customer_name", "" + lead.getName());
                params.put("phone", "" + lead.getPhone());
                params.put("address", "" + lead.getAddress());
                params.put("customer_details", lead.getDetails());
                params.put("customer_info", lead.getInfo());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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
