package org.phantancy.fgocalc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.phantancy.fgocalc.common.App;

/**
 * Created by PY on 2017/3/3.
 */
public class ToastUtils {
    public static boolean isDisplayToas = true;


    public static void displayLongToast(Context context, CharSequence content) {
        if (isDisplayToas) {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        }
    }

    public static void displayShortToast(Context context, CharSequence content) {
        if (isDisplayToas) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void displayCustomTimeToast(Context context, CharSequence content, int duration) {
        if (isDisplayToas) {
            Toast.makeText(context, content, duration).show();
        }
    }

    public static void displayCustomToast(Context context, int viewId, CharSequence content, int duration) {

        View view = LayoutInflater.from(context).inflate(viewId, null);
        displayCustomToast(context, view, content, duration);


    }

    private static void displayCustomToast(Context context, View view, CharSequence content, int duration) {
        if (isDisplayToas) {
            Toast toast = new Toast(context);
            toast.setDuration(duration);
            toast.setText(content);
            toast.setView(view);
            toast.show();
        }

    }

    public static void displayCustomToast(Context context, View viewId, CharSequence content) {
        displayCustomToast(context, viewId, content, Toast.LENGTH_LONG);
    }

    public static void showToast(CharSequence content) {
        if (isDisplayToas) {
            Toast.makeText(App.getAppContext(), content, Toast.LENGTH_SHORT).show();
        }
    }
}
