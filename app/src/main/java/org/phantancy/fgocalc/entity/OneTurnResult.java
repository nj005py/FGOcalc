package org.phantancy.fgocalc.entity;

public class OneTurnResult {
    public double min1 = 0;
    public double min2 = 0;
    public double min3 = 0;
    public double min4 = 0;
    public double sumMin = 0;
    public double max1 = 0;
    public double max2 = 0;
    public double max3 = 0;
    public double max4 = 0;
    public double sumMax = 0;
    public double avg1 = 0;
    public double avg2 = 0;
    public double avg3 = 0;
    public double avg4 = 0;
    public double sumAvg = 0;

    public OneTurnResult(double min1, double min2, double min3, double min4, double sumMin, double max1, double max2, double max3, double max4, double sumMax, double avg1, double avg2, double avg3, double avg4, double sumAvg) {
        this.min1 = min1;
        this.min2 = min2;
        this.min3 = min3;
        this.min4 = min4;
        this.sumMin = sumMin;
        this.max1 = max1;
        this.max2 = max2;
        this.max3 = max3;
        this.max4 = max4;
        this.sumMax = sumMax;
        this.avg1 = avg1;
        this.avg2 = avg2;
        this.avg3 = avg3;
        this.avg4 = avg4;
        this.sumAvg = sumAvg;
    }

    public OneTurnResult() {
    }
}
