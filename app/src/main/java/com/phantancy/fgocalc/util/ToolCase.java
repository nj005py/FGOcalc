package com.phantancy.fgocalc.util;

import android.widget.EditText;

/**
 * Created by PY on 2017/2/7.
 */
public class ToolCase {
    public static boolean notEmpty(String v){
        if (v != null && !v.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    public static String etValue(EditText et){
        if (et.getText() == null) {
            return "";
        }else{
            return et.getText().toString().trim();
        }
    }
}
