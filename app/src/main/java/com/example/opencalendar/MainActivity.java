package com.example.opencalendar;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;

import java.util.Calendar;


/**
 * Главная Activity приложения, реализующая календарь с возможностью
 * перелистывания месяцев и отображения событий
 */
public class MainActivity extends AppCompatActivity {
    // UI элементы
    private ViewPager2 monthViewPager;  // Для горизонтального перелистывания месяцев
    private MonthPagerAdapter monthPagerAdapter;  // Адаптер для ViewPager2
    private TextView yearText, monthText, dayText;  // Текстовые поля для отображения даты

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Сделать статус бар белым с темными иконками
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        // Инициализация всех компонентов
        initViews();
        setupMonthPager();
        setupAccountButton();

        // Установка текущей даты при запуске
        Calendar today = Calendar.getInstance();
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setText(String.valueOf(today.get(Calendar.DAY_OF_MONTH)));

        updateDateText(MonthPagerAdapter.INITIAL_POSITION);

        // Подписываемся на события выбора даты
        getSupportFragmentManager().setFragmentResultListener("dateSelected", this, (requestKey, result) -> {
            int year = result.getInt("year");
            int month = result.getInt("month");
            int day = result.getInt("day");

            // Обновляем верхнюю панель
            yearText.setText(String.valueOf(year));
            monthText.setText(String.format(".%02d", month + 1));
            dayText.setText(String.format(".%02d", day));
        });

        //setupBottomTabs();
        setupBottomTabs(savedInstanceState);
    }

    private void setupBottomTabs(Bundle savedInstanceState) {
        Button btnDaySchedule = findViewById(R.id.btnDaySchedule);
        Button btnGoals = findViewById(R.id.btnGoals);

        btnDaySchedule.setOnClickListener(v -> showDaySchedule());
        btnGoals.setOnClickListener(v -> showGoals());

        if (savedInstanceState == null) {
            showDaySchedule();
        }
    }

    // Добавляем сохранение состояния
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Сохраняем текущий фрагмент
    }

    private void showDaySchedule() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, new DayScheduleFragment())
                .commit();

        // Настраиваем поведение скроллинга
        setupScrollingBehavior(true);
    }

    private void showGoals() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, new GoalsFragment())
                .commit();

        // Настраиваем поведение скроллинга
        setupScrollingBehavior(false);
    }

    private void setupScrollingBehavior(boolean enableNestedScrolling) {
        View contentContainer = findViewById(R.id.contentContainer);
        if (contentContainer instanceof NestedScrollView) {
            ((NestedScrollView) contentContainer).setNestedScrollingEnabled(enableNestedScrolling);
        } else if (contentContainer instanceof RecyclerView) {
            ((RecyclerView) contentContainer).setNestedScrollingEnabled(enableNestedScrolling);
        }
    }

    /**
     * Инициализация всех View элементов
     */
    private void initViews() {
        yearText = findViewById(R.id.YearText);  // Поле года (например: 2023)
        monthText = findViewById(R.id.MonthText);  // Поле месяца (например: .04)
        dayText = findViewById(R.id.DayText);  // Поле дня (например: .27)

        monthViewPager = findViewById(R.id.monthViewPager);  // ViewPager для месяцев

        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setOnClickListener(v -> goToToday());
    }

    // Новый метод для перехода к текущей дате
    private void goToToday() {
        // Устанавливаем текущую дату в кнопке
        Calendar today = Calendar.getInstance();
        Button goTodayButton = findViewById(R.id.GoTodayButton);
        goTodayButton.setText(String.valueOf(today.get(Calendar.DAY_OF_MONTH)));

        // Вычисляем позицию для текущего месяца
        int currentPosition = MonthPagerAdapter.INITIAL_POSITION;
        monthViewPager.setCurrentItem(currentPosition, true);

        // Обновляем отображение даты
        updateDateText(currentPosition);
    }

    /**
     * Настройка ViewPager для перелистывания месяцев
     */
    private void setupMonthPager() {
        // Создаем адаптер, который будет создавать фрагменты месяцев
        monthPagerAdapter = new MonthPagerAdapter(this);
        monthViewPager.setAdapter(monthPagerAdapter);

        // Устанавливаем начальную позицию (для бесконечного скролла)
        //monthViewPager.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, false);

        //Замените DepthPageTransformer на новый:
        monthViewPager.setPageTransformer(new SlidePageTransformer());

        // Устанавливаем позицию после инициализации
        monthViewPager.post(() -> {
            monthViewPager.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, false);
            updateDateText(MonthPagerAdapter.INITIAL_POSITION);
        });

        /*// Добавляем анимацию перелистывания страниц
        monthViewPager.setPageTransformer(new DepthPageTransformer());*/

        // Обработчик изменения страницы (для обновления даты в заголовке)
        monthViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDateText(position);
            }
        });

        // Оптимизация анимации и скролла
        /*monthViewPager.post(() -> {
            RecyclerView recyclerView = (RecyclerView) monthViewPager.getChildAt(0);
            recyclerView.setItemAnimator(null);  // Отключаем анимацию элементов
            recyclerView.setLayoutFrozen(false);  // Разрешаем изменение layout
            // Устанавливаем параметры скролла
            recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        });*/
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
        yearText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthText.setText(String.format(".%02d", calendar.get(Calendar.MONTH) + 1));
        dayText.setText(String.format(".%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        // Если день не был выбран вручную, показываем текущий день
        if (dayText.getText().toString().isEmpty()) {
            dayText.setText(String.format(".%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        }
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
        //findViewById(R.id.imageButton).setOnClickListener(v -> addEvent());

        findViewById(R.id.imageButton).setOnClickListener(v -> {
            DayScheduleFragment scheduleFragment = (DayScheduleFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentContainer);

            if (scheduleFragment != null) {
                scheduleFragment.showEventTypeDialog();
            }
        });

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

    public AppBarLayout getAppBarLayout() {
        return findViewById(R.id.main); // Добавьте ID для AppBarLayout
    }
}



/**
 * Класс для анимации перелистывания страниц в ViewPager2
 * Создает эффект "глубины" при перелистывании
 */
/*
class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.75f;  // Минимальный масштаб страницы

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // Страница слева от видимой области
            view.setAlpha(0f);
        } else if (position <= 0) { // Страница уходит влево
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1) { // Страница появляется справа
            view.setAlpha(1 - position);  // Прозрачность
            view.setTranslationX(pageWidth * -position);  // Смещение
            // Масштабирование
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else { // Страница справа от видимой области
            view.setAlpha(0f);
        }
    }
}*/
