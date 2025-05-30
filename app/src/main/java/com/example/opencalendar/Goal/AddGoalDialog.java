package com.example.opencalendar.Goal;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.opencalendar.R;

import java.util.Locale;

public class AddGoalDialog extends DialogFragment {
    private AddGoalDialogListener listener;
    private Calendar selectedDate;
    private int selectedColor = Color.BLUE; // Цвет по умолчанию

    // Обновленный интерфейс
    public interface AddGoalDialogListener {
        void onGoalAdded(String title, String description, Calendar date, int color);
    }

    public void setListener(AddGoalDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_goal, container, false);

        EditText titleEdit = view.findViewById(R.id.goal_title_edit);
        EditText descEdit = view.findViewById(R.id.goal_description_edit);
        Button dateButton = view.findViewById(R.id.dateButton);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);

        // Инициализация даты текущей датой
        selectedDate = Calendar.getInstance();
        updateDateButton(dateButton);
        View colorPreview = view.findViewById(R.id.colorPreview);
        Button colorButton = view.findViewById(R.id.colorButton);

        // Инициализация превью цвета
        colorPreview.setBackgroundColor(selectedColor);

        colorButton.setOnClickListener(v -> showColorGridDialog(colorPreview));

        dateButton.setOnClickListener(v -> showDatePickerDialog(dateButton));

        cancelButton.setOnClickListener(v -> dismiss());

        addButton.setOnClickListener(v -> {
            String title = titleEdit.getText().toString();
            String description = descEdit.getText().toString();

            if (!title.trim().isEmpty()) {
                if (listener != null) {
                    // Только один вызов с цветом
                    listener.onGoalAdded(title, description, selectedDate, selectedColor);
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "Введите название цели", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showColorGridDialog(View colorPreview) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выберите цвет");

        // Создаем сетку 6x5 цветов
        GridView gridView = new GridView(getContext());
        gridView.setNumColumns(6);
        gridView.setAdapter(new ColorAdapter(generateColorPalette()));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            selectedColor = (int) parent.getItemAtPosition(position);
            colorPreview.setBackgroundColor(selectedColor);
            ((AlertDialog) builder.create()).dismiss();
        });

        builder.setView(gridView);
        builder.show();
    }

    private int[] generateColorPalette() {
        int[] colors = new int[30];
        float h = 0;
        for (int i = 0; i < 30; i++) {
            colors[i] = Color.HSVToColor(new float[]{h, 0.8f, 0.9f});
            h += 12; // Шаг 12 градусов
        }
        return colors;
    }

    private int generateRandomColor() {
        return Color.rgb(
                (int)(Math.random() * 256),
                (int)(Math.random() * 256),
                (int)(Math.random() * 256)
        );
    }

    private void showDatePickerDialog(Button dateButton) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    updateDateButton(dateButton);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void updateDateButton(Button button) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        button.setText(sdf.format(selectedDate.getTime()));
    }

    private static class ColorAdapter extends BaseAdapter {
        private final int[] colors;

        public ColorAdapter(int[] colors) {
            this.colors = colors;
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = new View(parent.getContext());
            view.setLayoutParams(new GridView.LayoutParams(100, 100));
            view.setBackgroundColor(colors[position]);
            return view;
        }
    }
}