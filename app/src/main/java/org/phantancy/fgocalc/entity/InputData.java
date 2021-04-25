package org.phantancy.fgocalc.entity;

import org.phantancy.fgocalc.common.Constant;

import java.util.HashMap;
import java.util.Map;

public class InputData {
    //ui数据
    //选卡类型，
    private String cardType1;
    private String cardType2;
    private String cardType3;
    private String cardType4 = Constant.CARD_EX;

    //1-3位置是否暴击
    private boolean isCritical1;
    private boolean isCritical2;
    private boolean isCritical3;

    //1-4位置是否过量伤害
    private boolean isOverkill1;
    private boolean isOverkill2;
    private boolean isOverkill3;
    private boolean isOverkill4;

    //敌人
    private String[] enemyClasses;
    private String targetEnemyClass;

    //总atk
    private double atk;
    //总hp
    private double hp;
    //剩余hp
    private double hpLeft;
    //是否保存条件
    private boolean isSavedCondition = false;
    //是否保存buff
    private boolean isSavedBuff = false;

    private double quickBuffP;
    private double artsBuffP;
    private double busterBuffP;
    private String affinityType;
    private String attributeType;
    private double atkUp;
    private double atkDown;
    private double defUp;
    private double defDown;
    private double specialBuff;
    private double specialDefBuff;
    private double criticalUp;
    private double criticalDown;
    private double criticalQuick;
    private double criticalArts;
    private double criticalBuster;
    private double selfDmgBuff;
    private double selfDmgDefBuff;
    private double npDmgMultiplier;
    private double atkBuff;
    private double defBuff;
    private double npPowerUp;
    private double npPowerDown;
    private double npSpecialBuff;
    //数据库获取
    private ServantEntity svt;
    /**
     * 加工数据
     */
    private String firstCardType;
    private boolean isSameColor;
    private boolean isBusterChain;
    private double random;
    private double enemyNpMod;
    private double[] enemysNpMod;
    //输入buff数据
    private Map<String,Double> buffMap;

    public boolean isSavedCondition() {
        return isSavedCondition;
    }

    public void setSavedCondition(boolean savedCondition) {
        isSavedCondition = savedCondition;
    }

    public boolean isSavedBuff() {
        return isSavedBuff;
    }

    public void setSavedBuff(boolean savedBuff) {
        isSavedBuff = savedBuff;
    }

    public Map<String, Double> getBuffMap() {
        if (buffMap == null) {
            buffMap = new HashMap<>();
        }
        return buffMap;
    }

    public void setBuffMap(Map<String, Double> buffMap) {
        this.buffMap = buffMap;
    }

    public boolean isOverkill4() {
        return isOverkill4;
    }

    public void setOverkill4(boolean overkill4) {
        isOverkill4 = overkill4;
    }

    public String getCardType1() {
        return cardType1;
    }

    public void setCardType1(String cardType1) {
        this.cardType1 = cardType1;
    }

    public String getCardType2() {
        return cardType2;
    }

    public void setCardType2(String cardType2) {
        this.cardType2 = cardType2;
    }

    public String getCardType3() {
        return cardType3;
    }

    public void setCardType3(String cardType3) {
        this.cardType3 = cardType3;
    }

    public String getCardType4() {
        return cardType4;
    }

    public void setCardType4(String cardType4) {
        this.cardType4 = cardType4;
    }

    public boolean isCritical1() {
        return isCritical1;
    }

    public void setCritical1(boolean critical1) {
        isCritical1 = critical1;
    }

    public boolean isCritical2() {
        return isCritical2;
    }

    public void setCritical2(boolean critical2) {
        isCritical2 = critical2;
    }

    public boolean isCritical3() {
        return isCritical3;
    }

    public void setCritical3(boolean critical3) {
        isCritical3 = critical3;
    }

    public boolean isOverkill1() {
        return isOverkill1;
    }

    public void setOverkill1(boolean overkill1) {
        isOverkill1 = overkill1;
    }

    public boolean isOverkill2() {
        return isOverkill2;
    }

    public void setOverkill2(boolean overkill2) {
        isOverkill2 = overkill2;
    }

    public boolean isOverkill3() {
        return isOverkill3;
    }

    public void setOverkill3(boolean overkill3) {
        isOverkill3 = overkill3;
    }

    public String[] getEnemyClasses() {
        return enemyClasses;
    }

    public void setEnemyClasses(String[] enemyClasses) {
        this.enemyClasses = enemyClasses;
    }

    public String getTargetEnemyClass() {
        return targetEnemyClass;
    }

    public void setTargetEnemyClass(String targetEnemyClass) {
        this.targetEnemyClass = targetEnemyClass;
    }

    public double getAtk() {
        return atk;
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }

    public double getQuickBuffP() {
        return quickBuffP;
    }

    public void setQuickBuffP(double quickBuffP) {
        this.quickBuffP = quickBuffP;
    }

