package org.phantancy.fgocalc.common;

import org.phantancy.fgocalc.entity.CalcEntity;
import org.phantancy.fgocalc.entity.OneTurnResult;

import java.text.DecimalFormat;
import java.text.MessageFormat;

public class LogManager {
    public static String logFinal(String infoLog, String calcLogs) {
        return mergeLog(infoLog, calcLogs);
    }

    public static String logInfo(String name, String svtClass) {
        return name + " " + svtClass;
    }

    public static String logCalc(String conditionLog, String buffLog, String resultLog) {
        return mergeLog(mergeLog(conditionLog, buffLog), resultLog);
    }

    public static String logCondition(CalcEntity data) {
        StringBuilder y = new StringBuilder();
//        y.append(MessageFormat.format("职阶克制{0} 阵营克制{1}",data.affinityType,data.attributeType));
        return y.toString();
    }

//    public static String logBuff(List<InputBuffEntity> x) {
//        StringBuilder y = new StringBuilder();
//        for (InputBuffEntity it : x) {
//            if (it.getValue() != 0) {
//                y.append(it.getHint()).append(displayBuffValue(it));
//            }
//        }
//        return y.toString();
//    }

   public static String resultLog(CalcEntity data, OneTurnResult x) {
        //1.a卡伤害10000 ~ 10197,平均10002
        StringBuilder y = new StringBuilder();
        y.append(MessageFormat.format("1.{0}卡伤害{1} ~ {2},平均{3}", getCardDisplay(data.getCardType1()),
                displayResult(x.min1), displayResult(x.max1), displayResult(x.avg1)))
                .append(MessageFormat.format("2.{0}卡伤害{1} ~ {2},平均{3}", getCardDisplay(data.getCardType2()),
                        displayResult(x.min2), displayResult(x.max2), displayResult(x.avg2)))
                .append(MessageFormat.format("3.{0}卡伤害{1} ~ {2},平均{3}", getCardDisplay(data.getCardType3()),
                        displayResult(x.min3), displayResult(x.max3), displayResult(x.avg3)))
                .append(MessageFormat.format("4.{0}卡伤害{1} ~ {2},平均{3}", getCardDisplay(data.getCardType4()),
                        displayResult(x.min4), displayResult(x.max4), displayResult(x.avg4)))
                .append(MessageFormat.format("总计{0} ~ {1},平均{2}", displayResult(x.sumMin),
                        displayResult(x.sumMax), displayResult(x.sumAvg)));
        return y.toString();
    }

    public static String mergeLog(String x, String y) {
        return new StringBuilder()
                .append(x)
                .append(y)
                .toString();
    }

    public static String getCardDisplay(String x) {
        if (x.contains("np")) {
            return "宝具";
        } else {
            return x;
        }
    }

    public static String displayResult(double x) {
        DecimalFormat f = new DecimalFormat("#.00");
        return f.format(x);
    }

//    public static String displayBuffValue(InputBuffEntity x) {
//        if (x.getType() == 1) {
//            return TypeConverter.doubleToString(x.getValue());
//        }
//        if (x.getType() == 2) {
//            return x + "%";
//        }
//        return "";
//    }
}
