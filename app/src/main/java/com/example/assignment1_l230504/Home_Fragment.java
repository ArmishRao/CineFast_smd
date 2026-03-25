package com.example.assignment1_l230504;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.viewpager2.widget.ViewPager2;
public class Home_Fragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPage = view.findViewById(R.id.viewPager);

        ViewPageAdapter adapter = new ViewPageAdapter(this);
        viewPage.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPage,
                (tab, position) -> {
                    if (position == 0) tab.setText("Now Showing");
                    else tab.setText("Coming Soon");
                }).attach();

        return view;
    }
}