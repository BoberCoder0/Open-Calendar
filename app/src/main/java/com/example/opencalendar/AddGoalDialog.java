package com.example.opencalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddGoalDialog extends DialogFragment {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private AddGoalDialogListener listener;

    public interface AddGoalDialogListener {
        void onGoalAdded(String title, String description);
    }

    public void setListener(AddGoalDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_goal, container, false);

        titleEditText = view.findViewById(R.id.goal_title_edit);
        descriptionEditText = view.findViewById(R.id.goal_description_edit);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dismiss());
        addButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (!title.isEmpty()) {
                if (listener != null) {
                    listener.onGoalAdded(title, description);
                }
                dismiss();
            } else {
                titleEditText.setError("Введите название цели");
            }
        });

        return view;
    }
}