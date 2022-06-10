package com.example.Runner8.ui.summary.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Runner8.R;
import com.example.Runner8.ui.summary.Fragment_Summary_home;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    Fragment_Summary_home fragment_summary_home = new Fragment_Summary_home();

    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentStateAdapter pagerAdapter;

    View tab_view1, tab_view2, tab_view3;

    public HomeFragment(){

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        Log.i("FragmentLifeCycle", "HomeFragment onAttach!!");
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("FragmentLifeCycle", "HomeFragment onCreate!!");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        tab_view1 = getLayoutInflater().inflate(R.layout.tab_item_summary_view, null);
        tab_view1.findViewById(R.id.home_summary).setBackgroundResource(R.drawable.analytics);

        tab_view2 = getLayoutInflater().inflate(R.layout.tab_item_food_view, null);

        // icon reset need
        tab_view2.findViewById(R.id.home_food).setBackgroundResource(R.drawable.calorie_home);

        List<View> tabElement2 = Arrays.asList(tab_view1, tab_view2);

        getParentFragmentManager().beginTransaction().replace(R.id.home_frame_layout, fragment_summary_home).commit();

        // viewPager
        pagerAdapter = new HomeAdapter(getActivity(), 2);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setCustomView(tabElement2.get(position));
                }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels == 0){
                    viewPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("FragmentLifeCycle", "HomeFragment onActivityCreated!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("FragmentLifeCycle", "HomeFragment onResume!!");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("FragmentLifeCycle", "HomeFragment onPause!!");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i("FragmentLifeCycle", "HomeFragment onStop!!");
    }

}


