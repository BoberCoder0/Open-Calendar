package com.example.opencalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        goals.add(new Goal("Цель 1", "Описание цели 1"));
        goals.add(new Goal("Цель 2", "Длинное описание цели 2, которое будет скрыто"));
        adapter.notifyDataSetChanged();

        return view;
    }

    private void showAddGoalDialog() {
        AddGoalDialog dialog = new AddGoalDialog();
        dialog.setListener(this);
        dialog.show(getParentFragmentManager(), "AddGoalDialog");
    }

    @Override
    public void onGoalAdded(String title, String description) {
        Goal goal = new Goal(title, description);
        goals.add(0, goal);
        adapter.notifyItemInserted(0);
    }
}