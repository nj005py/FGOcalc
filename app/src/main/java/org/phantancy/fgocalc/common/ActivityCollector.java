package org.phantancy.fgocalc.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HATTER on 2017/11/2.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActy(Activity activity){
        activities.add(activity);
    }

    public static void removeActy(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity :
                activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
