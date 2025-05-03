// Goal.java
package com.example.opencalendar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.Calendar;

/**
 * Класс сущности "Цель" для Room.
 * Содержит информацию о цели: название, описание и целевую дату.
 */
@Entity(tableName = "goals")
@TypeConverters(Converters.class)
public class Goal {
    @PrimaryKey(autoGenerate = true)
    private int id; // Уникальный идентификатор (автогенерация)

    private String title; // Название цели
    private String description; // Описание цели
    private Calendar targetDate; // Дата выполнения

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Calendar getTargetDate() { return targetDate; }
    public void setTargetDate(Calendar targetDate) { this.targetDate = targetDate; }

    /**
     * Вычисляет количество дней до выполнения цели
     * @return количество дней до targetDate
     */
    public int calculateDaysLeft() {
        Calendar today = Calendar.getInstance();
        long diff = targetDate.getTimeInMillis() - today.getTimeInMillis();
        return (int) (diff / (24 * 60 * 60 * 1000)); // Преобразование миллисекунд в дни
    }
}