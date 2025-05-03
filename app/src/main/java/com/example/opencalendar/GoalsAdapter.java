// GoalsAdapter.java
package com.example.opencalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Адаптер для RecyclerView, отображающего список целей.
 * Обрабатывает создание и привязку ViewHolder'ов.
 */
public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
    private List<Goal> goals; // Список целей
    private Context context; // Контекст приложения

    /**
     * ViewHolder для элемента списка целей.
     * Содержит ссылки на элементы макета.
     */
    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView title; // Заголовок цели
        TextView daysLeft; // Количество дней до выполнения

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.goalTitle);
            daysLeft = itemView.findViewById(R.id.daysLeft);
        }
    }

    /**
     * Конструктор адаптера
     * @param goals список целей для отображения
     * @param context контекст приложения
     */
    public GoalsAdapter(List<Goal> goals, Context context) {
        this.goals = goals;
        this.context = context;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового ViewHolder'а
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        // Привязка данных к ViewHolder'у
        Goal goal = goals.get(position);
        holder.title.setText(goal.getTitle());
        holder.daysLeft.setText("Дней до цели: " + goal.calculateDaysLeft());
        holder.itemView.setOnClickListener(v -> showGoalDetails(goal));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    /**
     * Обновляет список целей
     * @param newGoals новый список целей
     */
    public void updateList(List<Goal> newGoals) {
        goals = newGoals;
        notifyDataSetChanged();
    }

    /**
     * Показывает детали цели (заглушка)
     */
    private void showGoalDetails(Goal goal) {
        // TODO: Реализовать отображение деталей цели
    }
}