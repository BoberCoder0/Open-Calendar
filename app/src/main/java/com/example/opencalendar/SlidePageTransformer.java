// SlidePageTransformer.java
package com.example.opencalendar;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Кастомный трансформер страниц для ViewPager2.
 * Реализует анимацию при перелистывании месяцев:
 * - Страницы слева и справа становятся прозрачными
 * - Страницы сдвигаются при перелистывании
 */
public class SlidePageTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            // Страница полностью слева - невидима
            page.setAlpha(0);
        } else if (position <= 0) {
            // Страница уходит влево - без изменений
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) {
            // Страница появляется справа - сдвиг и изменение прозрачности
            page.setTranslationX(-position * page.getWidth()); // Сдвиг влево
            page.setAlpha(1 - Math.abs(position)); // Уменьшение прозрачности
        } else {
            // Страница полностью справа - невидима
            page.setAlpha(0);
        }
    }
}