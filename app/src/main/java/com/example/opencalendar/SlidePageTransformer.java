package com.example.opencalendar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class SlidePageTransformer implements ViewPager2.PageTransformer {
    @Override
    /*public void transformPage(@NonNull View page, float position) {
        if (position < -1) { // Страница слева от видимой области
            page.setAlpha(0);
        } else if (position <= 0) { // Страница уходит влево
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) { // Страница появляется справа
            page.setTranslationX(-position * page.getWidth()); // Сдвиг влево
            page.setAlpha(1 - Math.abs(position));
        } else { // Страница справа от видимой области
            page.setAlpha(0);
        }
    }*/
    public void transformPage(@NonNull View page, float position) {
        page.setTranslationX(-position * page.getWidth()); // Упрощенная анимация
        page.setAlpha(1 - Math.abs(position) * 0.3f); // Меньше прозрачности для производительности
    }
}
