package com.kraftlabs.crm_new.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.TaskAdapter;
import com.kraftlabs.crm_new.Db.TaskDB;
import com.kraftlabs.crm_new.Models.Task;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskFragment extends Fragment {

    private static final String TAG = "TaskFragment";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    private TaskDB taskDB;
    private TextView txtTaskEmpty;
    private OnFragmentInteractionListener mListener;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        txtTaskEmpty = (TextView) view.findViewById(R.id.txtTaskEmpty);
        taskDB = new TaskDB(context);
        ArrayList<Task> tasks = taskDB.getTasks("", false, 0, 50);

        RecyclerView lstChallenge = (RecyclerView) view.findViewById(R.id.lstTask);
        TaskAdapter taskAdapter = new TaskAdapter(context, tasks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        lstChallenge.setLayoutManager(mLayoutManager);
        lstChallenge.setItemAnimator(new DefaultItemAnimator());
        lstChallenge.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lstChallenge.setHasFixedSize(false);
        lstChallenge.setItemViewCacheSize(20);
        lstChallenge.setDrawingCacheEnabled(true);
        lstChallenge.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstChallenge.setAdapter(taskAdapter);
        lstChallenge.invalidate();
        for (Task mTask : tasks) {
            if (!mTask.isRead()) {
                mTask.setIsRead(1);
                mTask.setReadDate(formatter.format(new Date()));
                taskDB.update(mTask, false, false);
            }
        }

        if (tasks.size() == 0) {
            txtTaskEmpty.setVisibility(View.VISIBLE);
        } else {
            txtTaskEmpty.setVisibility(View.GONE);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View mProgressView = getActivity().findViewById(R.id.login_progress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.tasks));
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
