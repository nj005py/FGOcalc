package org.phantancy.fgocalc.item;

import java.io.Serializable;
import java.util.List;

public class TipItem implements Serializable {
    private int imgId;
    private String tip;
    private boolean hasTip;
    private boolean hasOption;
    private List<OptionItem> optionList;

    public TipItem() {
    }

    public TipItem(int imgId, String tip, boolean hasTip) {
        this.imgId = imgId;
        this.tip = tip;
        this.hasTip = hasTip;
    }

    public TipItem(int imgId, String tip, boolean hasTip, boolean hasOption) {
        this.imgId = imgId;
        this.tip = tip;
        this.hasTip = hasTip;
        this.hasOption = hasOption;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isHasTip() {
        return hasTip;
    }

    public void setHasTip(boolean hasTip) {
        this.hasTip = hasTip;
    }

    public boolean isHasOption() {
        return hasOption;
    }

    public void setHasOption(boolean hasOption) {
        this.hasOption = hasOption;
    }

    public List<OptionItem> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<OptionItem> optionList) {
        this.optionList = optionList;
    }
}
