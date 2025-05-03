// MainActivity.java
package com.example.opencalendar;

// Импорты необходимых библиотек
import android.app.DatePickerDialog;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Главная активность приложения, содержащая:
 * - ViewPager2 для перелистывания месяцев
 * - RecyclerView для отображения целей
 * - Кнопки управления и элементы интерфейса
 */
public class MainActivity extends AppCompatActivity {
    // Поля класса
    private ViewPager2 monthViewPager; // Для горизонтального пролистывания месяцев
    private MonthPagerAdapter monthPagerAdapter; // Адаптер для ViewPager
    private int selectedDay = -1; // Текущий выбранный день (-1 означает отсутствие выбора)
    private NestedScrollView nestedScrollView; // Основной контейнер с прокруткой
    private RecyclerView goalsRecyclerView; // Список целей
    private GoalsAdapter goalsAdapter; // Адаптер для списка целей
    private AppDatabase database; // База данных Room
    private GoalDao goalDao; // DAO для работы с целями

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Установка макета

        // Инициализация компонентов
        initViews();
        setupMonthPager();
        setupAccountButton();
        initializeTodayButton();
        updateDateText(MonthPagerAdapter.INITIAL_POSITION);

        // Работа с базой данных
        database = AppDatabaseHelper.getInstance(this);
        goalDao = database.goalDao();

        // Обработчик кнопки добавления цели
        findViewById(R.id.imageButton).setOnClickListener(v -> showAddGoalDialog());

        // Слушатель выбора даты из фрагмента
        getSupportFragmentManager().setFragmentResultListener("dateSelected", this,
                (requestKey, result) -> {
                    // Обновление текстовых полей при выборе даты
                    int year = result.getInt("year");
                    int month = result.getInt("month");
                    int day = result.getInt("day");

                    TextView yearText = findViewById(R.id.YearText);
                    TextView monthText = findViewById(R.id.MonthText);
                    TextView dayText = findViewById(R.id.DayText);

                    yearText.setText(String.valueOf(year));
                    monthText.setText(String.format(".%02d", month + 1));
                    dayText.setText(String.format(".%02d", day));
                });
    }

    /**
     * Показывает диалог для добавления новой цели
     */
    private void showAddGoalDialog() {
        // Создание и настройка диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_goal, null);

        // Получение ссылок на элементы диалога
        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText descInput = dialogView.findViewById(R.id.descInput);
        Button dateButton = dialogView.findViewById(R.id.dateButton);

        Calendar selectedDate = Calendar.getInstance(); // Дата по умолчанию - текущая

        // Обработчик выбора даты
        dateButton.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate.set(year, month, day);
                dateButton.setText(String.format("%d.%d.%d", day, month + 1, year));
            },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        // Настройка кнопок диалога
        builder.setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    // Создание новой цели
                    Goal goal = new Goal();
                    goal.setTitle(titleInput.getText().toString());
                    goal.setDescription(descInput.getText().toString());
                    goal.setTargetDate(selectedDate);

                    // Сохранение в БД в фоновом потоке
                    new Thread(() -> {
                        goalDao.insert(goal);
                        runOnUiThread(() -> {
                            // Обновление списка на UI
                            goalsAdapter.updateList(goalDao.getAllGoals());
                        });
                    }).start();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    /**
     * Инициализация View элементов
     */
    private void initViews() {
        // Кнопка перехода на сегодня
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setOnClickListener(v -> goToToday());

        // Настройка RecyclerView для целей
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalsAdapter = new GoalsAdapter(new ArrayList<>(), this);
        goalsRecyclerView.setAdapter(goalsAdapter);

        // Обработчик прокрутки для скрытия/показа ViewPager
        nestedScrollView = findViewById(R.id.main);
        nestedScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener)
                        (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                            if (scrollY > monthViewPager.getHeight()) {
                                monthViewPager.setVisibility(View.GONE);
                            } else {
                                monthViewPager.setVisibility(View.VISIBLE);
                            }
                        }
        );
    }

    /**
     * Устанавливает текущий день на кнопке "Сегодня"
     */
    private void initializeTodayButton() {
        Calendar today = Calendar.getInstance();
        selectedDay = today.get(Calendar.DAY_OF_MONTH);
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setText(String.valueOf(selectedDay));
        monthPagerAdapter.setSelectedDay(selectedDay);
    }

    /**
     * Переход к текущему дню
     */
    private void goToToday() {
        Calendar today = Calendar.getInstance();
        selectedDay = today.get(Calendar.DAY_OF_MONTH);
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setText(String.valueOf(selectedDay));

        monthPagerAdapter.setSelectedDay(selectedDay);
        monthViewPager.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, true);
    }

    /**
     * Настройка ViewPager для месяцев
     */
    private void setupMonthPager() {
        monthPagerAdapter = new MonthPagerAdapter(this);
        monthViewPager = findViewById(R.id.monthViewPager);
        monthViewPager.setAdapter(monthPagerAdapter);

        monthViewPager.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, false);
        monthViewPager.setPageTransformer(new SlidePageTransformer());

        // Слушатель изменения страницы
        monthViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDateText(position);
            }
        });

        // Оптимизация RecyclerView внутри ViewPager2
        monthViewPager.post(() -> {
            RecyclerView recyclerView = (RecyclerView) monthViewPager.getChildAt(0);
            recyclerView.setItemAnimator(null);
            recyclerView.setLayoutFrozen(false);
            recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        });
    }

    /**
     * Обновление отображаемой даты при перелистывании
     */
    private void updateDateText(int position) {
        int monthOffset = position - MonthPagerAdapter.INITIAL_POSITION;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        // Обновление текстовых полей
        TextView yearText = findViewById(R.id.YearText);
        TextView monthText = findViewById(R.id.MonthText);
        TextView dayText = findViewById(R.id.DayText);

        yearText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthText.setText(String.format(".%02d", calendar.get(Calendar.MONTH) + 1));
        dayText.setText(String.format(".%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    /**
     * Настройка кнопки аккаунта (скругление изображения)
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupAccountButton() {
        ImageButton accountButton = findViewById(R.id.Account_Button);
        accountButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 32f);
            }
        });
        accountButton.setClipToOutline(true);

        accountButton.setOnClickListener(v -> openAccount());
        findViewById(R.id.imageButton).setOnClickListener(v -> addEvent());
    }

    /**
     * Открытие экрана аккаунта (заглушка)
     */
    private void openAccount() {
        // TODO: Реализовать открытие экрана аккаунта
    }

    /**
     * Добавление события (заглушка)
     */
    private void addEvent() {
        // TODO: Реализовать добавление события
    }
}