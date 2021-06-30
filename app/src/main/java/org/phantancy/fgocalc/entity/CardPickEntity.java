package org.phantancy.fgocalc.entity;

public class CardPickEntity {
    private int id;
    private String name;
    private int img;
    private boolean isVisible = true;
    //归属
    private int svtSource;
    //从者头像
    private int svtAvatar;
    //编队计算
    final static int SINGLE_CALC = 0;
    final static int GROUP_CALC = 1;
    private int calcType = SINGLE_CALC;

    public CardPickEntity(int id, String name, int img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public CardPickEntity(int id, String name, int img, int svtSource, int svtAvatar) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.svtSource = svtSource;
        this.svtAvatar = svtAvatar;
        this.calcType = GROUP_CALC;
    }

    public int getSvtAvatar() {
        return svtAvatar;
    }

    public void setSvtAvatar(int svtAvatar) {
        this.svtAvatar = svtAvatar;
    }

    public int getCalcType() {
        return calcType;
    }

    public void setCalcType(int calcType) {
        this.calcType = calcType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getSvtSource() {
        return svtSource;
    }

    public void setSvtSource(int svtSource) {
        this.svtSource = svtSource;
    }
}
