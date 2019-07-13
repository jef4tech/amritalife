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
import com.kraftlabs.crm_new.Db.LeadCommentDB;

import java.util.ArrayList;

/**
 * Created by ashik on 14/7/17.
 */

public class LeadCommentAdapter extends RecyclerView.Adapter<LeadCommentAdapter.MyViewHolder> {
    private static final String TAG = "LeadCommentFragment";
    int mUserId;
    private Context context;
    private ArrayList<Comment> leadComments = new ArrayList<>();
    private LeadCommentDB leadCommentDB;

    public LeadCommentAdapter(Context context, int mUserId, ArrayList<Comment> leadComments) {
        this.context = context;
        this.mUserId = mUserId;
        this.leadComments = leadComments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new MyViewHolder(iteView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Comment leadComment = leadComments.get(position);
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        holder.txtTaskPercentage.setVisibility(View.GONE);
        int createdUser = leadComment.getCreatedUserId();
        holder.txtCreatedUser.setText(leadComment.getCreatedBy());
        holder.txtComment.setText(leadComment.getComment());
        holder.txtDate.setText(leadComment.getDate().toString());



        if (userId != leadComment.getCreatedUserId()) {//from Server
            holder.ivProfileMe.setVisibility(View.GONE);
            holder.ivProfileOther.setVisibility(View.VISIBLE);
            holder.imgSended.setVisibility(View.GONE);

            holder.txtComment.setGravity(Gravity.LEFT);
            holder.rlStatus.setGravity(Gravity.LEFT);

        } else {
            holder.ivProfileMe.setVisibility(View.VISIBLE);//from app
            holder.ivProfileOther.setVisibility(View.GONE);
            holder.rltvText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            if (leadComment.getServerId() == 0) {
                holder.imgSended.setVisibility(View.GONE);


            } else {
                holder.imgSended.setVisibility(View.VISIBLE);


            }
        }
    }

    @Override
    public int getItemCount() {
        return leadComments.size();
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
            txtTaskPercentage = (TextView) view.findViewById(R.id.txtTaskPercentage);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            ivProfileMe = (ImageView) view.findViewById(R.id.ivProfileMe);
            ivProfileOther = (ImageView) view.findViewById(R.id.ivProfileOther);
            imgSended = (ImageView) view.findViewById(R.id.imgSended);

            rlStatus = (RelativeLayout) view.findViewById(R.id.rlStatus);
            rltvText = (RelativeLayout) view.findViewById(R.id.rltvText);
        }
    }
}
