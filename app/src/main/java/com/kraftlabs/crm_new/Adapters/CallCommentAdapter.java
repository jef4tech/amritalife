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
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Models.Comment;

import java.util.ArrayList;

/**
 * Created by ashik on 8/7/17.
 */

public class CallCommentAdapter extends RecyclerView.Adapter<CallCommentAdapter.MyViewHolder> {
    private static final String TAG = "CallCommentAdapter";
    int mUserId;
    private Context context;
    private ArrayList<Comment> callComments = new ArrayList<>();
    private CallCommentDB callCommentDB;

    public CallCommentAdapter(Context context, int mUserId, ArrayList<Comment> callComments) {
        this.context = context;
        this.mUserId = mUserId;
        this.callComments = callComments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(iteView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment callComment = callComments.get(position);
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        holder.txtTaskPercentage.setVisibility(View.GONE);
        holder.txtCreatedUser.setText(callComment.getCreatedBy());
        holder.txtComment.setText(callComment.getComment());
        holder.txtDate.setText(callComment.getDate().toString());


        if (userId != callComment.getCreatedUserId()) {//from Server
            holder.ivProfileMe.setVisibility(View.GONE);
            holder.ivProfileOther.setVisibility(View.VISIBLE);
            holder.imgSended.setVisibility(View.GONE);

            holder.txtComment.setGravity(Gravity.LEFT);
            holder.rlStatus.setGravity(Gravity.LEFT);


        } else {
            holder.ivProfileMe.setVisibility(View.VISIBLE);//from app
            holder.ivProfileOther.setVisibility(View.GONE);
            holder.rltvText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            if (callComment.getServerId() == 0) {
                holder.imgSended.setVisibility(View.GONE);

            } else {
                holder.imgSended.setVisibility(View.VISIBLE);


            }
        }


    }

    @Override
    public int getItemCount() {
        return callComments.size();
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
            ivProfileMe = (ImageView) view.findViewById(R.id.ivProfileMe);
            ivProfileOther = (ImageView) view.findViewById(R.id.ivProfileOther);
            txtTaskPercentage = (TextView) view.findViewById(R.id.txtTaskPercentage);
            imgSended = (ImageView) view.findViewById(R.id.imgSended);

            rlStatus = (RelativeLayout) view.findViewById(R.id.rlStatus);
            rltvText = (RelativeLayout) view.findViewById(R.id.rltvText);
        }
    }
}
