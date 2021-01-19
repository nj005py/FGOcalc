package org.phantancy.fgocalc.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.ScrollView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.common.Formula;
import org.phantancy.fgocalc.common.LogManager;
import org.phantancy.fgocalc.common.ParamsMerger;
import org.phantancy.fgocalc.data.BuffData;
import org.phantancy.fgocalc.data.CalcRepository;
import org.phantancy.fgocalc.data.InfoBuilder;
import org.phantancy.fgocalc.entity.BuffInputEntity;
import org.phantancy.fgocalc.entity.CardPickEntity;
import org.phantancy.fgocalc.entity.InputData;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.OneTurnResult;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.entity.SvtExpEntity;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//计算Activity ViewModel
public class CalcViewModel extends AndroidViewModel {
    final String TAG = "CalcViewModel";
    private ServantEntity servant;
    //经验列表
    List<SvtExpEntity> svtExpEntities;
    //输入的数据，采集用户输入数据
    public InputData inputData;

    private int atkLv = 0;
    private int atkEssence = 0;
    private int atkFou = 0;
    private int atkDefault = 0;
    private int hpDefault = 0;
    private int hpLeft = 0;

    public String getAtkDefaultKey() {
        return atkDefault + "";
    }

    public String getHpDefaultKey() {
        return hpDefault + "";
    }

    public String getHpLeftKey() {
        return hpLeft + "";
    }

    private int hpLv = 0;

    public void setServant(ServantEntity servant) {
        this.servant = servant;
        setSvtExpEntities();
        atkLv = servant.atkDefault;
        atkDefault = servant.atkDefault;
        hpDefault = servant.hpDefault;
        hpLeft = servant.hpDefault;
    }

    public ServantEntity getServant() {
        return servant;
    }

    //当前页
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();

    public LiveData<Integer> getCurrentPage() {
        return mCurrentPage;
    }

    private CalcRepository calcRepository;

    public CalcViewModel(Application app) {
        super(app);
        calcRepository = new CalcRepository(app);
        inputData = new InputData();
    }

    //信息列表
    private MutableLiveData<List<InfoEntity>> infoList = new MutableLiveData<>();

    public LiveData<List<InfoEntity>> getInfoList() {
        return infoList;
    }

    public void initInfoList() {
        List<InfoEntity> list = InfoBuilder.buildServantInfo(servant);
        infoList.setValue(list);
    }

    //获得可选配卡
    private MutableLiveData<List<CardPickEntity>> cardPicks = new MutableLiveData<>();

    public LiveData<List<CardPickEntity>> getCardPicks() {
        return cardPicks;
    }

    public void parsePickCards() {
        String x = servant.cards;
        int id = 0;
        List<CardPickEntity> list = new ArrayList<>();
        for (char y : x.toCharArray()) {
            list.add(parseCardPickEntity(id, y));
            id++;
        }
        list.add(parseCardPickNp(id, servant.npColor));
        //测试宇宙凛
//        list.add(parseCardPickNp(id++, "np_q"));
//        list.add(parseCardPickNp(id++, "np_a"));
//        list.add(parseCardPickNp(id, "np_b"));
        cardPicks.setValue(list);
    }

    private CardPickEntity parseCardPickEntity(int id, char color) {
        if (color == 'q') {
            return new CardPickEntity(id, "q", R.drawable.quick);
        }
        if (color == 'a') {
            return new CardPickEntity(id, "a", R.drawable.arts);
        }
        return new CardPickEntity(id, "b", R.drawable.buster);

    }

    private CardPickEntity parseCardPickNp(int id, String color) {
        if (color.equals("np_q")) {
            return new CardPickEntity(id, color, R.drawable.np_q);
        }
        if (color.equals("np_a")) {
            return new CardPickEntity(id, color, R.drawable.np_a);
        }
        return new CardPickEntity(id, color, R.drawable.np_b);

    }

    private List<CardPickEntity> pickedCards = new ArrayList<>();

    public void setPickedCards(List<CardPickEntity> pickedCards) {
        this.pickedCards = pickedCards;
    }

