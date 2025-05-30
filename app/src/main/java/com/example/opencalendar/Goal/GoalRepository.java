package com.example.opencalendar.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalRepository {
    private static GoalRepository instance;
    private final List<Goal> goals = new ArrayList<>();

    private GoalRepository() {}

    public static synchronized GoalRepository getInstance() {
        if (instance == null) {
            instance = new GoalRepository();
        }
        return instance;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }
}