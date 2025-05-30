package com.example.opencalendar.Goal;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencalendar.R;

import java.util.List;
import java.util.Locale;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {

    private final List<Goal> goals;

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.bind(goal);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder { // Убрано static
        private final TextView goalTitle;
        private final TextView goalDescription;
        private final TextView goalDate;
        private final TextView daysLeft;
        private final View colorIndicator;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalTitle = itemView.findViewById(R.id.goal_title);
            goalDescription = itemView.findViewById(R.id.goal_description);
            goalDate = itemView.findViewById(R.id.goal_date);
            daysLeft = itemView.findViewById(R.id.days_left);
            colorIndicator = itemView.findViewById(R.id.color_indicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Goal goal = goals.get(position); // Теперь доступ к goals возможен
                    goal.setExpanded(!goal.isExpanded());
                    notifyItemChanged(position); // Теперь доступ к методу адаптера
                }
            });
        }

        public void bind(Goal goal) {
            goalTitle.setText(goal.getTitle());
            goalDescription.setText(goal.getDescription());
            goalDescription.setMaxLines(goal.isExpanded() ? Integer.MAX_VALUE : 2);

            // Устанавливаем дату
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            goalDate.setText(sdf.format(goal.getDate().getTime()));

            // Устанавливаем дни
            daysLeft.setText(goal.getDaysLeft());

            // Устанавливаем цвет
            colorIndicator.setBackgroundColor(goal.getColor());
            itemView.setBackgroundColor(adjustAlpha(goal.getColor(), 0.2f));
        }

        private int adjustAlpha(int color, float factor) {
            int alpha = Math.round(Color.alpha(color) * factor);
            return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        }
    }
}