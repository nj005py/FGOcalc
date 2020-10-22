package org.phantancy.fgocalc.entity;

public class FilterEntity {

    private String title;
    private int selectionPosition = 0;
    private boolean reset;
    /**
     * 数据类型
     * 0:String
     * 1:int
     */
    private int dataType;
    //选项提示
    private String[] hint;
    //String类型
    private String[] data0;
    private String value0;
    //int类型
    private int[] data1;
    private int value1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectionPosition() {
        return selectionPosition;
    }

    public void setSelectionPosition(int selectionPosition) {
        this.selectionPosition = selectionPosition;
        switch (dataType) {
            case 0:
                value0 = data0[selectionPosition];
                break;
            case 1:
                value1 = data1[selectionPosition];
                break;
        }
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String[] getHint() {
        return hint;
    }

    public void setHint(String[] hint) {
        this.hint = hint;
    }

    public String[] getData0() {
        return data0;
    }

    public void setData0(String[] data0) {
        this.data0 = data0;
    }

    public String getValue0() {
        return value0;
    }

    public void setValue0(String value0) {
        this.value0 = value0;
    }

    public int[] getData1() {
        return data1;
    }

    public void setData1(int[] data1) {
        this.data1 = data1;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
