package org.phantancy.fgocalc.entity;

import java.io.Serializable;

public class FetchEntity implements Serializable {
    private String name;
    private double value;
    private String display;

    public FetchEntity(String name, double value, String display) {
        this.name = name;
        this.value = value;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
