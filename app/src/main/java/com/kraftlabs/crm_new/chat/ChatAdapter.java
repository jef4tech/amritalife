
package com.kraftlabs.crm_new.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kraftlabs.crm_new.Models.Message;
import com.kraftlabs.crm_new.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashik on 23-03-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private final String TAG = "CHAT ADAPTER";
    private Context context;
    private Message message = new Message();

    private ArrayList<Message> messages = new ArrayList<>();
    private List<ChatMessage> MessageList = new ArrayList<ChatMessage>();
    public ChatAdapter(Context context, ArrayList<Message> messages) {

        this.context = context;
        this.messages = messages;
        // this.message_parent_id=message_parent_id;
    }




    /*  public void addMessage(ChatMessage object) {
        // TODO Auto-generated method stub

        MessageList.add(object);
        super.(object);
    }*/

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        return position;
    }

    public ChatMessage getItem(int index) {


        return this.MessageList.get(index);
    }
    public void add(ChatMessage object) {
        // TODO Auto-generated method stub

        MessageList.add(object);
        messages.add(object);
    }
    public void update(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new MyViewHolder(itemView);
    }
  /*  public ChatMessage getItem(int index) {

        return this.MessageList.get(index);
    }*/
    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ChatMessage messageobj = getItem(position);
        final Message message = messages.get(position);
        holder.leftChatText.setText(message.getMessage());
        //  if(message.getMessage().equals(null))
      // holder.rightChatText.setComment("Message Replay");
       holder.rightChatText.setText(messageobj.rightMessage);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView leftChatText, rightChatText;
        EditText edtChatText;

        public RelativeLayout relativeLayout;
        public MyViewHolder(View view) {
            super(view);
            leftChatText = (TextView) view.findViewById(R.id.message_left);
            rightChatText = (TextView) view.findViewById(R.id.message_right);
            edtChatText = (EditText) view.findViewById(R.id.messageEditText);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);

        }
    }


}



