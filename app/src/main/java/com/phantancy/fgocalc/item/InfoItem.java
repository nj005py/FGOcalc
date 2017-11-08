package com.phantancy.fgocalc.item;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoItem implements Serializable {
    private int portrait;
    private String info;
    private List<InfoCardsMVPItem> cardsList;
    private int type;
    private int column;

    public InfoItem(int portrait, String info, List<InfoCardsMVPItem> cardsList, int type) {
        this.portrait = portrait;
        this.info = info;
        this.cardsList = cardsList;
        this.type = type;
    }

    public InfoItem(int portrait, int type) {
        this.portrait = portrait;
        this.type = type;
    }

    public InfoItem(String info, int type) {
        this.info = info;
        this.type = type;
    }

    public InfoItem(List<InfoCardsMVPItem> cardsList, int type) {
        this.cardsList = cardsList;
        this.type = type;
    }

    public InfoItem(String info, int type, int column) {
        this.info = info;
        this.type = type;
        this.column = column;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<InfoCardsMVPItem> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<InfoCardsMVPItem> cardsList) {
        this.cardsList = cardsList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
