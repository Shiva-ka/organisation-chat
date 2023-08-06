package com.example.phonechat.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.core.widget.TextViewOnReceiveContentListener;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonechat.ChatDetailActivity;
import com.example.phonechat.R;
import com.example.phonechat.models.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class userAdapter  extends  RecyclerView.Adapter<userAdapter.ViewHolder>{

    ArrayList<users> list;
    Context  context;

    public userAdapter(ArrayList<users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        users users = list.get(position);

//Picasso.get() .load(users.getProfilepic()).placeholder(R.drawable.mans).into(holder.image);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.mans).into(holder.image);
        holder.userName.setText(users.getUsername());
//        now update last message at mainactivity from firebase
//        id  login user + receiver user
//        we use ordeer by child to  store meesage  in desending form in fare base means last message come first
        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserid())
                .orderByChild("timestamp")
                        .limitToLast(1)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                              if  any message present thent lastseen show or for loop start
                                        if(snapshot.hasChildren())
                                        {
                                        for(DataSnapshot snapshot1:snapshot.getChildren()){
//                                       now we got last message we set it in holder now
                                        holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                                        }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


//        now this is for chat details activity
//        sending userid, propfile pic , username  to chat activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userid",users.getUserid());
                intent.putExtra("profilepic",users.getProfilepic());
                intent.putExtra("username",users.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
       TextView  userName,lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          image = itemView.findViewById(R.id.profileimage);
          userName= itemView.findViewById(R.id.Usernamelist);
            lastMessage = itemView.findViewById(R.id.Lastmessage);
        }
    }
}
