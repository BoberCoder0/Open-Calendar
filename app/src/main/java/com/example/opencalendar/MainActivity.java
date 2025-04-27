package com.example.opencalendar;

import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Получаем ссылку на ImageButton из макета по его ID
        ImageButton btn = findViewById(R.id.Account_Button);

// Устанавливаем провайдер для определения формы (outline) кнопки
        btn.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            // Этот метод вызывается для определения формы элемента
            public void getOutline(View view, Outline outline) {
                // Создаем закругленный прямоугольник в качестве формы:
                // Параметры:
                // 1. Левый край (0)
                // 2. Верхний край (0)
                // 3. Правый край (текущая ширина view)
                // 4. Нижний край (текущая высота view)
                // 5. Радиус скругления углов (32 пикселя)
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 320f);
            }
        });

// Включаем обрезку содержимого по установленной форме
// Это означает, что все, что выходит за границы outline, будет обрезано
        btn.setClipToOutline(true);
    }
}