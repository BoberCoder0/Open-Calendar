package com.example.opencalendar.Goal;

import android.graphics.Color;

import java.util.Calendar;

public class Goal {
    private String title;
    private String description;
    private boolean expanded;
    private Calendar date;
    private int color;

    public Goal(String title, String description, int color) { // Обновленный конструктор
        this.title = title;
        this.description = description;
        this.expanded = false;
        this.date = Calendar.getInstance();
        this.color = color;
    }

    public Goal(String title, String description) {
        this(title, description, Color.BLUE); // Цвет по умолчанию
    }

    public Calendar getDate() { return date; }
    public void setDate(Calendar date) { this.date = date; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getColor() { return color; } // Геттер цвета
    public void setColor(int color) { this.color = color; } // Сеттер цвета

    // Метод для расчета дней до цели
    public String getDaysLeft() {
        Calendar today = Calendar.getInstance();
        long diff = date.getTimeInMillis() - today.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        if (days == 0) return "Сегодня!";
        if (days > 0) return "Осталось: " + days + " дн.";
        return "Просрочено: " + (-days) + " дн. назад";
    }
}