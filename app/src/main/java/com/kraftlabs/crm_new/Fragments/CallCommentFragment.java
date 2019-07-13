package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Adapters.CallCommentAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Models.Comment;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;




/**
 * Created by ashik on 10/7/17.
 */

public class CallCommentFragment extends Fragment {

  private static final String TAG = "CallCommentFragment ";
  private static final String CUSTOMER_ID = "CUSTOMER_ID";
  private static Runnable runnable = null;
  private Comment callComment;
  private int customer_id = 0;
  private Context context;
  private CallCommentDB callCommentDB;
  private CallCommentAdapter callCommentAdapter;
  private RecyclerView rvChat;
  private ArrayList<Comment> callComments;
  private TextView txtEmptyComment, edtMessageTwo, edtMessage;
  private Button btnSend;
  private CheckBox chkDone;
  private TextView txtDoneDate;
  private Spinner spnPercentCompleat;
  private Handler handler = null;
  private ArrayList<Comment> comments;

  public CallCommentFragment() {
  }

  @SuppressLint("ValidFragment")
  public CallCommentFragment(Context context) {

    this.context = context;
  }

  public static CallCommentFragment newInstance(int customer_id) {
    CallCommentFragment fragment = new CallCommentFragment();
    Bundle args = new Bundle();
    args.putInt(CUSTOMER_ID, customer_id);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity();
    callCommentDB = new CallCommentDB(context);
    customer_id = getArguments().getInt(CUSTOMER_ID);
    Log.i(TAG, "CustomerId: " + customer_id);
    callComment = new Comment();
    handler = new Handler();
    runnable = new Runnable() {
      public void run() {

      }
    };
    handler.postDelayed(runnable, 1000);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_comment, container, false);
    rvChat = (RecyclerView) view.findViewById(R.id.rvChat);
    //txtEmptyComment

    edtMessageTwo = (TextView) view.findViewById(R.id.edtMessageTwo);
    edtMessage = (TextView) view.findViewById(R.id.edtMessage);
    chkDone = (CheckBox) view.findViewById(R.id.chkDone);
    txtDoneDate = (TextView) view.findViewById(R.id.txtDoneDate);
    spnPercentCompleat = (Spinner) view.findViewById(R.id.spnPercentCompleat);
    btnSend = (Button) view.findViewById(R.id.btnSend);
    btnSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String comment = edtMessageTwo.getText().toString().trim();

