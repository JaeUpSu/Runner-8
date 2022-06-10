package com.example.Runner8.ui.map.UFSRecord;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Runner8.TRASH.MyRecordFragment;
import com.example.Runner8.TRASH.TopRecordFragment;

public class UFSRecordAdapter extends FragmentStateAdapter {

    public int mCount;

    public UFSRecordAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return TopRecordFragment.newInstance(0);
            case 1:
                return MyRecordFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}