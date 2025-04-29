package com.example.opencalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.content.ContextCompat;


public class CalendarDayAdapter extends ArrayAdapter<String> {

    private int currentDay = -1;// Текущий день месяца, который нужно выделить (-1 - нет выделения)
    private int selectedDay = -1; // Добавляем переменную для хранения выбранного дня
    private int highlightColor = Color.parseColor("#FF4285F4"); // Голубой цвет для выделения
    private int selectedColor = Color.parseColor("#FF00FF00"); // Цвет для выделения выбранного
    private int selectedPosition = -1;  // Выбранный день

    /**
     * Конструктор адаптера
     * @param context Контекст приложения
     * @param days Список дней для отображения
     * @param currentDay Номер сегодняшнего дня (или -1 если не нужно выделять)
     */
    public CalendarDayAdapter(@NonNull Context context, List<String> days, int currentDay) {
        super(context, 0, days);
        this.currentDay = currentDay;
    }

    @NonNull
    @Override
    /*public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Переиспользование view (оптимизация производительности)
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_day, parent, false);
        }

        String day = getItem(position);
        TextView dayText = convertView.findViewById(R.id.dayText);

        // 2. Установка текста дня
        dayText.setText(day);

        // 3. Сброс стилей для всех дней
        dayText.setBackgroundResource(0); // Убираем фон
        dayText.setTextColor(Color.BLACK); // Стандартный цвет текста

        // 4. Обработка пустых ячеек (для выравнивания календаря)
        if (day.isEmpty()) {
            dayText.setVisibility(View.INVISIBLE);
        } else {
            dayText.setVisibility(View.VISIBLE);

            // 5. Выделение сегодняшнего дня
            if (currentDay != -1 && day.equals(String.valueOf(currentDay))) {
                highlightToday(dayText); // Применяем стиль выделения
            }
        }

        // 5. Выделение сегодняшнего дня
        if (currentDay != -1 && day.equals(String.valueOf(currentDay))) {
            highlightToday(dayText);
        }

        // 6. Выделение выбранного дня
        if (selectedDay != -1 && day.equals(String.valueOf(selectedDay))) {
            highlightSelected(dayText);
        }

        // Меняем фон, если день выбран
        if (position == selectedPosition) {
            dayText.setBackgroundResource(R.drawable.selected_bg);
        } else {
            dayText.setBackgroundResource(R.drawable.today_bg);
        }

        return convertView;
    }*/

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_day, parent, false);
        }

        String day = getItem(position);
        TextView dayText = convertView.findViewById(R.id.dayText);
        dayText.setText(day);

        // Всегда устанавливаем базовую границу
        dayText.setBackgroundResource(R.drawable.day_cell_background);
        dayText.setTextColor(Color.BLACK);

        /*// Сброс стилей
        dayText.setBackgroundResource(0);
        dayText.setTextColor(Color.BLACK);*/

        if (day.isEmpty()) {
            dayText.setVisibility(View.INVISIBLE);
            return convertView;
        }

        dayText.setVisibility(View.VISIBLE);

        // Для выделенных дней создаем LayerDrawable, чтобы сохранить границу
        if ((currentDay != -1 && day.equals(String.valueOf(currentDay)))) {
            LayerDrawable layers = new LayerDrawable(new Drawable[]{
                    ContextCompat.getDrawable(getContext(), R.drawable.today_bg),
                    ContextCompat.getDrawable(getContext(), R.drawable.today_bg)
            });
            layers.setLayerInset(1, 2, 2, 2, 2); // Отступы для цветного фона
            dayText.setBackground(layers);
            dayText.setTextColor(Color.WHITE);
        }

        // Выделение сегодняшнего дня
        /*if (currentDay != -1 && day.equals(String.valueOf(currentDay))) {
            dayText.setBackgroundResource(R.drawable.today_bg);
            dayText.setTextColor(Color.WHITE);
        }*/

        // Выделение выбранного дня
        if (selectedDay != -1 && day.equals(String.valueOf(selectedDay))) {
            dayText.setBackgroundResource(R.drawable.selected_bg);
            dayText.setTextColor(Color.WHITE);
        }

        return convertView;
    }

    /**
     * Применяет стиль выделения для выбранного дня
     */
    private void highlightSelected(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.selected_bg);  // Используем готовый drawable
        dayText.setTextColor(Color.WHITE);
    }                                                 //soooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo

    /**
     * Устанавливает выбранный день
     */
    public void setSelectedDay(int day) {
        this.selectedDay = day;
        notifyDataSetChanged();
    }

    /**
     * Применяет стиль выделения для сегодняшнего дня
     * @param dayText TextView дня, который нужно выделить
     */
    private void highlightToday(TextView dayText) {
        // Вариант 1: Простое выделение цветом
        dayText.setBackgroundResource(R.drawable.today_bg);  // Используем готовый drawable
        dayText.setTextColor(Color.WHITE);


        // Вариант 2: Использование drawable-ресурса (рекомендуется)
        // Создайте файл res/drawable/today_bg.xml с нужным оформлением
        ///dayText.setBackgroundResource(R.drawable.today_bg);
        ///dayText.setTextColor(Color.WHITE);

    }
    //ssoooooooooooooooooooooooooooooooooooooooooooooooooooooooo


    /*private void highlightToday(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.today_bg);
        dayText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }
    private void highlightSelected(TextView dayText) {
        dayText.setBackgroundResource(R.drawable.selected_bg);
        dayText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }*/                                                                        //ну такоееееееееееееееееееееееееееееее

    /**
     * Обновляет текущий день для выделения
     * @param day Номер дня (или -1 чтобы снять выделение)
     */
    public void setCurrentDay(int day) {
        this.currentDay = day;
        notifyDataSetChanged(); // Обновляем отображение
    }

    // Метод для обновления выбранного дня
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();  // Перерисовываем GridView
    }

    //оптимизация
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}