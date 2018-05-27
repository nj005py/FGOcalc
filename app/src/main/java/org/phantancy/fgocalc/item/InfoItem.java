package org.phantancy.fgocalc.item;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoItem implements Serializable {
    private int id;
    private int portrait;
    private String info;
    private List<InfoCardsMVPItem> cardsList;
    private int type;
    private int column;
    private String pic;//数据库里的Base64字符串

    public InfoItem(int portrait, String info, List<InfoCardsMVPItem> cardsList, int type) {
        this.portrait = portrait;
        this.info = info;
        this.cardsList = cardsList;
        this.type = type;
    }

    public InfoItem(int id,int portrait,String pic, int type) {
        this.id = id;
        this.portrait = portrait;
        this.type = type;
        this.pic = pic;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
