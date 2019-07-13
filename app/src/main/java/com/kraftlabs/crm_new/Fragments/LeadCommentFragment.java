package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.kraftlabs.crm_new.Adapters.LeadCommentAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.LeadCommentDB;
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
 * Created by ashik on 14/7/17.
 */

public class LeadCommentFragment extends Fragment {
    private static final String TAG = "LeadCommentFragment";
    private static final String LEAD_ID = "LEAD_ID";
    boolean mFirstLoad;
    private Comment leadComment;
    private int leadId = 0;
    private Context context;
    private LeadCommentDB leadCommentDB;
    private LeadCommentAdapter leadCommentAdapter;
    private RecyclerView rvChat;
    private ArrayList<Comment> leadComments;
    private TextView txtEmptyComment, edtMessageTwo, edtMessage;
    private Button btnSend;
    private CheckBox chkDone;
    private TextView txtDoneDate;
    private Spinner spnPercentCompleat;

    public LeadCommentFragment() {
    }

    @SuppressLint("ValidFragment")
    public LeadCommentFragment(Context context) {
        this.context = context;
    }

    public static LeadCommentFragment newInstance(int leadId) {
        LeadCommentFragment fragment = new LeadCommentFragment();
        Bundle args = new Bundle();
        args.putInt(LEAD_ID, leadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        leadCommentDB = new LeadCommentDB(context);
        leadId = getArguments().getInt(LEAD_ID);
        Log.i(TAG, "LeadId: " + leadId);
        leadComment = new Comment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        rvChat = (RecyclerView) view.findViewById(R.id.rvChat);
        //txtEmptyComment
        edtMessage = (TextView) view.findViewById(R.id.edtMessage);

        edtMessageTwo = (TextView) view.findViewById(R.id.edtMessageTwo);
        chkDone = (CheckBox) view.findViewById(R.id.chkDone);
        txtDoneDate = (TextView) view.findViewById(R.id.txtDoneDate);
        spnPercentCompleat = (Spinner) view.findViewById(R.id.spnPercentCompleat);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = edtMessageTwo.getText().toString().trim();

                if (edtMessageTwo.getText().toString().equals("")) {
                    //                    Toast.makeText(context, context.getResources().getString(R.string.type_message), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, R.string.type_message,
                                Toast.LENGTH_SHORT).show();
                } else if (comment.matches("")) {
                    Toast.makeText(context, R.string.empty_are_not_allowed,
                                   Toast.LENGTH_SHORT).show();
                    //                    Toast.makeText(context, context.getResources().getString(R.string.empty_are_not_allowed), Toast.LENGTH_SHORT).show();
                } else {
                    sendComment();
                    //sendToServer();
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
        leadComments = new ArrayList<>();
        leadComment.setCreatedUserId(userId);
        leadComment.setComment(comment);
        leadComment.setLeadId(leadId);
        leadComment.setDate(today);

        leadCommentDB.insert(leadComment);
        leadComments = leadCommentDB.getCommentsById(leadId);
        leadCommentAdapter =
                new LeadCommentAdapter(context, leadComment.getCreatedUserId(), leadComments);
        rvChat.setAdapter(leadCommentAdapter);
        leadCommentAdapter.notifyDataSetChanged();
        edtMessageTwo.setText("");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.comment));
        chkDone.setVisibility(View.GONE);
        txtDoneDate.setVisibility(View.GONE);
        spnPercentCompleat.setVisibility(View.GONE);
        edtMessageTwo.setVisibility(View.VISIBLE);
        edtMessage.setVisibility(View.GONE);
        leadCommentDB = new LeadCommentDB(context);
        leadComments = leadCommentDB.getCommentsById(leadId);
        leadCommentAdapter =
                new LeadCommentAdapter(context, leadComment.getCreatedUserId(), leadComments);
        rvChat.setAdapter(leadCommentAdapter);
        if (leadComments.size() == 0) {

        }
        rvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert in != null;
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public int syncCount() {

        try {
            leadCommentDB = new LeadCommentDB(context);
        } catch (NullPointerException e) {
            leadCommentDB = new LeadCommentDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }

        leadComments = leadCommentDB.getUnsendData();

        return leadComments.size();
    }

    public void sync() {
        try {
            leadCommentDB = new LeadCommentDB(context);
        } catch (NullPointerException e) {
            leadCommentDB = new LeadCommentDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        leadComments = leadCommentDB.getUnsendData();
        if(leadComments.size() > 0) {
            for(int i = 0; i < leadComments.size(); i++) {
                leadComment = leadComments.get(i);
                sendToServer(leadComment.getId());
            }
        }
    }

    public void sendToServer(final int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_LEAD_COMMENT,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.i(TAG, s);

                                                                JSONObject object = null;
                                                                try {
                                                                    object = new JSONObject(s);
                                                                    String status = object.get("status").toString();
                                                                    if (status.equals("success")) {

                                                                        leadComment = leadCommentDB.getLeadCommentById(id);

                                /*callComment.setCustomerId(object.getInt("customerId"));
                                callComment.setCreatedUserId(object.getInt("userId"));
                                callComment.setComment(object.getString("comment"));
                                callComment.setDate(object.getString("date"));*/
                                                                        leadComment.setServerId(object.getInt("lead_comment_id"));
                                                                        leadCommentDB.update(leadComment);
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                int userId = PrefUtils.getCurrentUser(context).getUserId();

                leadComment = leadCommentDB.getLeadCommentById(id);

                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id", "" + leadComment.getCreatedUserId());
                params.put("lead_id", "" + leadComment.getLeadId());
                params.put("comment", "" + leadComment.getComment());
                params.put("date", "" + leadComment.getDate());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
