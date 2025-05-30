package com.example.opencalendar;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;
import java.util.Calendar;

public class DayScheduleFragment extends Fragment {

    private static final int HOUR_HEIGHT = 120; // Высота 1 часа в dp
    private final Handler handler = new Handler();
    // Замените поиск по ID на сохранение в переменную класса:
    private View currentTimeLine;
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateCurrentTimeLine();
            handler.postDelayed(this, 60000); // Обновлять каждую минуту
        }
    };

    private void updateCurrentTimeLine() {
        Calendar now = Calendar.getInstance();
        int minutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
        int topMargin = (int) (minutes * dpToPx(HOUR_HEIGHT) / 60f); // Исправленная строка

        // Используйте переменную класса вместо поиска по ID
        // View currentTimeLine = getView().findViewById(R.id.currentTimeLine);
        if (currentTimeLine != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) currentTimeLine.getLayoutParams();
            params.topMargin = topMargin;
            currentTimeLine.setLayoutParams(params);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_schedule, container, false);

        LinearLayout timeScale = view.findViewById(R.id.timeScale);
        FrameLayout eventsContainer = view.findViewById(R.id.eventsContainer);

        // Создаем временную шкалу
        for (int hour = 0; hour < 24; hour++) {
            TextView hourView = new TextView(getContext());
            hourView.setText(String.format("%02d:00", hour));
            // Исправление здесь: убраны лишние скобки
            hourView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(HOUR_HEIGHT) // Исправлено
            ));
            hourView.setGravity(Gravity.CENTER);
            timeScale.addView(hourView);
        }

        // Добавляем текущее время (красная линия)
        //View currentTimeLine = new View(getContext());
        currentTimeLine = new View(getContext());
        currentTimeLine.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                2
        );
        Calendar now = Calendar.getInstance();
        int minutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
        params.topMargin = (int) (minutes * dpToPx(HOUR_HEIGHT) / 60f);
        currentTimeLine.setLayoutParams(params);
        eventsContainer.addView(currentTimeLine);
        //currentTimeLine.setId(R.id.currentTimeLine);

        // Обработчик для создания событий
        view.findViewById(R.id.eventsContainer).setOnClickListener(v -> {
            showEventTypeDialog();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Убрана обработка скроллинга, так как она теперь управляется CoordinatorLayout
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void showEventTypeDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Создать")
                .setItems(new String[]{"Событие", "Цель"}, (dialog, which) -> {
                    if (which == 0) {
                        showAddEventDialog();
                    } else {
                        showAddGoalDialog();
                    }
                })
                .show();
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        dialog.show(getParentFragmentManager(), "AddEventDialog");
    }

    private void showAddGoalDialog() {
        AddGoalDialog dialog = new AddGoalDialog();
        dialog.show(getParentFragmentManager(), "AddGoalDialog");
    }

    public void addEventToSchedule(Event event) {
        View eventView = new View(getContext());
        eventView.setBackgroundColor(Color.parseColor("#4285F4"));

        int startMinutes = event.getStartHour() * 60 + event.getStartMinute();
        int endMinutes = event.getEndHour() * 60 + event.getEndMinute();
        int height = (endMinutes - startMinutes) * dpToPx(HOUR_HEIGHT) / 60;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                height
        );
        params.topMargin = startMinutes * dpToPx(HOUR_HEIGHT) / 60;
        eventView.setLayoutParams(params);

        eventView.setOnClickListener(v -> showEventDetails(event));

        View eventsContainer = getView().findViewById(R.id.eventsContainer);
        ((FrameLayout) eventsContainer).addView(eventView);
    }

    private void showEventDetails(Event event) {
        EventDetailsFragment fragment = EventDetailsFragment.newInstance(event);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}