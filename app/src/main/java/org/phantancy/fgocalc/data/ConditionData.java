package org.phantancy.fgocalc.data;

import java.util.HashMap;
import java.util.Map;

public class ConditionData {
    //职阶相性
    public static Map<String,Double> affinityMap = new HashMap<String,Double>(){{
        put("0.5x", 0.5);
        put("1.0x", 1.0);
        put("1.2x", 1.2);
        put("1.5x", 1.5);
        put("2.0x", 2.0);
    }};

    public static String[] getAffinityKeys() {
        return affinityMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getAffinityValues() {
        return affinityMap.values().toArray(new Integer[0]);
    }

    //阵营相性
    public static Map<String,Double> attributeMap = new HashMap<String, Double>(){{
        put("无克制",1.0);
        put("被克制",0.9);
        put("克制",1.1);
    }};

    public static String[] getAttributeKeys() {
        return attributeMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getAttributeValues() {
        return attributeMap.values().toArray(new Integer[0]);
    }

    //宝具等级
    public static String[] npLvKeys = {"一宝","二宝","三宝","四宝","五宝"};

    //芙芙atk
    static Map<String,Integer> fouAtkMap = new HashMap<String,Integer>(){{
        put("0",0);
        put("1000",1000);
        put("2000",2000);
    }};

    public static String[] getFouAtkKeys() {
        return fouAtkMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getFouAtkValues() {
        return fouAtkMap.values().toArray(new Integer[0]);
    }

    //礼装atk
    final static Map<String,Integer> essenceAtkMap = new HashMap<String,Integer>(){{
        put("0",0);
        put("500",500);
        put("786",786);
        put("1000",1000);
        put("2000",2000);
        put("2400",2400);
    }};

    public static String[] getEssenceAtkKeys() {
        return essenceAtkMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getEssenceAtkValues() {
        return essenceAtkMap.values().toArray(new Integer[0]);
    }

    public final static String[] essenceAtkKeys = {"0","500","786","1000","2000","2400"};
    public final static int[] essenceAtkValues = {0,500,786,1000,2000,2400};

    //敌人数量
    final static Map<String,Integer> enemyCountMap = new HashMap<String, Integer>(){{
        put("1",1);
        put("2",2);
        put("3",3);
    }};

    public static String[] getEnemyCountKeys() {
        return enemyCountMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getEnemyCountValues() {
        return enemyCountMap.values().toArray(new Integer[0]);
    }

    //职阶
    public final static String[] classTypeKeys = {"saber","archer","lancer","rider","caster","assassin","berserker",
            "ruler", "shielder","alterego","avenger","beast","mooncancer","foreigner" };
}
