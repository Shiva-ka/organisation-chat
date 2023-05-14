package com.example.phonechat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phonechat.Adapters.userAdapter;
import com.example.phonechat.R;
import com.example.phonechat.databinding.FragmentChatsgragmentsBinding;
import com.example.phonechat.models.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class chatsgragments extends Fragment {

    FragmentChatsgragmentsBinding binding;
    ArrayList<users>  list = new ArrayList<>();
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chatsgragments, container, false);
        binding = FragmentChatsgragmentsBinding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        userAdapter adapter = new userAdapter(list,getContext());

        binding.chatRecycleview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecycleview.setLayoutManager(layoutManager);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users users = dataSnapshot.getValue(users.class);
                    users.setUserid(dataSnapshot.getKey());
//                  jo user login he vo screen pe show nhi hoga ab
                    if(!users.getUserid().equals(FirebaseAuth.getInstance().getUid())){
                    list.add(users);}
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}