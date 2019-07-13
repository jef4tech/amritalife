
package com.kraftlabs.crm_new.chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.MessageDB;
import com.kraftlabs.crm_new.Models.Message;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class ChatFragment extends Fragment {

    private static final String MESSAGE_ASSIGN_ID = "message_assign_id";
    private static final String PARENT_MESSAGE_ID = "parent_message_id";
    private static final String MESSAGE = "mMessage";
    private static final String TAG = "ChatFragment";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    MessageDB messageDB;
    private Button send;
    private OnFragmentInteractionListener mListener;
    private int mMessageAssignId;
    String mMessageContent;
    private int mParentMessageId;
    private String mMessageBody;
    private EditText sendingText;
    TextView txtReplayAssignId, txtMessageId, txtReplayTitle, txtCreatesUserId, txtDate, txtReplay,
            txtStatus, txtIsSynced, txtParentMessageId;
    private Message mMessage;
    private Boolean updateSyncStatus;
    private Boolean isSynced;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(int mMessage_assign_id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(MESSAGE_ASSIGN_ID, mMessage_assign_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mMessageAssignId = getArguments().getInt(MESSAGE_ASSIGN_ID);
            mParentMessageId = getArguments().getInt(PARENT_MESSAGE_ID);
            mMessageContent = getArguments().getString(MESSAGE);
            messageDB = new MessageDB(context);
            //  ArrayList<Message> messages = messageDB.getMessages(mMessageAssignId, mMessage);

            //   messageClass  = messages.get(0);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_chat_main_fragment, container, false);

        // txtReplayAssignId= (TextView) view.findViewById(R.id.txtReplay);
        //  txtMessageId=  (TextView) view.findViewById(R.id.txtReplay) ;
        txtReplayTitle = (TextView) view.findViewById(R.id.txtMessage);
        txtCreatesUserId = (TextView) view.findViewById(R.id.txtCreatedUser);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtReplay = (TextView) view.findViewById(R.id.message_right);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtIsSynced = (TextView) view.findViewById(R.id.txtSyncStatus);
        //txtParentMessageId= (TextView) view.findViewById(R.id.);

        messageDB = new MessageDB(context);
        ArrayList<Message> messages = messageDB.getCurrentMessages(mMessageAssignId, mParentMessageId);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.msgRecyleView);
        ChatAdapter chatAdapter = new ChatAdapter(context,messages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.invalidate();

        send = (Button) view.findViewById(R.id.sendMessageButton);

        sendingText = (EditText) view.findViewById(R.id.messageEditText);
        /*ChatAdapter chatAdapter= new ChatAdapter(context);*/


        sendingText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mMessageBody = sendingText.getText().toString();
                if (mMessageBody.isEmpty()) {
                    Toast.makeText(context, R.string.enter_message, Toast.LENGTH_SHORT).show();
                } else {
                    sendChatMessage();
                    saveReplay();
                }
            }
        });


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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.chat));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public void saveReplay() {
        mMessage.setMessageAssignId(mMessage.getMessageAssignId());
        //mMessage.setMessageId(txtMessageId.getComment().toString());
        mMessage.setMessageTitle(getString(R.string.replay));
        //mMessage.getCreatedUserId(txtCreatesUserId.getComment().())
        mMessage.setDate(txtDate.getText().toString());
        mMessage.setMessage(txtReplay.getText().toString());
        mMessage.setStatus(txtStatus.getText().toString());
        mMessage.setStatus(txtStatus.getText().toString());
        //txtIsSynced
        mMessage.setParentMessageId(mMessage.getMessageId());

        //txtReplayAssignId,txtMessageId,txtReplayTitle,txtCreatesUserId,txtDate,txtReplay,txtStatus,
        // txtIsSynced,txtParentMessageId;
        saveToServer();
    }

    public void saveToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, s);
                        try {
                            JSONObject object = new JSONObject(s);
                            String status = object.get("status").toString();

                            if (status.equals("success")) {
                                //        mMessage.setSyncStatus(1);
                                //       mMessage.getServerMessageId(object.getInt("message_id"));
                                messageDB.update(mMessage, updateSyncStatus, isSynced);

                            }

                        } catch (JSONException e) {
                            Log.i(TAG, e.getMessage());
                        } catch (NullPointerException e) {
                            Log.i(TAG, e.getMessage());
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();

                params.put("relay_assign_id", "" + mMessage.getMessageId());
                params.put("replay_id", "" + mMessage.getMessageId());
                params.put("replay_title",  mMessage.getMessageTitle());
                params.put("created_user_id", "" +mMessage.getCreatedUserId());
                params.put("date", mMessage.getDate());
                params.put("replay",txtReplay.getText().toString());
                params.put("status", mMessage.getStatus());
                //params.put("is_synced",mMessage)
                  params.put("parent_message_id",""+mMessage.getParentMessageId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private boolean sendChatMessage() {
        ArrayList<Message> messages = messageDB.getCurrentMessages(mMessageAssignId, mParentMessageId);

        ChatAdapter chatArrayAdapter = new ChatAdapter(context, messages);

        chatArrayAdapter.add(new ChatMessage(sendingText.getText().toString()));
        sendingText.setText("");

        // sideLeft = !sideLeft;
        //    sideRight=!sideRight;
        return true;
    }

}































   /* @Override
    public void onResume() {

        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.message_content));

 messageDB = new MessageDB(context);
        messages = messageDB.getMessages(mMessageAssignId, 0);
        chatArrayAdapter = new ChatAdapter(context, 0);
        listView.setAdapter(chatArrayAdapter);
        if (messages.size() == 0) {
            Toast.makeText(context, "Empty", Toast.LENGTH_LONG);
            messageDB.getMessages(mMessageAssignId, 0);
//            messageLeft.setComment(getMe);
        }

    }


    public void getCurrentMessages() {

        //  messageDb.getMessages(0, 50);

    }


}
*/
