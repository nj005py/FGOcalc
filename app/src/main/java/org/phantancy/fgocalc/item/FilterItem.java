package org.phantancy.fgocalc.item;

import java.io.Serializable;

public class FilterItem implements Serializable {
    private String title;
    private String[] options;
    private String[] valuesString;
    private int[] valuesInt;
    private int type = 0;//0:String[] 1:int[]
    private boolean reset = false;
    private String valueString;
    private int valueInt;

    public FilterItem(String title, String[] options, String[] valuesString) {
        this.title = title;
        this.options = options;
        this.valuesString = valuesString;
    }

    public FilterItem(String title, String[] options, int[] valuesInt, int type) {
        this.title = title;
        this.options = options;
        this.valuesInt = valuesInt;
        this.type = type;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public String[] getValuesString() {
        return valuesString;
    }

    public void setValuesString(String[] valuesString) {
        this.valuesString = valuesString;
    }

    public int[] getValuesInt() {
        return valuesInt;
    }

    public void setValuesInt(int[] valuesInt) {
        this.valuesInt = valuesInt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
