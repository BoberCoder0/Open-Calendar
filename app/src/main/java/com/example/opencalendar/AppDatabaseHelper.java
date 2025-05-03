// AppDatabaseHelper.java
package com.example.opencalendar;

import android.content.Context;
import androidx.room.Room;

/**
 * Вспомогательный класс для работы с базой данных Room.
 * Реализует паттерн Singleton для обеспечения единственного экземпляра БД.
 */
public class AppDatabaseHelper {
    private static AppDatabase instance; // Единственный экземпляр базы данных

    /**
     * Возвращает экземпляр базы данных (создает при первом вызове)
     * @param context контекст приложения
     * @return экземпляр AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "goals-database"
                    ).fallbackToDestructiveMigration() // Разрешает "разрушительные" миграции
                    .build();
        }
        return instance;
    }
}