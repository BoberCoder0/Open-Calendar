
   /* @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        Calendar calendar = Calendar.getInstance();
        if (value != null) calendar.setTimeInMillis(value);
        return calendar;
    }

    @TypeConverter
    public static Long calendarToTimestamp(Calendar calendar) {
        return calendar != null ? calendar.getTimeInMillis() : null;
    }*/

    /*// Конвертация Calendar → Long
    @TypeConverter
    public static Long calendarToLong(Calendar calendar) {
        return calendar != null ? calendar.getTimeInMillis() : null;
    }

    // Конвертация Long → Calendar
    @TypeConverter
    public static Calendar longToCalendar(Long value) {
        Calendar calendar = Calendar.getInstance();
        if (value != null) {
            calendar.setTimeInMillis(value);
        }
        return calendar;
    }*/



    // Converters.java
package com.example.opencalendar;

import androidx.room.TypeConverter;
import java.util.Calendar;

    /**
     * Класс конвертеров для Room.
     * Преобразует сложные типы данных в примитивы, которые Room может сохранить.
     */
    public class Converters {
        /**
         * Конвертирует Calendar в Long (миллисекунды с эпохи Unix)
         * @param calendar объект Calendar для конвертации
         * @return время в миллисекундах или null, если calendar равен null
         */
        @TypeConverter
        public static Long calendarToTimestamp(Calendar calendar) {
            return calendar == null ? null : calendar.getTimeInMillis();
        }

        /**
         * Конвертирует Long (миллисекунды) обратно в Calendar
         * @param value время в миллисекундах с эпохи Unix
         * @return объект Calendar или текущую дату, если value равен null
         */
        @TypeConverter
        public static Calendar timestampToCalendar(Long value) {
            Calendar calendar = Calendar.getInstance();
            if (value != null) {
                calendar.setTimeInMillis(value);
            }
            return calendar;
        }
    }