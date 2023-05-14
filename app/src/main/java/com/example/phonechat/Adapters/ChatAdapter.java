package com.example.phonechat.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonechat.R;
import com.example.phonechat.models.messageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter  extends RecyclerView.Adapter{
ArrayList<messageModel> messageModels;
Context context;
String receiverId;
int SENDER_VIEW_TYPE=1;
int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<messageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<messageModel> messageModels, Context context, String receiverId) {
        this.messageModels = messageModels;
        this.context = context;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == SENDER_VIEW_TYPE){
           View view = LayoutInflater.from(context).inflate(R.layout.sampl_sender,parent,false);
           return new SenderViewholder(view);
       }
       else {
           View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
           return new ReceiverViewholder(view);
       }


    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messageModel messageModel = messageModels.get(position);
//        now this long click for deleting the message
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete this message ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                          String senderRoom =  FirebaseAuth.getInstance().getUid() + receiverId;
                          database.getReference().child("Chats").child(senderRoom)
                                  .child(messageModel.getMessageId())
                                  .setValue(null);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if(holder.getClass() == SenderViewholder.class){
            ((SenderViewholder)holder).senderMsg.setText(messageModel.getMessage());
        }
        else{
            ((ReceiverViewholder)holder).recieverMsg.setText(messageModel.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewholder extends RecyclerView.ViewHolder {

        TextView recieverMsg,recieverTime;

        public ReceiverViewholder(@NonNull View itemView) {
            super(itemView);
            recieverMsg =itemView.findViewById(R.id.receiverText);
            recieverTime =itemView.findViewById(R.id.receiverTime);

        }
    }
    public class SenderViewholder extends RecyclerView.ViewHolder {

        TextView senderMsg,senderTime;

        public SenderViewholder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}
