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
 * Адаптер для отображения дней месяца в календаре
 * Реализует оптимизацию производительности и согласованное выделение дней
 */
public class CalendarDayAdapter extends ArrayAdapter<String> {
    private int currentDay = -1; // Сегодняшний день месяца (-1 - не выделять)
    private int selectedDay = -1; // Выбранный пользователем день (-1 - не выделять)
    private Context context;

    /**
     * Создаёт новый экземпляр адаптера
     * @param context Контекст приложения
     * @param days Список дней для отображения
     * @param currentDay Номер сегодняшнего дня
     * @param selectedDay Номер выбранного пользователем дня
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
        // Оптимизация: переиспользуем существующие view
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

        // Скрываем пустые ячейки (для формирования сетки календаря)
        if (day.isEmpty()) {
            dayText.setVisibility(View.INVISIBLE);
            return convertView;
        }

        dayText.setVisibility(View.VISIBLE);
        int dayInt = Integer.parseInt(day);

        // Логика выделения дней:
        // 1. Если день выбран пользователем - применяем стиль выбранного
        // 2. Если это сегодняшний день - применяем стиль сегодняшнего
        if (dayInt == selectedDay) {
            highlightSelected(dayText);
        } else if (dayInt == currentDay) {
            highlightToday(dayText);
        }

        return convertView;
    }

    /**
     * Применяет стиль выделения для выбранного дня
     */
    private void highlightSelected(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.selected_bg);
        dayText.setTextColor(Color.WHITE);
    }

    /**
     * Применяет стиль выделения для сегодняшнего дня
     */
    private void highlightToday(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.today_bg);
        dayText.setTextColor(Color.WHITE);
    }

    /**
     * Обновляет выбранный день и перерисовывает календарь
     */
    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    /**
     * Обновляет текущий день и перерисовывает календарь
     */
    public void setCurrentDay(int day) {
        this.currentDay = day;
        notifyDataSetChanged();
    }
}