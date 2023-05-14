package com.example.phonechat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.phonechat.fragments.callsFragment;
import com.example.phonechat.fragments.chatsgragments;
import com.example.phonechat.fragments.statusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
      switch (position){
          case 0:return new chatsgragments();
          case 1:return new statusFragment();
          case 2:return new callsFragment();
          default:return new chatsgragments();
      }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "CHATS";
        }
        if(position == 1){
            title = "Status";
        }
        if(position == 2){
            title = "Calls";
        }
        return title;
    }
}