    //获取从者链接
    public String getServantWiki() {
        int id = servant.id;
        String url = new StringBuilder()
                .append(Constant.WIKI_URL)
                .append(id)
                .toString();
        return url;
    }

    //条件页
    public int getRewardLv() {
        return servant.rewardLv;
    }

    //合计条件atk
    public int sumAtk() {
        Log.d(TAG, "atkLv:" + atkLv + " atkEssence:" + atkEssence + " atkFou:" + atkFou);
        int res = atkLv + atkEssence + atkFou;
        inputData.setAtk(res);
        return res;
    }

    //芙芙atk变化
    public String onFouAtkChanged(int fou) {
        this.atkFou = fou;
        return sumAtk() + "";
    }

    //礼装atk变化
    public String onEssenceAtkChanged(int essence) {
        this.atkEssence = essence;
        return sumAtk() + "";
    }

    //等级变化，atk变化
    public String onAtkLvChanged(int lv) {
        this.atkLv = getAtkLv(servant, lv, svtExpEntities);
        return sumAtk() + "";
    }

    //等级变化，hp变化
    public String onHpLvChanged(int lv) {
        this.hpLv = getHpLv(servant, lv, svtExpEntities);
        return sumAtk() + "";
    }

    //依据等级获取atk
    public int getAtkLv(ServantEntity sItem, int lv, List<SvtExpEntity> curveList) {
        if (lv > 0 && curveList != null) {
            int curve = curveList.get(lv).curve;

            int atkBase = sItem.atkBase;
            int atkDefault = sItem.atkDefault;

            atkLv = (int) (atkBase + ((float) atkDefault - (float) atkBase) / 1000 * curve);
            return atkLv;
        }
        return 0;
    }

    //依据等级获取hp
    public int getHpLv(ServantEntity sItem, int lv, List<SvtExpEntity> curveList) {
        if (lv > 0 && curveList != null) {
            int curve = curveList.get(lv).curve;

            int hpBase = sItem.hpBase;
            int hpDefault = sItem.hpDefault;

            hpLv = (int) (hpBase + ((float) hpDefault - (float) hpBase) / 1000 * curve);
            return hpLv;
        }
        return 0;
    }

