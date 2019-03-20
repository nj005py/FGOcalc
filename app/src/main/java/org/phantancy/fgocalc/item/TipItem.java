package org.phantancy.fgocalc.item;

import java.io.Serializable;
import java.util.List;

public class TipItem implements Serializable {
    private boolean hasTip;//有无提示
    private String tip;//提示内容
    private String imgName;//立体图
    private boolean hasOption;//有无选项
    private List<OptionItem> options;//选项

    public TipItem() {
    }

    public TipItem(String imgName, String tip, boolean hasTip) {
        this.imgName = imgName;
        this.tip = tip;
        this.hasTip = hasTip;
    }

    public TipItem(String imgName, String tip, boolean hasTip, boolean hasOption) {
        this.imgName = imgName;
        this.tip = tip;
        this.hasTip = hasTip;
        this.hasOption = hasOption;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
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

    public List<OptionItem> getOptions() {
        return options;
    }

    public void setOptions(List<OptionItem> options) {
        this.options = options;
    }
}
