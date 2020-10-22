package org.phantancy.fgocalc.entity;

public class CardPickEntity {
    private int id;
    private String name;
    private int img;
    private boolean isVisible = true;

    public CardPickEntity(int id, String name, int img) {
        this.id = id;
        this.name = name;
        this.img = img;
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
}
