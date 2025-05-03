// MonthFragment.java
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
 * Фрагмент для отображения календаря одного месяца.
 * Содержит GridView с днями месяца и обрабатывает выбор дня.
 */
public class MonthFragment extends Fragment {
    private GridView calendarGrid; // Сетка для отображения дней
    private int monthOffset = 0; // Смещение от текущего месяца
    private int selectedDay = -1; // Выбранный день
    private CalendarDayAdapter adapter; // Адаптер для дней

    /**
     * Фабричный метод для создания фрагмента с параметрами
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

        // Получение параметров
        if (getArguments() != null) {
            monthOffset = getArguments().getInt("monthOffset", 0);
            selectedDay = getArguments().getInt("selectedDay", -1);
        }

        calendarGrid = view.findViewById(R.id.calendarGrid);
        setupMonthView(); // Настройка отображения месяца
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

    /**
     * Настройка отображения месяца
     */
    private void setupMonthView() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        // Определение сегодняшнего дня (если это текущий месяц)
        Calendar todayCal = Calendar.getInstance();
        int today = (calendar.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH))
                ? todayCal.get(Calendar.DAY_OF_MONTH) : -1;

        // Генерация списка дней и настройка адаптера
        List<String> days = generateDaysForMonth(calendar);
        adapter = new CalendarDayAdapter(requireContext(), days, today, selectedDay);
        calendarGrid.setAdapter(adapter);

        // Обработчик выбора дня
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDayStr = days.get(position);
            if (!selectedDayStr.isEmpty()) {
                int day = Integer.parseInt(selectedDayStr);
                updateSelectedDay(day);

                // Отправка выбранной даты в MainActivity
                onDaySelected(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), day);
            }
        });
    }

    /**
     * Отправка выбранной даты через FragmentResult
     */
    private void onDaySelected(int year, int month, int day) {
        Bundle result = new Bundle();
        result.putInt("year", year);
        result.putInt("month", month);
        result.putInt("day", day);
        getParentFragmentManager().setFragmentResult("dateSelected", result);
    }

    /**
     * Генерация списка дней для GridView
     */
    /*private List<String> generateDaysForMonth(Calendar calendar) {
        List<String> days = new ArrayList<>();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Установка 1-го числа и определение дня недели
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Пустые строки для выравнивания первого дня
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add("");
        }

        // Добавление дней месяца
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        return days;
    }*/

    private List<String> generateDaysForMonth(Calendar calendar) {
        List<String> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int offset = (firstDayOfWeek - Calendar.MONDAY + 7) % 7; // Исправленная логика смещения

        for (int i = 0; i < offset; i++) days.add("");
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            days.add(String.valueOf(i));
        }
        return days;
    }
}