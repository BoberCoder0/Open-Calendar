// MonthPagerAdapter.java
package com.example.opencalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Адаптер для ViewPager2, реализующий бесконечный скроллинг месяцев.
 * Создает фрагменты месяцев с учетом смещения от текущего месяца.
 */
public class MonthPagerAdapter extends FragmentStateAdapter {
    public static final int INITIAL_POSITION = Integer.MAX_VALUE / 2; // Начальная позиция для "бесконечного" скроллинга
    private int selectedDay = -1; // Выбранный день для выделения во всех месяцах

    /**
     * Конструктор адаптера
     */
    public MonthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Устанавливает выбранный день и обновляет адаптер
     */
    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int BIG_NUM = 100000000;
        // еще есть Integer.MAX_VALUE
        return Integer.MAX_VALUE; // Очень большое число для эффекта бесконечности
    }

    @Override
    public long getItemId(int position) {
        return position; // Уникальный идентификатор для каждой позиции
    }

    @Override
    public boolean containsItem(long itemId) {
        return true; // Все позиции существуют (для бесконечного скроллинга)
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Создание фрагмента месяца с нужным смещением
        int monthOffset = position - INITIAL_POSITION;
        return MonthFragment.newInstance(monthOffset, selectedDay);
    }
}