        if (edtMessageTwo.getText().toString().equals("")) {
          Toast.makeText(context, getString(R.string.type_message),
              Toast.LENGTH_SHORT).show();
        } else if (comment.matches("")) {
          Toast.makeText(context,
            R.string.empty_space_not_allowed,
              Toast.LENGTH_SHORT).show();
        } else {

          sendComment();
          sync();
        }
      }
    });

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

    rvChat.setLayoutManager(mLayoutManager);
    rvChat.setItemAnimator(new DefaultItemAnimator());

    rvChat.addItemDecoration(
        new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    rvChat.setHasFixedSize(false);
    rvChat.setItemViewCacheSize(20);
    rvChat.setDrawingCacheEnabled(true);
    rvChat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    // rvChat.smoothScrollToPosition(position);
    rvChat.invalidate();
      hideKeyboard(view);
    return view;
  }

  private void sendComment() {
    String comment = edtMessageTwo.getText().toString().trim();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int userId = PrefUtils.getCurrentUser(context).getUserId();
    String userName = PrefUtils.getCurrentUser(context).getName();
    Log.i(TAG, "sendComment: " + userName);
    String today = sdf.format(new Date());
    callComments = new ArrayList<>();
    callComment.setCreatedUserId(userId);//created by
    callComment.setComment(comment);
    callComment.setCustomerId(customer_id);
    callComment.setDate(today);

    callCommentDB.insert(callComment);
    callComments = callCommentDB.getCommentByCustomer(customer_id);
    callCommentAdapter =
        new CallCommentAdapter(context, callComment.getCreatedUserId(), callComments);
    rvChat.setAdapter(callCommentAdapter);
    callCommentAdapter.notifyDataSetChanged();
    edtMessageTwo.setText("");
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void onResume() {
    super.onResume();

       /* callComments=callCommentDB.getUnsentData();*/


       /* if (callComments.size() > 0) {
            for (int i = 0; i < callComments.size(); i++) {
                callComment = callComments.get(i);
                sendToServer();
            }
        }*/

    chkDone.setVisibility(View.GONE);
    txtDoneDate.setVisibility(View.GONE);
    spnPercentCompleat.setVisibility(View.GONE);
    edtMessageTwo.setVisibility(View.VISIBLE);
    edtMessage.setVisibility(View.GONE);

    int userId = PrefUtils.getCurrentUser(context).getUserId();
    getActivity().setTitle(getActivity().getResources().getString(R.string.comment));

    callComments = callCommentDB.getCommentByCustomer(customer_id);
    callCommentAdapter =
        new CallCommentAdapter(context, callComment.getCreatedUserId(), callComments);
    rvChat.setAdapter(callCommentAdapter);
    //callComment.setStatus("sending...");
    if (callComments.size() == 0) {

    }
    rvChat.setOnTouchListener((view, motionEvent) -> {
      hideKeyboard(view);
      return false;
    });
  }

  protected void hideKeyboard(View view) {
    InputMethodManager in =
        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    assert in != null;
    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public void sync() {
    try {
      callCommentDB = new CallCommentDB(context);
    } catch (NullPointerException e) {
      callCommentDB = new CallCommentDB(getActivity().getApplicationContext());
      context = getActivity().getApplicationContext();
    }
    callComments = callCommentDB.getUnsentData();
    if(callComments.size() > 0) {
      for(int i = 0; i < callComments.size(); i++) {
        callComment = callComments.get(i);
        sendToServer(callComment.getId());
      }
    }
  }
  public int syncCount() {
    try {
      callCommentDB = new CallCommentDB(context);
    } catch (NullPointerException e) {
      callCommentDB = new CallCommentDB(getActivity().getApplicationContext());
      context = getActivity().getApplicationContext();
    }
    callComments = callCommentDB.getUnsentData();
    return callComments.size();
  }

  public void sendToServer(final int id) {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_CALL_COMMENT,
            s -> {
              Log.i(TAG, s);

              JSONObject object = null;
              try {
                object = new JSONObject(s);
                String status = object.get("status").toString();
                if (status.equals("success")) {
                  callComment = callCommentDB.getCommentById(id);
                                  /*callComment.setCustomerId(object.getInt("customerId"));
                                  callComment.setCreatedUserId(object.getInt("userId"));
                                  callComment.setComment(object.getString("comment"));
                                  callComment.setDate(object.getString("date"));*/
                  callComment.setServerId(object.getInt("call_comment_id"));
                  callCommentDB.update(callComment);
                }
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {
      protected Map<String, String> getParams() throws AuthFailureError {
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        Map<String, String> params = new Hashtable<String, String>();
        params.put("user_id", "" + callComment.getCreatedUserId());
        // params.put("call_id", "" + callComment.getId());
        callComment = callCommentDB.getCommentById(id);

        params.put("customer_id", "" + callComment.getCustomerId());
        params.put("comment", "" + callComment.getComment());
        params.put("date", "" + callComment.getDate());
        return params;
      }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
  }

  /*  public void sendToServer(final int index) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NotifConfig.SAVE_CALL_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, s);

                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            String status = object.get("status").toString();
                            if (status.equals("success")) {
                                *//*callComment.setCustomerId(object.getInt("customerId"));
                                callComment.setCreatedUserId(object.getInt("userId"));
                                callComment.setComment(object.getString("comment"));
                                callComment.setDate(object.getString("date"));*//*
                                callComment.setServerId(object.getInt("call_comment_id"));
                                callCommentDB.update(callComment);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id", "" + callComments.get(index).getCreatedUserId());
                // params.put("call_id", "" + callComment.getId());

                params.put("customer_id", "" + callComments.get(index).getCustomerId());
                params.put("comment", "" + callComments.get(index).getComment());
                params.put("date", "" + callComments.get(index).getDate());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
*/
  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}