    //获取从者经验数据
    public void setSvtExpEntities() {
        Flowable.create(new FlowableOnSubscribe<List<SvtExpEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<SvtExpEntity>> emitter) throws Exception {
                int id = servant.id;
                emitter.onNext(calcRepository.getSvtExpList(id));
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SvtExpEntity>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<SvtExpEntity> svtExpEntities) {
                        CalcViewModel.this.svtExpEntities = svtExpEntities;
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public List<SvtExpEntity> getSvtExpEntities() {
        return svtExpEntities;
    }

    //buff页
    //获取buff表
    public List<BuffInputEntity> getBuffInputList() {
        return BuffData.buildBuffs();
    }

    //计算页
    private MutableLiveData<String> calcResult = new MutableLiveData<>();
    public LiveData<String> getCalcResult() {
        return calcResult;
    }

    //点击计算
    public void clickCalc(List<CardPickEntity> pickedCards) {
        this.pickedCards = pickedCards;
        if (pickedCards != null && pickedCards.size() == 3) {
            inputData.setCardType1(pickedCards.get(0).getName());
            inputData.setCardType2(pickedCards.get(1).getName());
            inputData.setCardType3(pickedCards.get(2).getName());
            //todo
            OneTurnResult x = oneTurnDmg(servant,inputData);
            String dmgResult = LogManager.resultLog(inputData,x);
            calcResult.setValue(dmgResult);
        }
    }


    //计算一张卡
    //计算伤害，返回最大，最小，平均
    //计算np，返回最大，最小，平均
    //计算打星，返回最大，最小，平均


    //calc dmg
    private OneTurnResult oneTurnDmg(ServantEntity svt,InputData data) {
        //同色
        inputData.setSameColor(ParamsMerger.isCardsSameColor(data.getCardType1(), data.getCardType2(), data.getCardType3()));
        //红链
        inputData.setBusterChain(ParamsMerger.isCardsBusterChain(data.getCardType1(), data.getCardType2(), data.getCardType3()));
        //伤害随机
        double dmgRandomMax = 1.1;
        double dmgRandomMin = 0.9;
        double dmgRandomAvg = 1.0;

        //4 card dmg
//        FullData minParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomMin);
        double min1 = oneCardDmg(data.getCardType1(), 1,dmgRandomMin);
        double min2 = oneCardDmg(data.getCardType2(), 2,dmgRandomMin);
        double min3 = oneCardDmg(data.getCardType3(), 3,dmgRandomMin);
        double min4 = oneCardDmg(data.getCardType4(), 4,dmgRandomMin);
        double sumMin = min1 + min2 + min3 + min4;
        //max
//        FullData maxParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomMax);
        double max1 = oneCardDmg(data.getCardType1(), 1,dmgRandomMax);
        double max2 = oneCardDmg(data.getCardType2(), 2,dmgRandomMax);
        double max3 = oneCardDmg(data.getCardType3(), 3,dmgRandomMax);
        double max4 = oneCardDmg(data.getCardType4(), 4,dmgRandomMax);
        double sumMax = max1 + max2 + max3 + max4;
        //avg
//        FullData avgParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomAvg);
        double avg1 = oneCardDmg(data.getCardType1(), 1,dmgRandomAvg);
        double avg2 = oneCardDmg(data.getCardType2(), 2,dmgRandomAvg);
        double avg3 = oneCardDmg(data.getCardType3(), 3,dmgRandomAvg);
        double avg4 = oneCardDmg(data.getCardType4(), 4,dmgRandomAvg);
        double sumAvg = avg1 + avg2 + avg3 + avg4;
        return new OneTurnResult(min1, min2, min3, min4, sumMin,
                max1, max2, max3, max4, sumMax,
                avg1, avg2, avg3, avg4, sumAvg);
    }

    //计算一张卡的伤害
    private double oneCardDmg(String cardType, int position,double random) {
        //判断卡片类型，宝具卡或普攻卡
        return ParamsMerger.isNp(cardType) ? dmg(cardType, position,random) : npDmg(cardType,random);
    }

    //计算普攻伤害
    //Todo
    private double dmg(String cardType, int position,double random) {
        String cardType1 = inputData.getCardType1();
        boolean isSameColor = inputData.isSameColor();
        boolean isBusterChain = inputData.isBusterChain();
        //atk
        double atk = inputData.getAtk();
        //卡牌伤害倍率
        double cardDmgMultiplier = ParamsMerger.mergecardDmgMultiplier(cardType);
        //位置补正
        double positionMod = ParamsMerger.mergeDmgPositionMod(position);
        //卡牌buff(魔放)
        double quickBuff = servant.quickBuffN + inputData.getQuickBuffP();
        double artsBuff = servant.artsBuffN + inputData.getArtsBuffP();
        double busterBuff = servant.busterBuffN + inputData.getBusterBuffP();
        double effectiveBuff = ParamsMerger.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //首卡加成
        double firstCardMod = ParamsMerger.mergeDmgFirstCardMod(cardType1);
        //职阶系数
        double classAtkMod = ParamsMerger.mergeclassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = ParamsMerger.mergeAffinityMod(inputData.getAffinityType());
        //阵营克制
        double attributeMod = ParamsMerger.mergeAttributeMod(inputData.getAttributeType());
        //攻击buff
        double atkBuff = ParamsMerger.mergeBuffDebuff(inputData.getAtkBuff(), inputData.getAtkDown());
        //防御buff
        double defBuff = ParamsMerger.mergeBuffDebuff(inputData.getDefUp(), inputData.getDefDown());
        //特攻
        double specialBuff = inputData.getSpecialBuff();
        //特防
        double specialDefBuff = inputData.getSpecialDefBuff();
        //暴击buff
        //判断暴击
        boolean isCritical = ParamsMerger.isCritical(position,inputData.isCritical1(),inputData.isCritical2(),inputData.isCritical3());
        double criticalBuff = ParamsMerger.mergeCriticalBuff(isCritical, cardType, inputData.getCriticalUp(),
                inputData.getCriticalDown(), inputData.getCriticalQuick(), inputData.getCriticalArts(), inputData.getCriticalBuster());
        //暴击补正
        double criticalMod = ParamsMerger.mergeDmgCriticalMod(isCritical);
        double exDmgBuff = ParamsMerger.mergeExDmgBuff(cardType, isSameColor);
        //固伤
        double selfDmgBuff = inputData.getSelfDmgBuff();
        //固防
        double selfDmgDefBuff = inputData.getSelfDmgDefBuff();
        //红链
        double busterChainMod = ParamsMerger.mergeBusterChainMod(cardType, isBusterChain);

        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBuff, selfDmgBuff, selfDmgDefBuff, busterChainMod);
    }

    //todo
    private double npDmg(String cardType,double random) {
        //atk
        double atk = inputData.getAtk();
        //宝具倍率
        double npDmgMultiplier = inputData.getNpDmgMultiplier();
        //卡牌伤害倍率
        double cardDmgMultiplier = ParamsMerger.mergecardDmgMultiplier(cardType);
        //卡牌buff(魔放)
        double quickBuff = servant.quickBuffN + inputData.getQuickBuffP();
        double artsBuff = servant.artsBuffN + inputData.getArtsBuffP();
        double busterBuff = servant.busterBuffN + inputData.getBusterBuffP();
        //当前有效的卡牌buff
        double effectiveBuff = ParamsMerger.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);

        //职阶系数
        double classAtkMod = ParamsMerger.mergeclassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = ParamsMerger.mergeAffinityMod(inputData.getAffinityType());
        //阵营克制
        double attributeMod = ParamsMerger.mergeAttributeMod(inputData.getAttributeType());
        //攻击buff
        double atkBuff = ParamsMerger.mergeBuffDebuff(inputData.getAtkUp(), inputData.getAtkDown());
        //防御buff
        double defBuff = ParamsMerger.mergeBuffDebuff(inputData.getDefUp(), inputData.getDefDown());
        //特攻
        double specialBuff = inputData.getSpecialBuff();
        //特防
        double specialDefBuff = inputData.getSpecialDefBuff();
        //宝具威力
        double npPowerBuff = ParamsMerger.mergeBuffDebuff(inputData.getNpPowerUp(), inputData.getNpPowerDown());
        //宝具特攻
        double npSpecialBuff = inputData.getNpSpecialBuff();
        //固伤
        double selfDmgBuff = inputData.getSelfDmgBuff();
        //固防
        double selfDmgDefBuff = inputData.getSelfDmgDefBuff();
        return Formula.npDamageFormula(atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff);
    }

