package com.kraftlabs.crm_new.chat;


import com.kraftlabs.crm_new.Models.Message;

/**
 * Created by Ashik on 23-03-2017.
 */


    public class ChatMessage extends Message {

  //  public boolean left;
   // public boolean right;
   // public String leftMessage;
    public String rightMessage;
    // public String leftMessage;
    public ChatMessage( String rightMessage) {
        super();
      //  this.left = left;
     //   this.right = right;
      //  this.leftMessage = leftMessage;
        this.rightMessage = rightMessage;
    }



/*public ChatMessage(boolean left , String leftMessage,String rightMessage) {
        // TODO Auto-generated constructor stub
        super();
        this.left=left;
        this.rightMessage= message;
    }*/

}