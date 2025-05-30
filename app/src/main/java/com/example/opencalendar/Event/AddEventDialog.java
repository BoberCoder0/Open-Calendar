package com.example.opencalendar.Event;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.opencalendar.Month.DayScheduleFragment;
import com.example.opencalendar.R;


public class AddEventDialog extends DialogFragment {

    private TimePicker startTimePicker, endTimePicker;
    private Spinner repeatSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_event, container, false);

        startTimePicker = view.findViewById(R.id.startTimePicker);
        endTimePicker = view.findViewById(R.id.endTimePicker);
        repeatSpinner = view.findViewById(R.id.repeatSpinner);

        // Настройка спиннера повторения
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.repeat_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);

        view.findViewById(R.id.btnSave).setOnClickListener(v -> saveEvent());

        return view;
    }

    private void saveEvent() {
        Event event = new Event();
        event.setTitle(((EditText) getView().findViewById(R.id.etTitle)).getText().toString());
        event.setDescription(((EditText) getView().findViewById(R.id.etDescription)).getText().toString());

        // Время начала
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            event.setStartHour(startTimePicker.getHour());
            event.setStartMinute(startTimePicker.getMinute());
        } else {
            event.setStartHour(startTimePicker.getCurrentHour());
            event.setStartMinute(startTimePicker.getCurrentMinute());
        }

        // Время окончания
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            event.setEndHour(endTimePicker.getHour());
            event.setEndMinute(endTimePicker.getMinute());
        } else {
            event.setEndHour(endTimePicker.getCurrentHour());
            event.setEndMinute(endTimePicker.getCurrentMinute());
        }

        // Повторение
        event.setRepeatMode(repeatSpinner.getSelectedItemPosition());

        // Передаем событие в фрагмент графика
        DayScheduleFragment scheduleFragment = (DayScheduleFragment) getParentFragmentManager()
                .findFragmentById(R.id.contentContainer);

        if (scheduleFragment != null) {
            scheduleFragment.addEventToSchedule(event);
        }

        dismiss();
    }
}