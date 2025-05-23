package com.example.opencalendar;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opencalendar.MonthFragment;

public class MonthPagerAdapter extends FragmentStateAdapter {
    public static final int INITIAL_POSITION = Integer.MAX_VALUE / 2;

    public MonthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int monthOffset = position - INITIAL_POSITION;
        MonthFragment fragment = MonthFragment.newInstance(monthOffset);
        Log.d("PagerAdapter", "Creating fragment for position: " + position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}