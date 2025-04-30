package com.example.opencalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Фрагмент для отображения одного месяца календаря
 * Реализует согласованное выделение дней между месяцами
 */
public class MonthFragment extends Fragment {
    private GridView calendarGrid;
    private int monthOffset = 0; // Смещение от текущего месяца (0 - текущий месяц)
    private int selectedDay = -1; // Выбранный пользователем день
    private CalendarDayAdapter adapter;

    /**
     * Фабричный метод для создания фрагмента с указанием смещения месяца и выбранного дня
     * @param monthOffset Смещение от текущего месяца
     * @param selectedDay Выбранный пользователем день
     * @return Новый экземпляр MonthFragment
     */
    public static MonthFragment newInstance(int monthOffset, int selectedDay) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("monthOffset", monthOffset);
        args.putInt("selectedDay", selectedDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        // Получаем переданные данные
        if (getArguments() != null) {
            monthOffset = getArguments().getInt("monthOffset", 0);
            selectedDay = getArguments().getInt("selectedDay", -1);
        }

        // Инициализация UI элементов
        calendarGrid = view.findViewById(R.id.calendarGrid);

        setupMonthView();
        return view;
    }

    /**
     * Обновляет выбранный день и перерисовывает календарь
     */
    public void updateSelectedDay(int day) {
        this.selectedDay = day;
        if (adapter != null) {
            adapter.setSelectedDay(day);
        }
    }

    private void setupMonthView() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        Calendar todayCal = Calendar.getInstance();
        int today = (calendar.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH))
                ? todayCal.get(Calendar.DAY_OF_MONTH) : -1;

        List<String> days = generateDaysForMonth(calendar);
        adapter = new CalendarDayAdapter(requireContext(), days, today, selectedDay);
        calendarGrid.setAdapter(adapter);

        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDayStr = days.get(position);
            if (!selectedDayStr.isEmpty()) {
                int day = Integer.parseInt(selectedDayStr);
                updateSelectedDay(day);

                // Уведомляем MainActivity о выборе дня
                onDaySelected(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), day);
            }
        });
    }

    private void onDaySelected(int year, int month, int day) {
        Bundle result = new Bundle();
        result.putInt("year", year);
        result.putInt("month", month);
        result.putInt("day", day);
        getParentFragmentManager().setFragmentResult("dateSelected", result);
    }

    /**
     * Генерирует список дней для отображения в сетке календаря
     * @param calendar Календарь с установленным нужным месяцем
     * @return Список строк с днями (пустые строки для выравнивания)
     */
    private List<String> generateDaysForMonth(Calendar calendar) {
        List<String> days = new ArrayList<>();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Устанавливаем 1-е число месяца и получаем день недели
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Добавляем пустые строки для выравнивания первого дня
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add("");
        }

        // Добавляем все дни месяца
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        return days;
    }
}