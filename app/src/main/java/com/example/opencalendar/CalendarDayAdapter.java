// CalendarDayAdapter.java
package com.example.opencalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.util.List;

/**
 * Адаптер для отображения дней в GridView календаря.
 * Обрабатывает выделение текущего и выбранного дней.
 */
public class CalendarDayAdapter extends ArrayAdapter<String> {
    private int currentDay = -1; // Сегодняшний день (если отображается текущий месяц)
    private int selectedDay = -1; // Выбранный пользователем день
    private Context context; // Контекст приложения

    /**
     * Конструктор адаптера
     */
    public CalendarDayAdapter(@NonNull Context context, List<String> days, int currentDay, int selectedDay) {
        super(context, 0, days);
        this.context = context;
        this.currentDay = currentDay;
        this.selectedDay = selectedDay;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Оптимизация: переиспользование View
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_day, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.dayText);
        String day = getItem(position);
        dayText.setText(day);

        // Базовые стили
        dayText.setBackgroundResource(R.drawable.day_cell_background);
        dayText.setTextColor(Color.BLACK);

        // Скрытие пустых ячеек (для выравнивания)
        if (day.isEmpty()) {
            dayText.setVisibility(View.INVISIBLE);
            return convertView;
        }

        dayText.setVisibility(View.VISIBLE);
        int dayInt = Integer.parseInt(day);

        // Выделение выбранного или текущего дня
        if (dayInt == selectedDay) {
            highlightSelected(dayText);
        } else if (dayInt == currentDay) {
            highlightToday(dayText);
        }

        return convertView;
    }

    /**
     * Применяет стиль для выбранного дня
     */
    private void highlightSelected(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.selected_bg);
        dayText.setTextColor(Color.WHITE);
    }

    /**
     * Применяет стиль для сегодняшнего дня
     */
    private void highlightToday(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.today_bg);
        dayText.setTextColor(Color.WHITE);
    }

    /**
     * Устанавливает выбранный день и обновляет отображение
     */
    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    /**
     * Устанавливает текущий день и обновляет отображение
     */
    public void setCurrentDay(int day) {
        this.currentDay = day;
        notifyDataSetChanged();
    }
}