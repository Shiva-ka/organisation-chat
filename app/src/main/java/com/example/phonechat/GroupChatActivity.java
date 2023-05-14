package com.example.phonechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       final ArrayList<messageModel>messageModels = new ArrayList<>();
        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.userName.setText("GROUP CHAT");



       final ChatAdapter adapter = new ChatAdapter(messageModels,this);
       binding.chatRecycleview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycleview.setLayoutManager(layoutManager);

//        now give  message in recycle view  taken taken from database
        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                               now clear message model because hr time ek hi data recycle view me le ke jana hota he na ki sara data hr baar
                                messageModels.clear();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    messageModel model = dataSnapshot.getValue(messageModel.class);
                                    messageModels.add(model);
                                }
//                                adater me sare changes notify kre denge
                               adapter.notifyDataSetChanged();
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



                final String message = binding.etMessage.getText().toString();
                final messageModel model = new messageModel(senderId,message);
                model.setTimestamp(new Date().getTime());

                binding.etMessage.setText("");

//                now store  message in database
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });


            }
        });

    }
}