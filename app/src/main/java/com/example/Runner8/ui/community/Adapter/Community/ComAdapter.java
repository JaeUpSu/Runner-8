package com.example.Runner8.ui.community.Adapter.Community;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Runner8.ui.community.Fragment.Fragment_Comm_Home;
import com.example.Runner8.ui.community.Fragment.Fragment_Comm_Notice;
import com.example.Runner8.ui.community.Fragment.Fragment_Comm_Popularity;

public class ComAdapter extends FragmentStateAdapter {

    public int mCount;

    public ComAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return Fragment_Comm_Home.newInstance(0);
            case 1:
                return Fragment_Comm_Popularity.newInstance(1);
            case 2:
                return Fragment_Comm_Notice.newInstance(2);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}