package com.javayhu.kiss.views.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * BadgeView以粒子形式消失的动画 (1-2)
 * <p>
 * https://github.com/qstumn/BadgeView
 */
public class BadgeAnimator extends ValueAnimator {
    private BitmapFragment[][] mFragments;
    private WeakReference<DraggableBadgeView> mWeakBadge;

    private BadgeAnimator(DraggableBadgeView badge) {
        mWeakBadge = new WeakReference<>(badge);
    }

    public static BadgeAnimator start(Bitmap badgeBitmap, PointF center, DraggableBadgeView badge) {
        final BadgeAnimator badgeAnimator = new BadgeAnimator(badge);
        badgeAnimator.setFloatValues(0f, 1f);
        badgeAnimator.setDuration(500);
        badgeAnimator.mFragments = badgeAnimator.getFragments(badgeBitmap, center);
        badgeAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DraggableBadgeView badgeView = badgeAnimator.mWeakBadge.get();
                if (badgeView == null || !badgeView.isShown()) {
                    badgeAnimator.end();
                } else {
                    badgeView.invalidate();
                }
            }
        });
        badgeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                DraggableBadgeView badgeView = badgeAnimator.mWeakBadge.get();
                if (badgeView != null) {
                    badgeView.reset();
                }
            }
        });
        badgeAnimator.start();
        return badgeAnimator;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < mFragments.length; i++) {
            for (int j = 0; j < mFragments[i].length; j++) {
                BitmapFragment bf = mFragments[i][j];
                float value = Float.parseFloat(getAnimatedValue().toString());
                bf.updata(value, canvas);
            }
        }
    }

    private BitmapFragment[][] getFragments(Bitmap badgeBitmap, PointF center) {
        int width = badgeBitmap.getWidth();
        int height = badgeBitmap.getHeight();
        float fragmentSize = Math.max(width, height) / 6f;
        float startX = center.x - badgeBitmap.getWidth() / 2f;
        float startY = center.y - badgeBitmap.getHeight() / 2f;
        BitmapFragment[][] fragments = new BitmapFragment[(int) (height / fragmentSize)][(int) (width / fragmentSize)];
        for (int i = 0; i < fragments.length; i++) {
            for (int j = 0; j < fragments[i].length; j++) {
                BitmapFragment bf = new BitmapFragment();
                bf.color = badgeBitmap.getPixel((int) (j * fragmentSize), (int) (i * fragmentSize));
                bf.x = startX + j * fragmentSize;
                bf.y = startY + i * fragmentSize;
                bf.size = fragmentSize;
                bf.maxSize = Math.max(width, height);
                fragments[i][j] = bf;
            }
        }
        badgeBitmap.recycle();
        return fragments;
    }

    private class BitmapFragment {
        Random random;
        float x;
        float y;
        float size;
        int color;
        int maxSize;
        Paint paint;

        public BitmapFragment() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            random = new Random();
        }

        public void updata(float value, Canvas canvas) {
            paint.setColor(color);
            x = x + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            y = y + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            canvas.drawCircle(x, y, size - value * size, paint);
        }
    }
}