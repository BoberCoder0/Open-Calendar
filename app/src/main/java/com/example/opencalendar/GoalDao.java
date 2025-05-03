// GoalDao.java
package com.example.opencalendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

/**
 * Data Access Object (DAO) для работы с целями в базе данных.
 * Определяет CRUD операции для сущности Goal.
 */
@Dao
public interface GoalDao {
    @Insert
    void insert(Goal goal); // Добавление новой цели

    @Update
    void update(Goal goal); // Обновление существующей цели

    @Delete
    void delete(Goal goal); // Удаление цели

    @Query("SELECT * FROM goals")
    List<Goal> getAllGoals(); // Получение всех целей
}