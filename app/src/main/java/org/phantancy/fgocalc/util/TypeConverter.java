package org.phantancy.fgocalc.util;

public class TypeConverter {
    public static String doubleToString(double x) {
        try {
            return String.valueOf(x);
        } catch (Exception e) {
            return "";
        }
    }
}
