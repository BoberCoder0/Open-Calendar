package com.example.opencalendar;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Calendar;

/**
 * Основная Activity приложения, реализующая интерфейс календаря
 * Управляет навигацией между месяцами и согласованным выделением дней
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager2 monthViewPager;
    private MonthPagerAdapter monthPagerAdapter;
    private int selectedDay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация всех компонентов
        initViews();
        setupMonthPager();
        setupAccountButton();

        // Установка текущей даты при запуске
        initializeTodayButton();
        updateDateText(MonthPagerAdapter.INITIAL_POSITION);

        // Подписываемся на события выбора даты
        getSupportFragmentManager().setFragmentResultListener("dateSelected", this,
                (requestKey, result) -> {
                    int year = result.getInt("year");
                    int month = result.getInt("month");
                    int day = result.getInt("day");

                    // Обновляем верхнюю панель
                    TextView yearText = findViewById(R.id.YearText);
                    TextView monthText = findViewById(R.id.MonthText);
                    TextView dayText = findViewById(R.id.DayText);

                    yearText.setText(String.valueOf(year));
                    monthText.setText(String.format(".%02d", month + 1));
                    dayText.setText(String.format(".%02d", day));
                });
    }

    /**
     * Инициализация всех View элементов
     */
    private void initViews() {
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setOnClickListener(v -> goToToday());
    }

    /**
     * Инициализация с сегодняшним днём при запуске
     */
    private void initializeTodayButton() {
        Calendar today = Calendar.getInstance();
        selectedDay = today.get(Calendar.DAY_OF_MONTH);
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setText(String.valueOf(selectedDay));
        monthPagerAdapter.setSelectedDay(selectedDay);
    }

    /**
     * Переход к текущему месяцу и выделение сегодняшнего дня
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
     * Настройка ViewPager для перелистывания месяцев
     */
    private void setupMonthPager() {
        monthPagerAdapter = new MonthPagerAdapter(this);
        monthViewPager = findViewById(R.id.monthViewPager);
        monthViewPager.setAdapter(monthPagerAdapter);

        // Устанавливаем начальную позицию (для бесконечного скролла)
        monthViewPager.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, false);

        // Добавляем анимацию перелистывания страниц
        monthViewPager.setPageTransformer(new SlidePageTransformer());

        // Обработчик изменения страницы (для обновления даты в заголовке)
        monthViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDateText(position);
            }
        });

        // Оптимизация анимации и скролла
        monthViewPager.post(() -> {
            RecyclerView recyclerView = (RecyclerView) monthViewPager.getChildAt(0);
            recyclerView.setItemAnimator(null);  // Отключаем анимацию элементов
            recyclerView.setLayoutFrozen(false);  // Разрешаем изменение layout
            // Устанавливаем параметры скролла
            recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        });
    }

    /**
     * Обновление текстовых полей с датой при перелистывании месяцев
     * @param position Текущая позиция во ViewPager
     */
    private void updateDateText(int position) {
        // Вычисляем смещение от текущего месяца
        int monthOffset = position - MonthPagerAdapter.INITIAL_POSITION;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);

        // Обновляем текстовые поля
        TextView yearText = findViewById(R.id.YearText);
        TextView monthText = findViewById(R.id.MonthText);
        TextView dayText = findViewById(R.id.DayText);

        yearText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthText.setText(String.format(".%02d", calendar.get(Calendar.MONTH) + 1));
        dayText.setText(String.format(".%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    /**
     * Настройка кнопки аккаунта (закругление и обработчик клика)
     * Требует API Level 21+ для ViewOutlineProvider
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupAccountButton() {
        ImageButton accountButton = findViewById(R.id.Account_Button);
        // Создаем закругленные края для кнопки
        accountButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Устанавливаем закругленный прямоугольник с радиусом 32f
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 32f);
            }
        });
        accountButton.setClipToOutline(true);  // Включаем обрезку по контуру

        // Обработчики кликов
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
     * Добавление нового события (заглушка)
     */
    private void addEvent() {
        // TODO: Реализовать добавление события
    }
}
