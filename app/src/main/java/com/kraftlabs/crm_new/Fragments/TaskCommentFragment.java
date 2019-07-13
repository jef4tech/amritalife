package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.CompoundButton;
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
import com.kraftlabs.crm_new.Adapters.TaskCommentAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.TaskCommentDB;
import com.kraftlabs.crm_new.Models.Comment;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class TaskCommentFragment extends Fragment {

    private static final String TAG = "TaskCommentFragment";
    private static final String TASK_ID = "TASK_ID";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Comment taskComment;
    private int task_id = 0;
    private Context context;
    private TaskCommentDB taskCommentDB;
    private TaskCommentAdapter taskCommentAdapter;
    private RecyclerView rvChat;
    private ArrayList<Comment> taskComments;
    private TextView txtEmptyComment, edtMessage, edtMessageTwo;
    private Button btnSend;
    private CheckBox chkDone;
    private TextView txtDoneDate;
    private Spinner spnPercentCompleat;

    public TaskCommentFragment() {
    }

    public static TaskCommentFragment newInstance(int taskId) {
        TaskCommentFragment fragment = new TaskCommentFragment();
        Bundle args = new Bundle();
        args.putInt(TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        taskComment = new Comment();
        taskCommentDB = new TaskCommentDB(context);
        Log.i(TAG, "onCreate: Context=" + context);
        task_id = getArguments().getInt(TASK_ID);
        Log.i(TAG, "onCreate: TaskId" + task_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        rvChat = (RecyclerView) view.findViewById(R.id.rvChat);
        edtMessageTwo = (TextView) view.findViewById(R.id.edtMessageTwo);
        edtMessage = (TextView) view.findViewById(R.id.edtMessage);
        chkDone = (CheckBox) view.findViewById(R.id.chkDone);
        txtDoneDate = (TextView) view.findViewById(R.id.txtDoneDate);
        spnPercentCompleat = (Spinner) view.findViewById(R.id.spnPercentCompleat);

        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spnPercentCompleat.getSelectedItem().toString().equals("100")) {
                    chkDone.setChecked(true);
                }
                sendComment();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvChat.setLayoutManager(mLayoutManager);
        rvChat.setItemAnimator(new DefaultItemAnimator());

        rvChat.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rvChat.setHasFixedSize(false);
        rvChat.setItemViewCacheSize(20);
        rvChat.setDrawingCacheEnabled(true);
        // rvChat.getLayoutManager().scrollToPosition(taskCommentAdapter.);
        rvChat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // rvChat.smoothScrollToPosition(position);
        rvChat.invalidate();
        hideKeyboard(view);
        return view;
    }

    private void sendComment() {
        String comment = edtMessage.getText().toString().trim();

        if (edtMessage.getText().toString().equals("")) {
            //Toasty.info(context, context.getResources().getString(R.string.type_message), Toast.LENGTH_SHORT, true).show();
            Toast.makeText(context,context.getResources().getString(R.string.type_message) , Toast.LENGTH_SHORT).show();
        } else if (comment.matches("")) {
          //  Toasty.warning(context, context.getResources().getString(R.string.empty_are_not_allowed), Toast.LENGTH_SHORT, true).show();
            Toast.makeText(context, context.getResources().getString(R.string.empty_are_not_allowed), Toast.LENGTH_SHORT).show();
        } else {

            //   spnPercentCompleat.setPopupBackgroundDrawable();
            String value = spnPercentCompleat.getSelectedItem().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HH:mm:ss)");
            int userId = PrefUtils.getCurrentUser(context).getUserId();
            String today = sdf.format(new Date());
            taskComments = new ArrayList<>();
            taskComment.setTaskId(task_id);
            taskComment.setCreatedUserId(userId);
            taskComment.setComment(comment);
            taskComment.setDate(today);
            taskComment.setCreatedBy(String.valueOf(userId));
            taskComment.setTaskDoneBy(String.valueOf(userId));
            taskComment.setDoneDate(taskComment.getDoneDate());

            if (value.equals("25") || value.equals("50") || value.equals("75") || value.equals("100")) {
                taskComment.setPercentageOfCompleation((value));
            } else {
                taskComment.setPercentageOfCompleation(("0"));
            }

            taskCommentDB.insert(taskComment);
            taskComments = taskCommentDB.getCommentByTask(task_id);
            taskCommentAdapter = new TaskCommentAdapter(context, task_id, taskComments);
            rvChat.setAdapter(taskCommentAdapter);
            taskCommentAdapter.notifyDataSetChanged();
            edtMessage.setText("");

            if (taskComment.isDone()) {
                txtDoneDate.setText(taskComment.getDoneDate());
            } else {
                txtDoneDate.setText("");
            }
            if (taskComment.isDone()) {
                chkDone.setChecked(true);
            } else {
                chkDone.setChecked(false);

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int percentage = 0;
        if (percentage == taskCommentDB.getCompleation()) {
            chkDone.setChecked(true);
        }
        if (taskComment.isDone()) {
            txtDoneDate.setText(taskComment.getDoneDate());
        } else {
            txtDoneDate.setText("");
        }
        if (taskComment.isDone()) {
            chkDone.setChecked(true);
        } else {
            chkDone.setChecked(false);

        }

        getActivity().setTitle(getActivity().getResources().getString(R.string.comment));
        chkDone.setVisibility(View.VISIBLE);
        txtDoneDate.setVisibility(View.VISIBLE);
        spnPercentCompleat.setVisibility(View.VISIBLE);
        edtMessage.setVisibility(View.VISIBLE);
        edtMessageTwo.setVisibility(View.GONE);

        //spnPercentCompleat.setSelection(taskComment.getPercentageOfCompleation());
        spnPercentCompleat.setPrompt(getString(R.string.select_percentage_of_compleation));

        taskComments = taskCommentDB.getCommentByTask(task_id);
        taskCommentAdapter = new TaskCommentAdapter(context, task_id, taskComments);
        String value = String.valueOf(spnPercentCompleat.getSelectedItem());

        taskComment.setPercentageOfCompleation(value);

        rvChat.setAdapter(taskCommentAdapter);
        rvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });


/*if(taskComment.getPercentageOfCompleation().equals("100")){
    taskComment.isDone();
    chkDone.setChecked(true);
}*/
        chkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int id = buttonView.getId();
                if (id == R.id.chkDone) {
                    if (chkDone.isChecked()) {


                       /* if (!taskComment.isDone()) {*/
                        taskComment.setIsDone(1);
                        taskComment.setDoneDate(formatter.format(new Date()));
                        txtDoneDate.setText(taskComment.getDoneDate());
                        txtDoneDate.setVisibility(View.VISIBLE);
                        chkDone.setText(R.string.done);
                        String comment = "Task compleated";
                        String value = spnPercentCompleat.getSelectedItem().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HH:mm:ss)");
                        int userId = PrefUtils.getCurrentUser(context).getUserId();
                        String today = sdf.format(new Date());
                        taskComments = new ArrayList<>();
                        taskComment.setTaskId(task_id);
                        taskComment.setCreatedUserId(userId);
                        taskComment.setComment(comment);
                        taskComment.setDate(today);
                        taskComment.setCreatedBy(String.valueOf(userId));
                        taskComment.setTaskDoneBy(String.valueOf(userId));
                        taskComment.setDoneDate(taskComment.getDoneDate());
                        taskComment.setPercentageOfCompleation("100");

                        taskCommentDB.insert(taskComment);
                        taskComments = taskCommentDB.getCommentByTask(task_id);
                        taskCommentAdapter = new TaskCommentAdapter(context, task_id, taskComments);
                        rvChat.setAdapter(taskCommentAdapter);
                        taskCommentAdapter.notifyDataSetChanged();
                        edtMessage.setText("");

                   /*     } else {
                            chkDone.setText("");
                            taskComment.setIsDone(0);
                            taskComment.setDoneDate("");
                            txtDoneDate.setVisibility(View.GONE);
                        }
*/

                    } else {


                      /*  if (taskComment.isDone()) {*/
                        taskComment.setIsDone(0);
                        taskComment.setDoneDate(formatter.format(new Date()));
                        txtDoneDate.setText(taskComment.getDoneDate() + "(re-opened)");
                        txtDoneDate.setVisibility(View.GONE);
                        chkDone.setText("");
                        String comment = getString(R.string.task_re_opened);
                        String value = spnPercentCompleat.getSelectedItem().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HH:mm:ss)");
                        int userId = PrefUtils.getCurrentUser(context).getUserId();
                        String today = sdf.format(new Date());
                        taskComments = new ArrayList<>();
                        taskComment.setTaskId(task_id);
                        taskComment.setCreatedUserId(userId);
                        taskComment.setComment(comment);
                        taskComment.setDate(today);
                        taskComment.setCreatedBy(String.valueOf(userId));
                        taskComment.setTaskDoneBy(String.valueOf(userId));
                        taskComment.setDoneDate(taskComment.getDoneDate());
                        taskComment.setPercentageOfCompleation("50");

                        taskCommentDB.insert(taskComment);
                        taskComments = taskCommentDB.getCommentByTask(task_id);
                        taskCommentAdapter = new TaskCommentAdapter(context, task_id, taskComments);
                        rvChat.setAdapter(taskCommentAdapter);
                        taskCommentAdapter.notifyDataSetChanged();
                        edtMessage.setText("");

                      /*  } else {*/
                            /*chkDone.setText("");
                            taskComment.setIsDone(1);
                            taskComment.setDoneDate("");
                            txtDoneDate.setVisibility(View.VISIBLE);*/
                        //     }

                    }
                }
            }
        });
       /* chkDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!taskComment.isDone()) {
                    taskComment.setIsDone(1);
                    taskComment.setDoneDate(formatter.format(new Date()));
                    txtDoneDate.setText(taskComment.getDoneDate());
                    txtDoneDate.setVisibility(View.VISIBLE);
                    chkDone.setText(R.string.done);
                    String comment = "Task compleated";
                    String value = spnPercentCompleat.getSelectedItem().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HH:mm:ss)");
                    int userId = PrefUtils.getCurrentUser(context).getUserId();
                    String today = sdf.format(new Date());
                    taskComments = new ArrayList<>();
                    taskComment.setTaskId(task_id);
                    taskComment.setCreatedUserId(userId);
                    taskComment.setComment(comment);
                    taskComment.setDate(today);
                    taskComment.setCreatedBy(String.valueOf(userId));
                    taskComment.setTaskDoneBy(String.valueOf(userId));
                    taskComment.setDoneDate(taskComment.getDoneDate());
                    taskComment.setPercentageOfCompleation("100");


                    taskCommentDB.insert(taskComment);
                    taskComments = taskCommentDB.getCommentByTask(task_id);
                    taskCommentAdapter = new TaskCommentAdapter(context, task_id, taskComments);
                    rvChat.setAdapter(taskCommentAdapter);
                    taskCommentAdapter.notifyDataSetChanged();
                    edtMessage.setText("");

                } else {
                    chkDone.setText("");
                    taskComment.setIsDone(0);
                    taskComment.setDoneDate("");
                    txtDoneDate.setVisibility(View.GONE);
                }

//TODO insert
//TODO Send to server
            }
        });*/

    }

    private void sendToServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_TASK_COMMENT,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.i(TAG, s);
                                                                try {
                                                                    JSONObject object = new JSONObject(s);
                                                                    String status = object.get("status").toString();
                                                                    if (status.equals("success")) {
                                                                        taskCommentDB.update(taskComment);
                                                                    } else {
                                                                        // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                } catch (JSONException e) {
                                                                    Log.i(TAG, context.getString(R.string.server_error) + e);
                                                                } catch (NullPointerException e) {
                                                                    Log.i(TAG, context.getString(R.string.server_error) + e);
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                       /* taskCommentDB.update(taskComment);*/
                                                               // Toasty.error(context, context.getResources().getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();

                                                                Toast.makeText(context, context.getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();
                                                                Log.i(TAG, context.getString(R.string.network_error) + volleyError.toString() + volleyError.networkResponse + volleyError.fillInStackTrace());
                                                            }
                                                        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                TaskCommentDB taskCommentDB = new TaskCommentDB(context);
                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();
                try {

                    params.put("task_id", Integer.toString(taskComment.getTaskId()));
                    params.put("user_id", Integer.toString(taskComment.getCreatedUserId()));
                    params.put("comment", taskComment.getComment());
                    params.put("date", taskComment.getDoneDate());
                    params.put("comment_created_by", String.valueOf(taskComment.getCreatedUserId()));
                    params.put("task_done_by", String.valueOf(taskComment.getCreatedUserId()));
                    params.put("task_done_date", taskComment.getDoneDate());
                    params.put("task_is_done", String.valueOf(taskComment.getIsDone()));

                    return params;
                } catch (Exception e) {
                    Log.i(TAG, "" + e);
                }
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