    public double getArtsBuffP() {
        return artsBuffP;
    }

    public void setArtsBuffP(double artsBuffP) {
        this.artsBuffP = artsBuffP;
    }

    public double getBusterBuffP() {
        return busterBuffP;
    }

    public void setBusterBuffP(double busterBuffP) {
        this.busterBuffP = busterBuffP;
    }

    public String getAffinityType() {
        return affinityType;
    }

    public void setAffinityType(String affinityType) {
        this.affinityType = affinityType;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public double getAtkUp() {
        return atkUp;
    }

    public void setAtkUp(double atkUp) {
        this.atkUp = atkUp;
    }

    public double getAtkDown() {
        return atkDown;
    }

    public void setAtkDown(double atkDown) {
        this.atkDown = atkDown;
    }

    public double getDefUp() {
        return defUp;
    }

    public void setDefUp(double defUp) {
        this.defUp = defUp;
    }

    public double getDefDown() {
        return defDown;
    }

    public void setDefDown(double defDown) {
        this.defDown = defDown;
    }

    public double getSpecialBuff() {
        return specialBuff;
    }

    public void setSpecialBuff(double specialBuff) {
        this.specialBuff = specialBuff;
    }

    public double getSpecialDefBuff() {
        return specialDefBuff;
    }

    public void setSpecialDefBuff(double specialDefBuff) {
        this.specialDefBuff = specialDefBuff;
    }

    public double getCriticalUp() {
        return criticalUp;
    }

    public void setCriticalUp(double criticalUp) {
        this.criticalUp = criticalUp;
    }

    public double getCriticalDown() {
        return criticalDown;
    }

    public void setCriticalDown(double criticalDown) {
        this.criticalDown = criticalDown;
    }

    public double getCriticalQuick() {
        return criticalQuick;
    }

    public void setCriticalQuick(double criticalQuick) {
        this.criticalQuick = criticalQuick;
    }

    public double getCriticalArts() {
        return criticalArts;
    }

    public void setCriticalArts(double criticalArts) {
        this.criticalArts = criticalArts;
    }

    public double getCriticalBuster() {
        return criticalBuster;
    }

    public void setCriticalBuster(double criticalBuster) {
        this.criticalBuster = criticalBuster;
    }

    public double getSelfDmgBuff() {
        return selfDmgBuff;
    }

    public void setSelfDmgBuff(double selfDmgBuff) {
        this.selfDmgBuff = selfDmgBuff;
    }

    public double getSelfDmgDefBuff() {
        return selfDmgDefBuff;
    }

    public void setSelfDmgDefBuff(double selfDmgDefBuff) {
        this.selfDmgDefBuff = selfDmgDefBuff;
    }

    public double getNpDmgMultiplier() {
        return npDmgMultiplier;
    }

    public void setNpDmgMultiplier(double npDmgMultiplier) {
        this.npDmgMultiplier = npDmgMultiplier;
    }

    public double getAtkBuff() {
        return atkBuff;
    }

    public void setAtkBuff(double atkBuff) {
        this.atkBuff = atkBuff;
    }

    public double getDefBuff() {
        return defBuff;
    }

    public void setDefBuff(double defBuff) {
        this.defBuff = defBuff;
    }

    public double getNpPowerUp() {
        return npPowerUp;
    }

    public void setNpPowerUp(double npPowerUp) {
        this.npPowerUp = npPowerUp;
    }

    public double getNpPowerDown() {
        return npPowerDown;
    }

    public void setNpPowerDown(double npPowerDown) {
        this.npPowerDown = npPowerDown;
    }

    public double getNpSpecialBuff() {
        return npSpecialBuff;
    }

    public void setNpSpecialBuff(double npSpecialBuff) {
        this.npSpecialBuff = npSpecialBuff;
    }

    public ServantEntity getSvt() {
        return svt;
    }

    public void setSvt(ServantEntity svt) {
        this.svt = svt;
    }

    public String getFirstCardType() {
        return firstCardType;
    }

    public void setFirstCardType(String firstCardType) {
        this.firstCardType = firstCardType;
    }

    public boolean isSameColor() {
        return isSameColor;
    }

    public void setSameColor(boolean sameColor) {
        isSameColor = sameColor;
    }

    public boolean isBusterChain() {
        return isBusterChain;
    }

    public void setBusterChain(boolean busterChain) {
        isBusterChain = busterChain;
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
    }

    public double getEnemyNpMod() {
        return enemyNpMod;
    }

    public void setEnemyNpMod(double enemyNpMod) {
        this.enemyNpMod = enemyNpMod;
    }

    public double[] getEnemysNpMod() {
        return enemysNpMod;
    }

    public void setEnemysNpMod(double[] enemysNpMod) {
        this.enemysNpMod = enemysNpMod;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getHpLeft() {
        return hpLeft;
    }

    public void setHpLeft(double hpLeft) {
        this.hpLeft = hpLeft;
    }
}
