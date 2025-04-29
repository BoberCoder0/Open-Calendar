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

public class MonthFragment extends Fragment {

    private TextView monthTitle;
    private GridView calendarGrid;
    private int monthOffset = 0; // Смещение от текущего месяца (0 - текущий месяц)
    private CalendarDayAdapter adapter;
    private int currentYear, currentMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        // Инициализация UI элементов
        monthTitle = view.findViewById(R.id.monthTitle);
        calendarGrid = view.findViewById(R.id.calendarGrid);

        // Скрываем заголовок месяца (по вашему требованию)
        monthTitle.setVisibility(View.GONE);

        // Получаем переданное смещение месяца (если есть)
        if (getArguments() != null) {
            monthOffset = getArguments().getInt("monthOffset", 0);
        }

        setupMonthView();
        return view;


    }

    private void setupMonthView() {
        // 1. Настраиваем календарь с учетом смещения
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        // Сохраняем год и месяц для использования в обработчике кликов
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);

        // 2. Устанавливаем заголовок месяца (хотя он скрыт)
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthTitle.setText(monthFormat.format(calendar.getTime()));

        // 3. Генерируем список дней для отображения
        List<String> days = generateDaysForMonth(calendar);

        // 4. Определяем сегодняшний день (если это текущий месяц)
        Calendar todayCal = Calendar.getInstance();
        int today = -1; // -1 означает "не выделять"

        if (calendar.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH)) {
            today = todayCal.get(Calendar.DAY_OF_MONTH);
        }

        // 5. Создаем адаптер с передачей дней и номера сегодняшнего дня
        CalendarDayAdapter adapter = new CalendarDayAdapter(requireContext(), days, today);
        calendarGrid.setAdapter(adapter);

        // 6. Настраиваем обработчик кликов по дням
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDay = days.get(position);
            if (!selectedDay.isEmpty()) {
                onDaySelected(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        Integer.parseInt(selectedDay));
            }
        });

        calendarGrid.setOnTouchListener((v, event) -> {
            getParentFragmentManager().setFragmentResult("requestKey", new Bundle());
            return false;
        });

        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDay = days.get(position);
            if (!selectedDay.isEmpty()) {
                int day = Integer.parseInt(selectedDay);
                adapter.setSelectedDay(day); // Обновляем выделение в адаптере
                onDaySelected(currentYear, currentMonth, day);
            }
        });
    }

    private void onDaySelected(int year, int month, int day) {
        // Передаем выбранную дату в MainActivity
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
        // (Calendar.SUNDAY = 1, Calendar.SATURDAY = 7)
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add("");
        }

        // Добавляем все дни месяца
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        return days;
    }

    /**
     * Обрабатывает выбор дня в календаре
     * @param year Год выбранной даты
     * @param month Месяц (0-11)
     * @param day День месяца
     */

    /**
     * Фабричный метод для создания фрагмента с указанием смещения месяца
     * @param monthOffset Смещение от текущего месяца (0 - текущий месяц)
     * @return Новый экземпляр MonthFragment
     */
    public static MonthFragment newInstance(int monthOffset) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("monthOffset", monthOffset);
        fragment.setArguments(args);
        return fragment;
    }

}