    // calc np
//    private OneTurnResult oneTurnNp(ServantEntity svt, InputData data) {
//        String cardType1 = data.cardType1;
//        double np1;
//        double np2;
//        double np3;
//        double np4;
//        return new OneTurnResult();
//    }

//    private double oneCardNpGeneration(FullData x, String cardType, int position) {
//
//        return ParamsMerger.isNp(cardType) ? npGeneration(x, cardType, position) : npNpGeneration(x, cardType);
//    }

//    private double npGeneration(FullData x, String cardType, int position, double enemyNpMod) {
//        ServantEntity svt = x.getSvt();
//        InputData data = x.getData();
//
//        //np获取率
//        double na = ParamsMerger.mergeNaHits(cardType, svt.getQuickNa(), svt.getArtsNa(), svt.getBusterNa(), svt.getExNa(), svt.getNpNa());
//        //hit数
//        double hits = ParamsMerger.mergeNaHits(cardType, svt.getQuickHit(), svt.getArtsHit(), svt.getBusterHit(), svt.getExHit(), svt.getNpHit());
//        //卡牌np倍率
//        double cardNpMultiplier = ParamsMerger.mergecardNpMultiplier(cardType);
//        //位置加成
//        double positionMod = ParamsMerger.mergeNpPositionMod(position);
//        //魔放
//        double quickBuff = svt.getQuickBuffN() + data.quickBuffP;
//        double artsBuff = svt.getArtsBuffN() + data.artsBuffP;
//        double busterBuff = svt.getBusterBuffN() + data.busterBuffP;
//        double effectiveBuff = ParamsMerger.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
//        //首卡加成
//        double firstCardMod = ParamsMerger.mergeNpFirstCardMod(x.getCardType1());
//        double npBuff = ParamsMerger.mergeBuffDebuff(data.npUp, data.npDown);
//        double criticalMod = ParamsMerger.mergeNpCriticalMod(data.isCritical, cardType);
//        double overkillMod = ParamsMerger.mergeNpOverkillMod(data.isOverkill);
//        //敌补正
//        return Formula.npGenerationFormula(na, hits, cardNpMultiplier, positionMod, effectiveBuff, firstCardMod,
//                npBuff, criticalMod, overkillMod, enemyNpMod);
//
//    }

//    private double npNpGeneration(FullData x, String cardType) {
//        //np类型
//        ServantEntity svt = x.getSvt();
//        if (svt.getNpType().equals("all")) {
//            double[] targets = x.getEnemysNpMod();
//            double np = 0;
//            for (int i = 0; i < targets.length; i++) {
//                np += npGeneration();
//            }
//            return np;
//        }
//        if (svt.getNpType().equals("support")) {
//            return 0;
//        }
//        return npGeneration();
//    }
    // calc star
    /*
    private double starDropRatePerHit(FullData x, String cardType, int position, double enemyStarMod) {
        ServantEntity svt = x.getSvt();
        FetchData data = x.getData();
        //svt带的
        double starRate = svt.getStarGeneration();
        //卡牌补正，受卡色、位置影响
        double cardStarMultiplier = ParamsMerger.mergeCardStarMultiplier(cardType,position);
        //魔放
        double quickBuff = svt.getQuickBuffN() + data.quickBuffP;
        double artsBuff = svt.getArtsBuffN() + data.artsBuffP;
        double busterBuff = svt.getBusterBuffN() + data.busterBuffP;
        double effectiveBuff = ParamsMerger.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //首卡补正quick 20% 非quick 0，判断卡色，返回结果
        double firstCardMod = ParamsMerger.mergeStarFirstCardMod(data.cardType1);
        //星星发生率Buff-星星发生率DeBuff
        double starRateBuff = ParamsMerger.mergeBuffDebuff(data.starRateUp,data.starRateDown);
        //暴击补正：暴击时计算此项。为定值[20%]
        double criticalMod = ParamsMerger.mergeStarCtriticalMod(data.isCritical);
        //敌方星星发生率Buff,此项基本不进行计算。
        double enemyStarBuff = 0;
        //常数1
        double overkillMultiplier = 1;
        //触发时，定值30%
        double overkillAdd = ParamsMerger.mergeOverkillAdd(isOverkill);
        return Formula.starDropRatePerHitFormula(starRate,cardStarMultiplier,effectiveBuff,firstCardMod,starRateBuff,
                criticalMod,enemyStarBuff,enemyStarMod,overkillMultiplier,overkillAdd);
    }

    private double npStarDropRatePerHit() {
        double starRate,
        double cardStarRate,
        double effectiveBuff,
        double starRateBuff,
        double enemyStarBuff,
        double randomMod,
        double overkillMultiplier,
        double overkillAdd,
        double enemyAmount
        return Formula.npStarDropRatePerHitFormula();
    }*/

    String calcLogs = "";

    String infoLog = "";

    private void dataToInfoLog(String svtName, String svtClass) {
        infoLog = LogManager.logInfo(svtName, svtClass);
    }

    private String dataToFinalLog() {
        return LogManager.logFinal(infoLog, calcLogs);
    }

}
