// AppDatabase.java
package com.example.opencalendar;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Абстрактный класс базы данных Room.
 * Определяет:
 * - entities: список сущностей (в данном случае только Goal)
 * - version: версия базы данных
 * - exportSchema: экспорт схемы (false для упрощения)
 * Использует Converters для работы с типами, которые Room не поддерживает напрямую
 */
@Database(entities = {Goal.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Возвращает DAO для работы с целями
     */
    public abstract GoalDao goalDao();
}