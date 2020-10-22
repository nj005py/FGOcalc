package org.phantancy.fgocalc.common;

import org.phantancy.fgocalc.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HATTER on 2017/11/4.
 */

public class Constant {
    public static final int TYPE_IMG = 0;
    public static final int TYPE_VALUE = 1;
    public static final int TYPE_LIST = 2;

    //buff
    public static final int SET_BUFF = 0;

    //handler
    public static final int CHECK_APP_VERSION = 666;

    //头像最新ID
    public static final int AVATAR_LATEST_ID = 267;

    //默认头像下载地址
    public static final String AVATAR_BASE_URL = "https://gitee.com/nj005py/fgocalc/raw/master/svt/";
    //默认wiki地址
    public static String WIKI_URL = "http://fgo.vgtime.com/servant/";

    public static final Map<String, Integer> NP_COLOR_MAP = new HashMap<String, Integer>() {
        {
            put("q", R.drawable.quick);
            put("a", R.drawable.arts);
            put("b", R.drawable.buster);
        }
    };

    public static final Map<String, String> NP_CLASSIFICATION_MAP = new HashMap<String, String>() {
        {
            put("one", "单体");
            put("all", "全体");
            put("support", "辅助");
        }
    };

    //伤害随机数
    public static final Map<String, Double> RANDOM_ATK = new HashMap<String, Double>() {
        {
            put("平均", 1.0);
            put("最大", 1.1);
            put("最小", 0.9);
        }
    };

    //宝具充能随机数
    public static final Map<String, Double> RANDOM_NPC = new HashMap<String, Double>() {
        {
            put("一般", 1.0);
            put("敌方Rider", 1.1);
            put("敌方Caster", 1.2);
            put("敌方Assassin", 0.9);
            put("敌方Bserserker", 0.8);
        }
    };

    //打星随机数
    public static final Map<String, Double> RANDOM_STAR = new HashMap<String, Double>() {
        {
            put("平均", 0.05);
            put("最大", 0.2);
            put("最小", -0.1);
        }
    };

    //指令卡类型
    public final static String CARD_QUICK = "q";
    public final static String CARD_ARTS = "a";
    public final static String CARD_BUSTER = "b";
    public final static String CARD_EX = "ex";
    public final static String NP_QUICK = "np_q";
    public final static String NP_ARTS = "np_a";
    public final static String NP_BUSTER = "np_b";

    //指令卡颜色
    public final static String COLOR_QUICK = "q";
    public final static String COLOR_ARTS = "a";
    public final static String COLOR_BUSTER = "b";

}
