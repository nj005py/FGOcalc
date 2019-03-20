package org.phantancy.fgocalc.item;

import java.io.Serializable;

public class CurveItem implements Serializable {
    private int curve;

    public int getCurve() {
        return curve;
    }

    public void setCurve(int curve) {
        this.curve = curve;
    }
}
