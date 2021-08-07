package org.phantancy.fgocalc.common;

import androidx.collection.SimpleArrayMap;

import org.phantancy.fgocalc.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HATTER on 2017/11/4.
 */

public class Constant {
    //数据库版本
    public static final int DATABASE_VERSION= 70;

    //handler
    public static final int CHECK_APP_VERSION = 666;

    //头像最新ID
    public static final int AVATAR_LATEST_ID = 311;

    //默认头像下载地址
    public static final String AVATAR_BASE_URL = "https://gitee.com/nj005py/fgocalc/raw/master/svt/";
    //默认wiki地址
    public static String WIKI_URL = "http://fgo.vgtime.com/servant/";
    public static String MOONCELL_URL = "https://fgo.wiki";

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

    private static SimpleArrayMap<String,Integer> cardDrawableMap = new SimpleArrayMap<>();
    static {
        cardDrawableMap.put(CARD_QUICK,R.drawable.quick);
        cardDrawableMap.put(CARD_ARTS,R.drawable.arts);
        cardDrawableMap.put(CARD_BUSTER,R.drawable.buster);
        cardDrawableMap.put(CARD_EX,R.drawable.extra);
        cardDrawableMap.put(NP_QUICK,R.drawable.np_q);
        cardDrawableMap.put(NP_ARTS,R.drawable.np_a);
        cardDrawableMap.put(NP_BUSTER,R.drawable.np_b);
    }

    public static int getCardDrawable(String cardType) {
        return cardDrawableMap.get(cardType);
    }

    //单从者入口
    public final static int ENTRY_SINGLE = 0;
    //编队计算入口
    public final static int ENTRY_GROUP = 1;

}
