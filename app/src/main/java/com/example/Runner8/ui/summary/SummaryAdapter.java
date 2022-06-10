package com.example.Runner8.ui.summary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SummaryAdapter extends FragmentStateAdapter {

public int mCount;

public SummaryAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
        }

@NonNull
@Override
public Fragment createFragment(int position) {
    int index = getRealPosition(position);

    switch (index) {
        case 0:
            return new Fragment_Summary_home();
        case 1:
            return Fragment_Summary_two.newInstance(index + 1);
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