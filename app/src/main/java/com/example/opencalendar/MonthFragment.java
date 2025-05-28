package com.example.opencalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment {
    private GridView calendarGrid;
    private int monthOffset = 0;
    private int selectedDay = -1;
    private CalendarDayAdapter adapter;

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

        if (getArguments() != null) {
            monthOffset = getArguments().getInt("monthOffset", 0);
            selectedDay = getArguments().getInt("selectedDay", -1);
        }

        calendarGrid = view.findViewById(R.id.calendarGrid);
        setupMonthView();
        return view;
    }

    private void setupMonthView() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        // Определение сегодняшнего дня
        Calendar todayCal = Calendar.getInstance();
        int today = (calendar.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH))
                ? todayCal.get(Calendar.DAY_OF_MONTH) : -1;

        // Генерация дней
        List<String> days = generateDaysForMonth(calendar);

        // Инициализация адаптера
        adapter = new CalendarDayAdapter(requireContext(), days, today, selectedDay);
        calendarGrid.setAdapter(adapter);

        // Обработчик выбора дня
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDayStr = days.get(position);
            if (!selectedDayStr.isEmpty()) {
                int day = Integer.parseInt(selectedDayStr);
                adapter.setSelectedDay(day);
                selectedDay = day;
                onDaySelected(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        day
                );
            }
        });
    }

    private List<String> generateDaysForMonth(Calendar calendar) {
        List<String> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int offset = (firstDayOfWeek - Calendar.MONDAY + 7) % 7;

        for (int i = 0; i < offset; i++) days.add("");
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            days.add(String.valueOf(i));
        }

        return days;
    }

    private void onDaySelected(int year, int month, int day) {
        Bundle result = new Bundle();
        result.putInt("year", year);
        result.putInt("month", month);
        result.putInt("day", day);
        getParentFragmentManager().setFragmentResult("dateSelected", result);
    }
}