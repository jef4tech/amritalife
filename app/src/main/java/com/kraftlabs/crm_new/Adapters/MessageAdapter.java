package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Models.Message;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.chat.ChatFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


//import com.kraftlabs.sfm.test.ChatFragment;

/**
 * Created by ajith on 29/7/16.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private final String TAG = "MESSAGE ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    private ArrayList<Message> messages = new ArrayList<>();
   // Point p;
     private PopupWindow mPopupWindow;
    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;

    }

    public void update(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Message message = messages.get(position);
        holder.txtMessageTitle.setText(message.getMessageTitle());
        holder.txtMessage.setText(message.getMessage());
        holder.txtDate.setText(message.getDate());
        holder.txtCreatedUser.setText(context.getResources().getString(R.string.created_by) + message.getCreatedUserName());
        holder.txtStatus.setText(message.getStatus());

        holder.relativeLayoutMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = ChatFragment.newInstance(message.getMessageAssignId());
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();


            }


        });


    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMessageTitle, txtMessage, txtDate, txtCreatedUser, txtStatus;
        RelativeLayout relativeLayoutMsg;

        public MyViewHolder(View view) {
            super(view);
            txtMessageTitle = (TextView) view.findViewById(R.id.txtMessageTitle);
            txtMessage = (TextView) view.findViewById(R.id.txtMessage);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtCreatedUser = (TextView) view.findViewById(R.id.txtCreatedUser);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            relativeLayoutMsg= (RelativeLayout) view.findViewById(R.id.rlItemMessage);

        }
    }


}
