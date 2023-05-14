package com.example.phonechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.phonechat.Adapters.ChatAdapter;
import com.example.phonechat.databinding.ActivityChatDetailBinding;
import com.example.phonechat.models.messageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
//        now receive data
       final String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("userid");
        String  userName= getIntent().getStringExtra("username");
        String  profilePic= getIntent().getStringExtra("profilepic");

        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.mans).into(binding.profileimage);
       binding.backarrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
               startActivity(intent);
           }
       });
       final ArrayList<messageModel> messageModels = new ArrayList<>();
       final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,receiveId);
       binding.chatRecycleview.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycleview.setLayoutManager(layoutManager);

        final String SenderRoom = senderId + receiveId;
        final String ReceiverRoom = receiveId + senderId;

        database.getReference().child("Chats")
                        .child(SenderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messageModels.clear();
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                           messageModel model = snapshot1.getValue(messageModel.class);
                                   model.setMessageId(snapshot1.getKey());
                                     messageModels.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.etMessage.getText().toString().isEmpty()) {
                    binding.etMessage.setError(" Please Enter your Message");
                    return;
                }


                 String  message = binding.etMessage.getText().toString();
                 final messageModel model = new messageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                 binding.etMessage.setText("");

                 database.getReference().child("Chats")
                         .child(SenderRoom)
                         .push()
                         .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 database.getReference().child("Chats")
                                         .child(ReceiverRoom)
                                         .push()
                                         .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void unused) {

                                             }
                                         });
                             }
                         });
            }
        });
    }
}