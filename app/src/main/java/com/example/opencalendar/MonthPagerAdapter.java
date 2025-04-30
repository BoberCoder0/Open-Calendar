package com.example.opencalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Адаптер для ViewPager2, управляющий фрагментами месяцев
 * Реализует согласованное отображение выбранного дня
 */
public class MonthPagerAdapter extends FragmentStateAdapter {
    public static final int INITIAL_POSITION = Integer.MAX_VALUE / 2;
    private int selectedDay = -1; // Хранение выбранного дня

    public MonthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Обновляет выбранный день и пересоздаёт фрагменты
     */
    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getItemId(int position) {
        // Для поддержки notifyDataSetChanged()
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        return true;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int monthOffset = position - INITIAL_POSITION;
        return MonthFragment.newInstance(monthOffset, selectedDay);
    }
}