package com.example.Runner8.ui.community.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Runner8.R;
import com.example.Runner8.ui.community.Activity.ComSearchActivity;
import com.example.Runner8.ui.community.Adapter.Community.ComAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Fragment_Community extends Fragment {

    Fragment_Comm_Home fragment_comm_home = new Fragment_Comm_Home();

    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentStateAdapter pagerAdapter;

    final List<String> tabElement = Arrays.asList("HOME", "HOT", "NOTICE");

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);


        /*
        LoadingDialogue loadingDialogue = new LoadingDialogue();
        loadingDialogue.show(getActivity().getSupportFragmentManager(), "community");

         */
        Log.i("FragmentLifeCycle", "Fragment_Community onAttach!!");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_community,container,false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.comm_home_toolbar ,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                Intent intent = new Intent(getContext(), ComSearchActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);
        Log.i("FragmentLifeCycle", "Fragment_Community onViewCreated!!");

        tabLayout = view.findViewById(R.id.tab_layout_community);
        viewPager = view.findViewById(R.id.view_pager_community);

        pagerAdapter = new ComAdapter(getActivity(), 3);
        viewPager.setAdapter(pagerAdapter);


        /*
        MaterialContainerTransform enterTransform = new MaterialContainerTransform();
        enterTransform.setDrawingViewId(R.id.nav_community);
        enterTransform.setDuration(1100011);
        enterTransform.setScrimColor(Color.TRANSPARENT);
        enterTransform.setAllContainerColors(requireContext().getColor(R.attr.colorSurface));
        setSharedElementEnterTransition(enterTransform);

         */

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    TextView textView = new TextView(getContext());
                    textView.setText(tabElement.get(position));
                    textView.setGravity(Gravity.CENTER);
                    tab.setCustomView(textView);
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

        viewPager.setUserInputEnabled(false);

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
