package com.example.assignment1_l230504;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new NowShowingFragment();
        else
            return new ComingSoonFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
