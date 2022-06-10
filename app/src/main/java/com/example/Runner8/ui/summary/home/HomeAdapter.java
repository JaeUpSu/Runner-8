package com.example.Runner8.ui.summary.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Runner8.ui.F_H.FH_Fragment;
import com.example.Runner8.ui.summary.AnalysticsFragment;

public class HomeAdapter extends FragmentStateAdapter {

    public int mCount;

    public HomeAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return AnalysticsFragment.newInstance(0);
            case 1:
                return FH_Fragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}