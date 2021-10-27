package org.phantancy.fgocalc.data;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConditionData {
    //职阶相性
    public final static Map<String, Double> affinityMap = new LinkedHashMap<>();

    static {
        affinityMap.put("2.0x", 2.0);
        affinityMap.put("1.5x", 1.5);
        affinityMap.put("1.0x", 1.0);
        affinityMap.put("0.5x", 0.5);
        affinityMap.put("1.2x", 1.2);
    }

    public static String[] getAffinityKeys() {
        return affinityMap.keySet().toArray(new String[0]);
    }

    public static Double[] getAffinityValues() {
        return affinityMap.values().toArray(new Double[0]);
    }

    //阵营相性
    public final static Map<String, Double> attributeMap = new LinkedHashMap<>();

    static {
        attributeMap.put("无克制", 1.0);
        attributeMap.put("被克制", 0.9);
        attributeMap.put("克制", 1.1);
    }

    public static String[] getAttributeKeys() {
        return attributeMap.keySet().toArray(new String[0]);
    }

    public static Double[] getAttributeValues() {
        return attributeMap.values().toArray(new Double[0]);
    }

    //宝具等级
    public final static String[] npLvKeys = {"一宝", "二宝", "三宝", "四宝", "五宝"};

    //芙芙atk
    public final static Map<String, Integer> fouAtkMap = new LinkedHashMap<>();

    static {
        fouAtkMap.put("0", 0);
        fouAtkMap.put("1000", 1000);
        fouAtkMap.put("2000", 2000);
    }

    public static String[] getFouAtkKeys() {
        return fouAtkMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getFouAtkValues() {
        return fouAtkMap.values().toArray(new Integer[0]);
    }

    //礼装atk
    public final static Map<String, Integer> essenceAtkMap = new LinkedHashMap<>();

    static {
        essenceAtkMap.put("0", 0);
        essenceAtkMap.put("500", 500);
        essenceAtkMap.put("786", 786);
        essenceAtkMap.put("1000", 1000);
        essenceAtkMap.put("2000", 2000);
        essenceAtkMap.put("2400", 2400);
    }

    public static String[] getEssenceAtkKeys() {
        return essenceAtkMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getEssenceAtkValues() {
        return essenceAtkMap.values().toArray(new Integer[0]);
    }

//    public final static String[] essenceAtkKeys = {"0","500","786","1000","2000","2400"};
//    public final static int[] essenceAtkValues = {0,500,786,1000,2000,2400};

    //敌人数量
    public final static Map<String, Integer> enemyCountMap = new HashMap<String, Integer>();

    static {
        enemyCountMap.put("1", 1);
        enemyCountMap.put("2", 2);
        enemyCountMap.put("3", 3);
        enemyCountMap.put("4", 4);
        enemyCountMap.put("5", 5);
        enemyCountMap.put("6", 6);
    }

    public static String[] getEnemyCountKeys() {
        return enemyCountMap.keySet().toArray(new String[0]);
    }

    public static Integer[] getEnemyCountValues() {
        return enemyCountMap.values().toArray(new Integer[0]);
    }

    //职阶
    public final static String[] classTypeKeys = {"saber", "archer", "lancer", "rider", "caster", "assassin", "berserker",
            "ruler", "alterego", "avenger", "beast", "mooncancer", "foreigner"};

    //敌方np补正类型1
    public final static Map<String,Double> enemyNpMods = new LinkedHashMap<>();
    static {
        enemyNpMods.put("saber类型1",1.0);
        enemyNpMods.put("archer类型1",1.0);
        enemyNpMods.put("lancer类型1",1.0);
        enemyNpMods.put("rider类型1",1.1);
        enemyNpMods.put("caster类型1",1.2);
        enemyNpMods.put("assassin类型1",0.9);
        enemyNpMods.put("berserker类型1",0.8);
        enemyNpMods.put("ruler类型1",1.0);
        enemyNpMods.put("alterego类型1",1.0);
        enemyNpMods.put("avenger类型1",1.0);
        enemyNpMods.put("beast类型1",1.0);
        enemyNpMods.put("mooncancer类型1",1.2);
        enemyNpMods.put("foreigner类型1",1.0);
        enemyNpMods.put("saber类型2",1.2);
        enemyNpMods.put("archer类型2",1.2);
        enemyNpMods.put("lancer类型2",1.2);
        enemyNpMods.put("rider类型2",1.32);
        enemyNpMods.put("caster类型2",1.44);
        enemyNpMods.put("assassin类型2",1.08);
        enemyNpMods.put("berserker类型2",0.96);
    }

    public static String[] getEnemyNpModsKeys() {
        return enemyNpMods.keySet().toArray(new String[0]);
    }

    public static Double[] getEnemyNpModsValues() {
        return enemyNpMods.values().toArray(new Double[0]);
    }

    //敌方打星补正
    public final static Map<String,Double> enemyStarMods = new LinkedHashMap<>();
    static {
        enemyStarMods.put("saber类型1",0.0);
        enemyStarMods.put("archer类型1",0.05);
        enemyStarMods.put("lancer类型1",-0.05);
        enemyStarMods.put("rider类型1",0.1);
        enemyStarMods.put("caster类型1",0.0);
        enemyStarMods.put("assassin类型1",-0.1);
        enemyStarMods.put("berserker类型1",0.0);
        enemyStarMods.put("ruler类型1",0.0);
        enemyStarMods.put("alterego类型1",0.05);
        enemyStarMods.put("avenger类型1",-0.1);
        enemyStarMods.put("beast类型1",0.0);
        enemyStarMods.put("mooncancer类型1",0.0);
        enemyStarMods.put("foreigner类型1",0.2);
        enemyStarMods.put("saber类型2",0.0);
        enemyStarMods.put("archer类型2",0.05);
        enemyStarMods.put("lancer类型2",-0.05);
        enemyStarMods.put("rider类型2",0.1);
        enemyStarMods.put("caster类型2",0.0);
        enemyStarMods.put("assassin类型2",-0.1);
        enemyStarMods.put("berserker类型2",0.0);
    }

    public static Double[] getEnemyStarModsValues() {
        return enemyStarMods.values().toArray(new Double[enemyStarMods.size()]);
    }

}
