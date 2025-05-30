package com.example.opencalendar.Month;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MonthPagerAdapter extends FragmentStateAdapter {
    private static final int TOTAL_PAGES = 5000; // Вместо 10000
    public static final int INITIAL_POSITION = TOTAL_PAGES / 2;

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
        return TOTAL_PAGES;
    }
}