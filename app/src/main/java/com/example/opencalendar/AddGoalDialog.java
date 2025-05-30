package com.example.opencalendar;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddGoalDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Замените на вашу разметку для целей
        return inflater.inflate(R.layout.dialog_add_goal, container, false);
    }
}