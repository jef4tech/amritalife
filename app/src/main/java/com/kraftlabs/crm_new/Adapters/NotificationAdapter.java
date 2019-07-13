package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.NotificationModel;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;

/**
 * User: ashik
 * Date: 6/2/18
 * Time: 3:29 PM
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private static final String TAG=NotificationAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<NotificationModel> notifications= new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notifications) {
        this.context = context;
        this.notifications = notifications;
    }


    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_notif, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        final NotificationModel notificationModel=notifications.get(position);
        try {
            holder.txtTitle.setText(notificationModel.getTitle()+"");
            holder.txtMessage.setText(notificationModel.getMessage()+"");
            holder.txtDate.setText(notificationModel.getDate()+"");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        holder.rlNotification.setOnClickListener(v -> {
                            
                        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle,txtMessage,txtDate;
        RelativeLayout rlNotification;
        public MyViewHolder(View itemView) {
            super(itemView);
            setContentView(itemView);

            
            
        }

        private void setContentView(View view) {
            txtTitle=view.findViewById(R.id.txtNotifTitle);
            txtMessage=view.findViewById(R.id.txtMessage);
            txtDate=view.findViewById(R.id.txtDate);
            rlNotification=view.findViewById(R.id.container);

        }
    }

}
;
