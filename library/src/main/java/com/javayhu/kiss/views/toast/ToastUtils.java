package com.javayhu.kiss.views.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javayhu.kiss.views.R;

/**
 * This file is part of Toasty.
 * <p>
 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * The usual Toast, but with steroids
 * <p>
 * source code: https://github.com/GrenderG/Toasty
 * <p>
 * updates：1.将Toast的icon由不同大小的png图片换成vector drawable
 * <p>
 * updated by javayhu on 2017/3/6.
 * created by GrenderG on 2017.
 */
@SuppressLint("InflateParams")
public class ToastUtils {

    //@formatter:off
    private static final @ColorInt int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    private static final @ColorInt int ERROR_COLOR = Color.parseColor("#D50000");
    private static final @ColorInt int INFO_COLOR = Color.parseColor("#3F51B5");
    private static final @ColorInt int SUCCESS_COLOR = Color.parseColor("#388E3C");
    private static final @ColorInt int WARNING_COLOR = Color.parseColor("#FFA900");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private ToastUtils() {
    }

    public static @CheckResult Toast normal(@NonNull Context context, @NonNull String message) {
        return normal(context, message, Toast.LENGTH_SHORT, null, false);
    }

    public static @CheckResult Toast normal(@NonNull Context context, @NonNull String message, Drawable icon) {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true);
    }

    public static @CheckResult Toast normal(@NonNull Context context, @NonNull String message, int duration) {
        return normal(context, message, duration, null, false);
    }

    public static @CheckResult Toast normal(@NonNull Context context, @NonNull String message, int duration,
                                            Drawable icon) {
        return normal(context, message, duration, icon, true);
    }

    public static @CheckResult Toast normal(@NonNull Context context, @NonNull String message, int duration,
                                            Drawable icon, boolean withIcon) {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon);
    }

    public static @CheckResult Toast warning(@NonNull Context context, @NonNull String message) {
        return warning(context, message, Toast.LENGTH_SHORT, true);
    }

    public static @CheckResult Toast warning(@NonNull Context context, @NonNull String message, int duration) {
        return warning(context, message, duration, true);
    }

    public static @CheckResult Toast warning(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.drawable.ic_error_outline),
                DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true);
    }

    public static @CheckResult Toast info(@NonNull Context context, @NonNull String message) {
        return info(context, message, Toast.LENGTH_SHORT, true);
    }

    public static @CheckResult Toast info(@NonNull Context context, @NonNull String message, int duration) {
        return info(context, message, duration, true);
    }

    public static @CheckResult Toast info(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.drawable.ic_info_outline),
                DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true);
    }

    public static @CheckResult Toast success(@NonNull Context context, @NonNull String message) {
        return success(context, message, Toast.LENGTH_SHORT, true);
    }

    public static @CheckResult Toast success(@NonNull Context context, @NonNull String message, int duration) {
        return success(context, message, duration, true);
    }

    public static @CheckResult Toast success(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.drawable.ic_check),
                DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true);
    }

    public static @CheckResult Toast error(@NonNull Context context, @NonNull String message) {
        return error(context, message, Toast.LENGTH_SHORT, true);
    }

    public static @CheckResult Toast error(@NonNull Context context, @NonNull String message, int duration) {
        return error(context, message, duration, true);
    }

    public static @CheckResult Toast error(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.drawable.ic_close),
                DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true);
    }

    public static @CheckResult Toast custom(@NonNull Context context, @NonNull String message, Drawable icon,
                                            @ColorInt int textColor, int duration, boolean withIcon) {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false);
    }

    public static @CheckResult Toast custom(@NonNull Context context, @NonNull String message, @DrawableRes int iconRes,
                                            @ColorInt int textColor, @ColorInt int tintColor, int duration,
                                            boolean withIcon, boolean shouldTint) {
        return custom(context, message, getDrawable(context, iconRes), textColor,
                tintColor, duration, withIcon, shouldTint);
    }

    public static @CheckResult Toast custom(@NonNull Context context, @NonNull String message, Drawable icon,
                                            @ColorInt int textColor, @ColorInt int tintColor, int duration,
                                            boolean withIcon, boolean shouldTint) {
        final Toast currentToast = new Toast(context);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint)
            drawableFrame = tint9PatchDrawableFrame(context, tintColor);
        else
            drawableFrame = getDrawable(context, R.drawable.toast_frame);
        setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            setBackground(toastIcon, icon);
        } else
            toastIcon.setVisibility(View.GONE);

        toastTextView.setTextColor(textColor);
        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        return currentToast;
    }
    //@formatter:on

    private static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.drawable.toast_frame);
        toastDrawable.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        return toastDrawable;
    }

    private static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(id);
        else
            return context.getResources().getDrawable(id);
    }
}
