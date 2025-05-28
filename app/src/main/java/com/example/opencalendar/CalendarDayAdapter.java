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

import java.util.List;

public class CalendarDayAdapter extends ArrayAdapter<String> {
    private int currentDay = -1;
    private int selectedDay = -1;
    private Context context;

    public CalendarDayAdapter(@NonNull Context context, List<String> days, int currentDay, int selectedDay) {
        super(context, 0, days);
        this.context = context;
        this.currentDay = currentDay;
        this.selectedDay = selectedDay;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Инфлейт или переиспользование view
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_day, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.dayText);
        String dayStr = getItem(position);
        dayText.setText(dayStr);

        // Сброс стилей
        dayText.setBackgroundResource(0);
        dayText.setTextColor(Color.BLACK);

        int dayValue = -1;
        if (dayStr != null && !dayStr.isEmpty()) {
            try {
                dayValue = Integer.parseInt(dayStr);
            } catch (NumberFormatException e) {
                dayValue = -1;
            }
        }

        // Выделение выбранного дня
        if (selectedDay != -1 && dayValue == selectedDay) {
            dayText.setBackgroundResource(R.drawable.selected_bg);
            dayText.setTextColor(Color.WHITE);
        }
        // Выделение сегодняшнего дня
        else if (currentDay != -1 && dayValue == currentDay) {
            dayText.setBackgroundResource(R.drawable.today_bg);
            dayText.setTextColor(Color.WHITE);
        }
        // Базовый стиль
        else {
            dayText.setBackgroundResource(R.drawable.day_cell_background);
        }

        return convertView;
    }

    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    public void setCurrentDay(int day) {
        this.currentDay = day;
        notifyDataSetChanged();
    }
}