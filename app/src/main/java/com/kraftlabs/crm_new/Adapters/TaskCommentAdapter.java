package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Util.PrefUtils;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Models.Comment;
import com.kraftlabs.crm_new.Db.TaskCommentDB;

import java.util.ArrayList;

/**
 * Created by ashik on 18/7/17.
 */

public class TaskCommentAdapter extends RecyclerView.Adapter<TaskCommentAdapter.MyViewHolder> {
    private static final String TAG = "TaskCommentAdapter";
    int taskId;
    private Context context;
    private ArrayList<Comment> taskComments = new ArrayList<>();
    private TaskCommentDB taskCommentDB;

    public TaskCommentAdapter(Context context, int taskId, ArrayList<Comment> taskComments) {
        this.taskId = taskId;
        this.context = context;
        this.taskComments = taskComments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment taskComment = taskComments.get(position);
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        holder.txtCreatedUser.setText(taskComment.getCreatedBy());//user name
        holder.txtComment.setText(taskComment.getComment());
        holder.txtDate.setText(taskComment.getDate().toString());

        if (userId != taskComment.getCreatedUserId()) {//from Server
            holder.ivProfileMe.setVisibility(View.GONE);
            holder.ivProfileOther.setVisibility(View.VISIBLE);
            holder.imgSended.setVisibility(View.GONE);

            holder.txtComment.setGravity(Gravity.LEFT);
            holder.rlStatus.setGravity(Gravity.LEFT);
            holder.txtTaskPercentage.setVisibility(View.GONE);


        } else {
            holder.ivProfileMe.setVisibility(View.VISIBLE);//from app
            holder.ivProfileOther.setVisibility(View.GONE);
            holder.rltvText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.txtTaskPercentage.setVisibility(View.VISIBLE);

           /* if(holder.txtTaskPercentage.getText().equals(0)){
                taskComment.setPercentageOfCompleation(0);

            }*/
            holder.txtTaskPercentage.setText(String.valueOf(taskComment.getPercentageOfCompleation()) + "% compleated");
            if (taskComment.getServerId() == 0) {
                holder.imgSended.setVisibility(View.GONE);

            } else {
                holder.imgSended.setVisibility(View.VISIBLE);


            }
        }


    }

    @Override
    public int getItemCount() {
        return taskComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtComment, txtCreatedUser, txtDate, txtStatus, txtTaskPercentage;
        public RelativeLayout rltvText, rlStatus;
        public ImageView ivProfileMe, ivProfileOther, imgSended;

        public MyViewHolder(View view) {
            super(view);
            txtComment = (TextView) view.findViewById(R.id.txtComment);
            txtCreatedUser = (TextView) view.findViewById(R.id.txtCreatedUser);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtTaskPercentage = (TextView) view.findViewById(R.id.txtTaskPercentage);
            ivProfileMe = (ImageView) view.findViewById(R.id.ivProfileMe);
            ivProfileOther = (ImageView) view.findViewById(R.id.ivProfileOther);
            imgSended = (ImageView) view.findViewById(R.id.imgSended);

            rlStatus = (RelativeLayout) view.findViewById(R.id.rlStatus);
            rltvText = (RelativeLayout) view.findViewById(R.id.rltvText);
        }
    }
}
