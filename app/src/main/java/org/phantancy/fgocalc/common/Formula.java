package org.phantancy.fgocalc.common;

/***
 * 公式
 */
public class Formula {
    public static double damgeFormula(
            double atk,
            double cardDmgMultiplier,
            double postitionMod,
            double effectiveBuff,
            double firstCardMod,
            double classAtkMod,
            double affinityMod,
            double attributeMod,
            double randomMod,
            double atkBuff,
            double defBuff,
            double specialBuff,
            double specialDefBuff,
            double criticalBuff,
            double criticalMod,
            double exDmgBuff,
            double selfDmgBuff,
            double selfDmgDefBuff,
            double busterChainMod
    ) {
        double attack = atk * 0.23 * (cardDmgMultiplier * postitionMod * (1.0 + effectiveBuff) + firstCardMod) *
                classAtkMod * affinityMod * attributeMod * randomMod * (1.0 + atkBuff - defBuff) *
                (1.0 + specialBuff - specialDefBuff + criticalBuff) * criticalMod * exDmgBuff
                + (selfDmgBuff - selfDmgDefBuff) + atk * busterChainMod;
        return attack;
    }

    public static double npDamageFormula(
            double atk,
            double npDmgMultiplier,
            double cardDmgMultiplier,
            double effectiveBuff,
            double classAtkMod,
            double affinityMod,
            double attributeMod,
            double randomMod,
            double atkBuff,
            double defBuff,
            double specialBuff,
            double specialDefBuff,
            double npPowerBuff,
            double npSpecialBuff,
            double selfDmgBuff,
            double selfDmgDefBuff
    ) {
        double attack = atk * 0.23 * (npDmgMultiplier * cardDmgMultiplier * (1.0 + effectiveBuff))
                * classAtkMod * affinityMod * attributeMod * randomMod *
                (1.0 + atkBuff - defBuff) * (1.0 + specialBuff - specialDefBuff + npPowerBuff)
                * npSpecialBuff + (selfDmgBuff - selfDmgDefBuff);
        return attack;
    }

    //普攻np获取
    public static double npGenerationFormula(
            double na,
            double hits,
            double cardNpMultiplier,
            double positionMod,
            double effectiveBuff,
            double firstCardMod,
            double npBuff,
            double criticalMod,
            double overkillMod,
            double enemyNpMod
    ) {
        double np = na * hits * (cardNpMultiplier * positionMod * (1 + effectiveBuff) + firstCardMod) *
                (1 + npBuff) * criticalMod * overkillMod * enemyNpMod;
        return np;
    }

    //宝具np获取
    public static double npNpGenerationFormula(
            double na,
            double hits,
            double cardNpMultiplier,
            double effectiveBuff,
            double npBuff,
            double overkillMod,
            double enemyNpMod
    ) {
        double np = na * hits * (cardNpMultiplier * (1 + effectiveBuff)) *
                (1 + npBuff) * overkillMod * enemyNpMod;
        return np;
    }

    public static double starDropRatePerHitFormula(
            double starRate,
            double cardStarMultiplier,
            double effectiveBuff,
            double firstCardMod,
            double starRateBuff,
            double criticalMod,
            double enemyStarBuff,
            double enemyStarMod,
            double overkillMultiplier,
            double overkillAdd
    ) {
        double ratePerHit = (starRate + cardStarMultiplier * (1 + effectiveBuff) + firstCardMod + starRateBuff + criticalMod
                - enemyStarBuff - enemyStarMod) * overkillMultiplier + overkillAdd;
        return ratePerHit;
    }

    public static double npStarDropRatePerHitFormula(
            double starRate,
            double cardStarRate,
            double effectiveBuff,
            double starRateBuff,
            double enemyStarBuff,
            double enemyStarMod,
            double overkillMultiplier,
            double overkillAdd
    ) {
        double ratePerHit = ((starRate + cardStarRate * (1 + effectiveBuff) + starRateBuff - enemyStarBuff - enemyStarMod)
                * overkillMultiplier + overkillAdd);
        return ratePerHit;
    }

    public static double starGenerationAmountFormula(
            double starDropRate,
            double hits
    ){
        return starDropRate * hits;
    }
}
