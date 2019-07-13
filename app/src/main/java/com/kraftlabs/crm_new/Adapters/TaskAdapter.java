package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.kraftlabs.crm_new.Db.TaskDB;
import com.kraftlabs.crm_new.Models.Task;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.ServiceManager;
import com.kraftlabs.crm_new.Util.PrefUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ajith on 29/7/16.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

  private final String TAG = "TASK ADAPTER";
  private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private Context context;
  private ArrayList<Task> tasks = new ArrayList<>();
  private TaskDB taskDB;

  private int m_interval = 5000; // 5 seconds by default, can be changed later
  private Handler m_handler = new Handler();

  public TaskAdapter(Context context) {
    this.context = context;
  }

  public TaskAdapter(Context context, ArrayList<Task> tasks) {
    this.context = context;
    this.tasks = tasks;
    taskDB = new TaskDB(context);
  }

  public void update(ArrayList<Task> tasks) {
    this.tasks.clear();
    this.tasks = tasks;
    notifyDataSetChanged();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_task, parent, false);
    return new MyViewHolder(itemView);
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {

    final Task task = tasks.get(position);
    holder.title.setText(task.getTitle());
    holder.description.setText(task.getDescription());

    if (task.isDone()) {
      holder.time.setText(task.getDoneDate());
    } else {
      holder.time.setText("");
    }
    holder.createdUserName.setText(task.getCreatedUserName());

    if (task.isDone()) {
      holder.checkBox.setChecked(true);
    } else {
      holder.checkBox.setChecked(false);
    }

    try {
      Picasso.with(context)
          .load(task.getPhoto())
          .fit()
          .placeholder(R.mipmap.no_user_profile)
          .into(holder.imgProfile);
    } catch (IllegalArgumentException e) {
      Log.i(TAG, task.getDescription());
    }

    holder.checkBox.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {


              if (!task.isDone()) {
                  task.setIsDone(1);
                  task.setDoneDate(formatter.format(new Date()));
              } else {
                  task.setIsDone(0);
                  task.setDoneDate("");
              }
              setTaskStatus(task);
              notifyDataSetChanged();
        /*sync(task);*/
      }
    });
    holder.rlTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

                /*Fragment fragment = TaskCommentFragment.newInstance(task.getTaskId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();*/

      }
    });
  }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }
  @Override
  public long getItemId(int position) {
    return position;
  }

  /*public int sync(Task task) {
    try {
      taskDB = new TaskDB(context);
    } catch (Exception e) {
      Log.i(TAG, "sync: ========>Context null");
    }
    if (context != null) {
      tasks = taskDB.getUnsentData();
      if (tasks.size() > 0) {
        for (int i = 0; i < tasks.size(); i++) {
          task = tasks.get(i);
          //setTaskStatus(task);
        }
      }
    }
    return tasks.size();
    //   tasks=taskDB.getUnsentData();

  }*/

  public void setTaskStatus(final Task task) {//send To server
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SET_TASK_STATUS,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String s) {
            Log.i(TAG, s);
            try {
              JSONObject object = new JSONObject(s);
              String status = object.get("status").toString();
              if (status.equals("success")) {

                taskDB.update(task, true, true);
              } else {
                taskDB.update(task, true, false);
              }
            } catch (JSONException e) {
              Log.i(TAG, context.getResources().getString(R.string.server_error) + e);
            } catch (NullPointerException e) {
              Log.i(TAG, context.getResources().getString(R.string.server_error) + e);
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {
            taskDB.update(task, true, false);
            Toast.makeText(context, R.string.network_error_message,
                Toast.LENGTH_SHORT).show();

            //Toast.makeText(context, context.getResources().getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();
            Log.i(TAG, context.getString(R.string.network_error)
                + volleyError.toString()
                + volleyError.networkResponse
                + volleyError.fillInStackTrace());
          }
        }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        TaskDB taskDB = new TaskDB(context);
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        Map<String, String> params = new Hashtable<String, String>();
        try {

          params.put("task_id", Integer.toString(task.getTaskId()));
          params.put("is_read", Integer.toString(task.getIsRead()));
          params.put("read_date", task.getReadDate());
          params.put("is_done", Integer.toString(task.getIsDone()));
          params.put("done_date", task.getDoneDate());
          params.put("user_id", Integer.toString(userId));
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

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView createdUserName, time, title, description;
    public ImageView imgProfile;
    public CheckBox checkBox;
    public RelativeLayout rlTask;

    public MyViewHolder(View view) {
      super(view);
      createdUserName = (TextView) view.findViewById(R.id.txtCreatedUser);
      time = (TextView) view.findViewById(R.id.txtTime);
      description = (TextView) view.findViewById(R.id.txtDescription);
      title = (TextView) view.findViewById(R.id.txtTitle);
      imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
      rlTask = (RelativeLayout) view.findViewById(R.id.rlTask);
      checkBox = (CheckBox) view.findViewById(R.id.chkDone);
    }
  }
}
