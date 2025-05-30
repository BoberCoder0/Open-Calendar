package com.example.opencalendar.Goal;

import java.util.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment implements AddGoalDialog.AddGoalDialogListener {

    private GoalsAdapter adapter;
    private List<Goal> goals = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GoalsAdapter(goals);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.add_goal_fab);
        fab.setOnClickListener(v -> showAddGoalDialog());

        // Тестовые данные
        goals.add(new Goal("Цель 1", "Описание цели 1", Color.BLUE));
        goals.add(new Goal("Цель 2", "Длинное описание цели 2, которое будет скрыто", Color.RED));
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onGoalAdded(String title, String description, Calendar date, int color) {
        Goal goal = new Goal(title, description, color);
        goal.setDate(date);
        goals.add(0, goal);
        adapter.notifyItemInserted(0);
    }

    private void showAddGoalDialog() {
        AddGoalDialog dialog = new AddGoalDialog();
        dialog.setListener(this);
        dialog.show(getParentFragmentManager(), "AddGoalDialog");
    }
}