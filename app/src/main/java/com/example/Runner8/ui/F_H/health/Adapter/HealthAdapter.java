package com.example.Runner8.ui.F_H.health.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Runner8.TRASH.FragmentSecond;
import com.example.Runner8.TRASH.HealthFragment;

public class HealthAdapter extends FragmentStateAdapter {

public int mCount;

public HealthAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
        }

@NonNull
@Override
public Fragment createFragment(int position) {
    int index = getRealPosition(position);

    switch (index) {
        case 0:
            return new HealthFragment();
        case 1:
            return FragmentSecond.newInstance(index + 1);
        default:
            return null;
    }
}
@Override
public int getItemCount() {
        return 2000;
        }

public int getRealPosition(int position) { return position % mCount; }

        }