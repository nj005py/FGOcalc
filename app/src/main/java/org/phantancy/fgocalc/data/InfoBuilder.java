package org.phantancy.fgocalc.data;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.util.ToolCase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class InfoBuilder {

    public static List<InfoEntity> buildServantInfo(ServantEntity servant) {
        return new ArrayList<InfoEntity>(){
            {
                add(new InfoEntity("编号",getNumber(servant.id)));
                add(new InfoEntity("职阶",servant.classType));
                add(new InfoEntity("星",servant.star + "星"));
                add(new InfoEntity("阵营", servant.attribute));
                add(new InfoEntity("属性",servant.alignments));
                add(new InfoEntity("特性",servant.traits));
                add(new InfoEntity("掉星率",getPercent(servant.starGeneration)));

                //4
                add(new InfoEntity("基础Hp",getNumber(servant.hpBase)));
                add(new InfoEntity("基础Atk",getNumber(servant.atkBase)));
                add(new InfoEntity("满级Hp",getNumber(servant.hpDefault)));
                add(new InfoEntity("满级Atk",getNumber(servant.atkDefault)));
                add(new InfoEntity("满级系数Hp",getNumber(servant.hpDefaultMod)));
                add(new InfoEntity("满级系数Atk",getNumber(servant.atkDefaultMod)));

                //5
                add(new InfoEntity("绿卡Hits",getNumber(servant.quickHit)));
                add(new InfoEntity("蓝卡Hits",getNumber(servant.artsHit)));
                add(new InfoEntity("红卡Hits",getNumber(servant.busterHit)));
                add(new InfoEntity("Ex卡Hits",getNumber(servant.exHit)));

                //6
                add(new InfoEntity("宝具Hits",getNumber(servant.npHit)));
                add(new InfoEntity("绿卡NP获取",getPercent(servant.quickNa)));
                add(new InfoEntity("蓝卡NP获取",getPercent(servant.artsNa)));
                add(new InfoEntity("红卡NP获取",getPercent(servant.busterNa)));

                //7
                add(new InfoEntity("Ex卡NP获取",getPercent(servant.exNa)));
                add(new InfoEntity("宝具NP获取",getPercent(servant.npNa)));
                add(new InfoEntity("受击NP获取",getPercent(servant.nd)));
                add(new InfoEntity("被动绿魔放",getPercent(servant.quickBuffN)));

                //8
                add(new InfoEntity("被动蓝魔放",getPercent(servant.artsBuffN)));
                add(new InfoEntity("被动红魔放",getPercent(servant.busterBuffN)));
                add(new InfoEntity("被动特攻",getPercent(servant.specialBuffN)));
                add(new InfoEntity("被动暴击",getPercent(servant.criticalBuffN)));

                //9
                add(new InfoEntity("被动固定伤害",getNumber(servant.selfDamageN)));
                add(new InfoEntity("被动产星率",getPercent(servant.starGenerationN)));

            }
        };
    }

    private static String getPercent(double value) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        return nf.format(value);
    }

    private static String getNumber(int value) {
        return String.valueOf(value);
    }

    private static String getNumber(double value) {
        return String.valueOf(value);
    }

}
