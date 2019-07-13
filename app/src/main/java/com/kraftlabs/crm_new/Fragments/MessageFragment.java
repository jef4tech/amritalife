package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.MessageAdapter;
import com.kraftlabs.crm_new.Db.MessageDB;
import com.kraftlabs.crm_new.Models.Message;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MESSAGE_ASSIGN_ID = "message__assign_id";
    private Context context;
    private MessageDB messageDB;
    private TextView txtMessageEmpty;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int messageAssignId;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String param1, String param2, int messageAssignId) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(MESSAGE_ASSIGN_ID, messageAssignId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            messageAssignId = getArguments().getInt(MESSAGE_ASSIGN_ID);
            context = getActivity();


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        txtMessageEmpty = (TextView) view.findViewById(R.id.txtMessageEmpty);
        messageDB = new MessageDB(context);


        ArrayList<Message> messages = messageDB.getMessages(0, 50);

        RecyclerView lstMessage = (RecyclerView) view.findViewById(R.id.lstMessage);
        MessageAdapter messageAdapter = new MessageAdapter(context, messages);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstMessage.setLayoutManager(mLayoutManager);
        lstMessage.setItemAnimator(new DefaultItemAnimator());
        lstMessage.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstMessage.setHasFixedSize(false);
        lstMessage.setItemViewCacheSize(20);
        lstMessage.setDrawingCacheEnabled(true);
        lstMessage.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstMessage.setAdapter(messageAdapter);
        lstMessage.invalidate();

        if (messages.size() == 0) {
            txtMessageEmpty.setVisibility(View.VISIBLE);
        } else {
            txtMessageEmpty.setVisibility(View.GONE);
        }

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
        getActivity().setTitle(getActivity().getResources().getString(R.string.messages));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
