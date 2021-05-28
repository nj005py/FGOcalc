package org.phantancy.fgocalc.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.common.Formula;
import org.phantancy.fgocalc.common.LogManager;
import org.phantancy.fgocalc.common.ParamsUtil;
import org.phantancy.fgocalc.data.BuffData;
import org.phantancy.fgocalc.data.CalcRepository;
import org.phantancy.fgocalc.data.ConditionData;
import org.phantancy.fgocalc.data.InfoBuilder;
import org.phantancy.fgocalc.data.NoblePhantasmRepository;
import org.phantancy.fgocalc.entity.BuffInputEntity;
import org.phantancy.fgocalc.entity.CardPickEntity;
import org.phantancy.fgocalc.entity.CalcEntity;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;
import org.phantancy.fgocalc.entity.OneTurnResult;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.entity.SvtExpEntity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.phantancy.fgocalc.common.ParamsUtil.mergeDmgPositionMod;
import static org.phantancy.fgocalc.common.ParamsUtil.mergecardDmgMultiplier;

//计算Activity ViewModel
public class CalcViewModel extends AndroidViewModel {
    final String TAG = "CalcViewModel";
    private ServantEntity servant;
    //经验列表
    List<SvtExpEntity> svtExpEntities;
    //输入的数据，采集用户输入数据
    public CalcEntity calcEntity;

    //等级atk
    private int atkLv = 0;
    //礼装atk
    private int atkEssence = 0;
    //芙芙atk
    private int atkFou = 0;
    //默认atk
    private int atkDefault = 0;

    public String getAtkDefaultKey() {
        return atkDefault + "";
    }

    //等级hp
    private int hpLv = 0;
    //默认hp
    private int hpDefault = 0;

    public String getHpDefaultKey() {
        return hpDefault + "";
    }

    //剩余hp
    private int hpLeft = 0;

    public String getHpLeftKey() {
        return hpLeft + "";
    }

    //当前页
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();

    public LiveData<Integer> getCurrentPage() {
        return mCurrentPage;
    }

    //数据源
    private CalcRepository calcRepository;
    private NoblePhantasmRepository npRepository;

    //入口
    public CalcViewModel(Application app) {
        super(app);
        calcRepository = new CalcRepository(app);
        npRepository = new NoblePhantasmRepository(app);
        calcEntity = new CalcEntity();
    }

    public void setServant(ServantEntity servant) {
        this.servant = servant;
        atkLv = servant.atkDefault;
        atkDefault = servant.atkDefault;
        hpDefault = servant.hpDefault;
        hpLeft = servant.hpDefault;
    }

    public ServantEntity getServant() {
        return servant;
    }

    /**
     * 信息页
     */
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
        //平A
        for (char y : x.toCharArray()) {
            list.add(parseCardPickEntity(id, y));
            id++;
        }
        //宝具
        list.add(parseCardPickNp(id, servant.npColor));
        cardPicks.setValue(list);
    }

    //更新宝具
    public void parsePickCards(NoblePhantasmEntity np) {
        String x = servant.cards;
        int id = 0;
        List<CardPickEntity> list = new ArrayList<>();
        //平A
        for (char y : x.toCharArray()) {
            list.add(parseCardPickEntity(id, y));
            id++;
        }
        //宝具
        list.add(parseCardPickNp(id, np.npColor));
        cardPicks.setValue(list);
    }

    //解析指令卡
    private CardPickEntity parseCardPickEntity(int id, char color) {
        if (color == 'q') {
            return new CardPickEntity(id, "q", R.drawable.quick);
        }
        if (color == 'a') {
            return new CardPickEntity(id, "a", R.drawable.arts);
        }
        return new CardPickEntity(id, "b", R.drawable.buster);

    }

    //解析宝具卡
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

    /**
     * 条件页
     */
    public int getRewardLv() {
        return servant.rewardLv;
    }

    //合计条件atk
    public int sumAtk() {
        Log.d(TAG, "atkLv:" + atkLv + " atkEssence:" + atkEssence + " atkFou:" + atkFou);
        //等级atk+礼装atk+芙芙atk
        int res = atkLv + atkEssence + atkFou;
        calcEntity.setAtk(res);
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

    //数据库查询从者等级数据
    public void setSvtExpEntities(List<SvtExpEntity> exps) {
        if (exps != null) {
            this.svtExpEntities = exps;
        }
//        Flowable.create(new FlowableOnSubscribe<List<SvtExpEntity>>() {
//            @Override
//            public void subscribe(FlowableEmitter<List<SvtExpEntity>> emitter) throws Exception {
//                int id = servant.id;
//                emitter.onNext(calcRepository.getSvtExpList(id));
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<SvtExpEntity>>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(List<SvtExpEntity> svtExpEntities) {
//                        CalcViewModel.this.svtExpEntities = svtExpEntities;
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    public LiveData<List<SvtExpEntity>> getSvtExpEntities() {
        return calcRepository.getSvtExpList(servant.id);
    }

    //数据库查询宝具信息
    public LiveData<List<NoblePhantasmEntity>> getNPEntities(int svtId) {
        return npRepository.getNoblePhantasmEntities(svtId);
    }

    //todo 保存条件数据
    public void saveCondition(String atk, String hp, String hpLeft) {
        calcEntity.setSavedCondition(true);
        //职阶相性
        Log.d(TAG, "职阶相性：" + calcEntity.getAffinityMod());
        //阵营相性
        Log.d(TAG, "阵营相性：" + calcEntity.getAttributeMod());
        /**
         * 宝具倍率问题
         */
        //选择宝具？
        //宝具是否强化？
        //宝具lv
        /**
         * atk问题
         */
        //atk
        calcEntity.setAtk(Double.parseDouble(atk));
        Log.d(TAG, "atk：" + calcEntity.getAtk());
        //总hp
        calcEntity.setHp(Double.parseDouble(hp));
        Log.d(TAG, "总hp:" + calcEntity.getHp());
        //剩余hp
        calcEntity.setHpLeft(Double.parseDouble(hpLeft));
        Log.d(TAG, "剩余hp：" + calcEntity.getHpLeft());
        /**
         * 敌方单位设置
         */
        //敌人数量
        //敌方1
        //敌方2
        //敌方3
    }

    //保存职阶克制
    public void saveAffinity(double affinityMod) {
        calcEntity.setAffinityMod(affinityMod);

    }

    //保存阵营克制
    public void saveAttribute(double attributeMod) {
        calcEntity.setAttributeMod(attributeMod);
    }

    /**
     * buff页
     */
    //获取buff表
    public List<BuffInputEntity> getBuffInputList() {
        return BuffData.buildBuffs();
    }

    //缓存上次buff
    public SimpleArrayMap<String, Double> preNpBuff = new SimpleArrayMap<>();
    /**
     *
     * @param it 宝具
     * @param lv 宝具等级
     */
    public void parseNpBuff(NoblePhantasmEntity it, String lv) {
        //buff随宝具
        String lvBuff = "";
        switch (lv) {
            case "一宝":
                lvBuff = it.buffLv1;
                break;
            case "二宝":
                lvBuff = it.buffLv2;
                break;
            case "三宝":
                lvBuff = it.buffLv3;
                break;
            case "四宝":
                lvBuff = it.buffLv4;
                break;
            case "五宝":
                lvBuff = it.buffLv5;
                break;

        }
        SimpleArrayMap<String, Double> lvMap = buffStrToMap(lvBuff);
        //buff随oc
        String ocBuff = it.oc_buff;
        SimpleArrayMap<String, Double> ocMap = buffStrToMap(ocBuff);

        //更新buff
        setBuffFromNp(lvMap);
        setBuffFromNp(ocMap);
    }

    /**
     * 字符串buff信息，解析为map
     *
     * @param buffStr
     */
    private SimpleArrayMap<String, Double> buffStrToMap(String buffStr) {
        if (TextUtils.isEmpty(buffStr)) {
            return null;
        }
        //解析宝具字符串
        JsonObject buffObj = (JsonObject) new JsonParser().parse(buffStr);
        //解析宝具json
        SimpleArrayMap<String, Double> buffMap = new SimpleArrayMap<>();
        if (buffObj != null && buffObj.size() > 0) {
            for (Map.Entry<String, JsonElement> entry : buffObj.entrySet()) {
                if (entry.getValue() != null) {
                    double v = entry.getValue().getAsDouble();
                    buffMap.put(entry.getKey(), v);
                }
                Log.d(TAG, MessageFormat.format("{0} {1}", entry.getKey(), entry.getValue()));
            }
        }
        return buffMap;
    }

    //宝具自带的buff
    private MutableLiveData<SimpleArrayMap<String, Double>> buffFromNp = new MutableLiveData<>();

    /**
     * 选宝具，设置宝具自带buff
     *
     * @param x
     */
    public void setBuffFromNp(SimpleArrayMap<String, Double> x) {
        if (x != null && x.size() > 0) {
            buffFromNp.setValue(x);
        }
    }

    /**
     * 设置宝具倍率
     *
     * @return
     */
    public void setNpDmgMultiplier(NoblePhantasmEntity it, int lv) {
        switch (lv) {
            case 0:
                calcEntity.setNpDmgMultiplier(it.npLv1);
                break;
            case 1:
                calcEntity.setNpDmgMultiplier(it.npLv2);
                break;
            case 2:
                calcEntity.setNpDmgMultiplier(it.npLv3);
                break;
            case 3:
                calcEntity.setNpDmgMultiplier(it.npLv4);
                break;
            case 4:
                calcEntity.setNpDmgMultiplier(it.npLv5);
                break;
        }
    }

    public LiveData<SimpleArrayMap<String, Double>> getBuffFromNp() {
        return buffFromNp;
    }

    //todo 保存buff信息
    public void saveBuff(List<BuffInputEntity> buffs) {
        calcEntity.setSavedBuff(true);
        SimpleArrayMap<String, Double> buffMap = new SimpleArrayMap<String, Double>();
        for (BuffInputEntity x : buffs) {
//            Log.d(TAG, MessageFormat.format("{0} {1} {2}",x.getKey(),x.getValue(),x.getType()));
            switch (x.getType()) {
                //整数
                case 0:
                    buffMap.put(x.getKey(), x.getValue());
                    break;
                //百分号
                case 1:
                    buffMap.put(x.getKey(), x.getValue() / 100);
                    break;
            }
        }
        calcEntity.setBuffMap(buffMap);
    }

    /**
     * 计算页
     */
    private MutableLiveData<String> calcResult = new MutableLiveData<>();

    public LiveData<String> getCalcResult() {
        return calcResult;
    }

    //点击计算
    public void clickCalc(List<CardPickEntity> pickedCards) {
        //buff信息
        //条件信息

        //选择的卡
        this.pickedCards = pickedCards;
        //计算伤害
        if (pickedCards != null && pickedCards.size() == 3) {
            calcEntity.setCardType1(pickedCards.get(0).getName());
            calcEntity.setCardType2(pickedCards.get(1).getName());
            calcEntity.setCardType3(pickedCards.get(2).getName());
            //todo 完整计算结果
//            OneTurnResult x = oneTurnDmg(servant,inputData);
//            String dmgResult = LogManager.resultLog(inputData,x);
//            calcResult.setValue(dmgResult);
            //todo 计算
            double res = npDmg();
            calcResult.setValue(MessageFormat.format("平A: {0}，宝具伤害: {1}", calcDmg(), res));
        }
        //计算Np
        //计算打星
    }


    //计算一张卡
    //计算伤害，返回最大，最小，平均
    //计算np，返回最大，最小，平均
    //计算打星，返回最大，最小，平均


    //calc dmg
    private OneTurnResult oneTurnDmg(ServantEntity svt, CalcEntity data) {
        //同色
        calcEntity.setSameColor(ParamsUtil.isCardsSameColor(data.getCardType1(), data.getCardType2(), data.getCardType3()));
        //红链
        calcEntity.setBusterChain(ParamsUtil.isCardsBusterChain(data.getCardType1(), data.getCardType2(), data.getCardType3()));
        //伤害随机
        double dmgRandomMax = 1.1;
        double dmgRandomMin = 0.9;
        double dmgRandomAvg = 1.0;

        //4 card dmg
//        FullData minParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomMin);
        double min1 = oneCardDmg(data.getCardType1(), 1, dmgRandomMin);
        double min2 = oneCardDmg(data.getCardType2(), 2, dmgRandomMin);
        double min3 = oneCardDmg(data.getCardType3(), 3, dmgRandomMin);
        double min4 = oneCardDmg(data.getCardType4(), 4, dmgRandomMin);
        double sumMin = min1 + min2 + min3 + min4;
        //max
//        FullData maxParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomMax);
        double max1 = oneCardDmg(data.getCardType1(), 1, dmgRandomMax);
        double max2 = oneCardDmg(data.getCardType2(), 2, dmgRandomMax);
        double max3 = oneCardDmg(data.getCardType3(), 3, dmgRandomMax);
        double max4 = oneCardDmg(data.getCardType4(), 4, dmgRandomMax);
        double sumMax = max1 + max2 + max3 + max4;
        //avg
//        FullData avgParcel = pack(svt, data, cardType1, isSameColor, isBusterChain, dmgRandomAvg);
        double avg1 = oneCardDmg(data.getCardType1(), 1, dmgRandomAvg);
        double avg2 = oneCardDmg(data.getCardType2(), 2, dmgRandomAvg);
        double avg3 = oneCardDmg(data.getCardType3(), 3, dmgRandomAvg);
        double avg4 = oneCardDmg(data.getCardType4(), 4, dmgRandomAvg);
        double sumAvg = avg1 + avg2 + avg3 + avg4;
        return new OneTurnResult(min1, min2, min3, min4, sumMin,
                max1, max2, max3, max4, sumMax,
                avg1, avg2, avg3, avg4, sumAvg);
    }

    //计算一张卡的伤害
    private double oneCardDmg(String cardType, int position, double random) {
        //判断卡片类型，宝具卡或普攻卡
        return ParamsUtil.isNp(cardType) ? dmg(cardType, position, random) : npDmg();
    }

    //Todo 普攻伤害
    private double dmg(String cardType, int position, double random) {
        String cardType1 = calcEntity.getCardType1();
        boolean isSameColor = calcEntity.isSameColor();
        boolean isBusterChain = calcEntity.isBusterChain();
        //atk
        double atk = calcEntity.getAtk();
        //卡牌伤害倍率
        double cardDmgMultiplier = mergecardDmgMultiplier(cardType);
        //位置补正
        double positionMod = mergeDmgPositionMod(position);
        //卡牌buff(魔放)
        double quickBuff = servant.quickBuffN + calcEntity.getQuickBuffP();
        double artsBuff = servant.artsBuffN + calcEntity.getArtsBuffP();
        double busterBuff = servant.busterBuffN + calcEntity.getBusterBuffP();
        double effectiveBuff = ParamsUtil.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //首卡加成
        double firstCardMod = ParamsUtil.mergeDmgFirstCardMod(cardType1);
        //职阶系数
        double classAtkMod = ParamsUtil.mergeclassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = calcEntity.getAffinityMod();
        //阵营克制
        double attributeMod = ParamsUtil.mergeAttributeMod(calcEntity.getAttributeType());
        //攻击buff
        double atkBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getAtkBuff(), calcEntity.getAtkDown());
        //防御buff
        double defBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getDefUp(), calcEntity.getDefDown());
        //特攻
        double specialBuff = calcEntity.getSpecialBuff();
        //特防
        double specialDefBuff = calcEntity.getSpecialDefBuff();
        //暴击buff
        //判断暴击
        boolean isCritical = ParamsUtil.isCritical(position, calcEntity.isCritical1(), calcEntity.isCritical2(), calcEntity.isCritical3());
        double criticalBuff = ParamsUtil.mergeCriticalBuff(isCritical, cardType, calcEntity.getCriticalUp(),
                calcEntity.getCriticalDown(), calcEntity.getCriticalQuick(), calcEntity.getCriticalArts(), calcEntity.getCriticalBuster());
        //暴击补正
        double criticalMod = ParamsUtil.mergeDmgCriticalMod(isCritical);
        double exDmgBuff = ParamsUtil.mergeExDmgBuff(cardType, isSameColor);
        //固伤
        double selfDmgBuff = calcEntity.getSelfDmgBuff();
        //固防
        double selfDmgDefBuff = calcEntity.getSelfDmgDefBuff();
        //红链
        double busterChainMod = ParamsUtil.mergeBusterChainMod(cardType, isBusterChain);

        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBuff, selfDmgBuff, selfDmgDefBuff, busterChainMod);
    }

    //todo 笼统计算伤害
    private double calcDmg() {
        /**
         * 如果未保存条件，初始化inputData
         */
        if (!calcEntity.isSavedCondition()) {
            calcEntity.setAtk(sumAtk());
            calcEntity.setHp(hpDefault);
            calcEntity.setHpLeft(hpLeft);
            //职阶相性
            calcEntity.setAffinityType(ConditionData.getAffinityKeys()[0]);
            //阵营相性
            calcEntity.setAttributeType(ConditionData.getAttributeKeys()[0]);
        }

        /**
         * 如果未保存条件，初始化buffMap
         */
        if (!calcEntity.isSavedBuff()) {

        }

        /**
         * 需要3张卡判断的参数
         */
        //是否同色
        calcEntity.setSameColor(ParamsUtil.isCardsSameColor(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3()));
        //是否红链
        calcEntity.setBusterChain(ParamsUtil.isCardsBusterChain(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3()));
        //伤害随机
        double dmgRandomMax = 1.1;
        double dmgRandomMin = 0.9;
        double dmgRandomAvg = 1.0;
        //设置个平均值吧
        double random = dmgRandomAvg;
        //首卡类型，看染色
        String cardType1 = calcEntity.getCardType1();
        //看看是不是同色卡链
        boolean isSameColor = calcEntity.isSameColor();
        //看看是不是三红加固伤
        boolean isBusterChain = calcEntity.isBusterChain();
        //宝具卡位置
        int npPosition = ParamsUtil.getNpPosition(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3());
        /**
         * 单独卡计算的部分
         */
        //选个卡计算吧
        String cardType = calcEntity.getCardType1();
        int position = 1;
        //atk
        double atk = calcEntity.getAtk();
        //卡牌伤害倍率
        double cardDmgMultiplier = ParamsUtil.mergecardDmgMultiplier(cardType);
        //位置补正
        double positionMod = ParamsUtil.mergeDmgPositionMod(position);
        /**
         * 卡牌buff(魔放)
         * 1，被动buff，这个稳
         * 2，全局buff，输入的全上
         * 3，判断宝具前，宝具后buff，有选择地上
         */
        double quickBuff = servant.quickBuffN + calcEntity.getQuickBuffP();
        double artsBuff = servant.artsBuffN + calcEntity.getArtsBuffP();
        double busterBuff = servant.busterBuffN + calcEntity.getBusterBuffP();
        //判断宝具卡前还是后，按需取buff
        if (position < npPosition) {
            //todo 宝具前buff
            quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_BE);
            artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_BE);
            busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_BE);
        } else {
            //宝具后buff
            quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_AF);
            artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_AF);
            busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_AF);
        }

        //最终用于计算的魔放结果
        double effectiveBuff = ParamsUtil.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //首卡加成
        double firstCardMod = ParamsUtil.mergeDmgFirstCardMod(cardType1);
        //职阶系数
        double classAtkMod = ParamsUtil.mergeclassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = ParamsUtil.mergeAffinityMod(calcEntity.getAffinityType());
        //阵营克制
        double attributeMod = ParamsUtil.mergeAttributeMod(calcEntity.getAttributeType());
        //攻击buff
        double atkBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getAtkBuff(), calcEntity.getAtkDown());
        if (position < npPosition) {
            atkBuff = atkBuff + safeGetBuffMap(BuffData.ATK_UP_BE);
        } else {
            atkBuff = atkBuff + safeGetBuffMap(BuffData.ATK_UP_AF);
        }
        //防御buff
        double defBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getDefUp(), calcEntity.getDefDown());
        //特攻
        double specialBuff = calcEntity.getSpecialBuff();
        //特防
        double specialDefBuff = calcEntity.getSpecialDefBuff();
        //暴击buff
        //判断暴击
        boolean isCritical = ParamsUtil.isCritical(position, calcEntity.isCritical1(), calcEntity.isCritical2(), calcEntity.isCritical3());
        double criticalBuff = ParamsUtil.mergeCriticalBuff(isCritical, cardType, calcEntity.getCriticalUp(),
                calcEntity.getCriticalDown(), calcEntity.getCriticalQuick(), calcEntity.getCriticalArts(), calcEntity.getCriticalBuster());
        if (position < npPosition) {
            criticalBuff = criticalBuff + ParamsUtil.mergeCriticalBuff(isCritical, cardType,
                    safeGetBuffMap(BuffData.CRITICAL_UP_BE),
                    0, safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_BE),
                    safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_BE),
                    safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_BE));
        } else {
            criticalBuff = criticalBuff + ParamsUtil.mergeCriticalBuff(isCritical, cardType,
                    safeGetBuffMap(BuffData.CRITICAL_UP_AF),
                    0, safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_AF),
                    safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_AF),
                    safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_AF));
        }

        //暴击补正
        double criticalMod = ParamsUtil.mergeDmgCriticalMod(isCritical);
        double exDmgBuff = ParamsUtil.mergeExDmgBuff(cardType, isSameColor);
        //固伤
        double selfDmgBuff = calcEntity.getSelfDmgBuff();
        //固防
        double selfDmgDefBuff = calcEntity.getSelfDmgDefBuff();
        //红链
        double busterChainMod = ParamsUtil.mergeBusterChainMod(cardType, isBusterChain);

        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBuff, selfDmgBuff, selfDmgDefBuff, busterChainMod);
    }

    //todo 宝具伤害
    private double npDmg() {
        /**
         * 如果未保存条件，初始化inputData
         */
        if (!calcEntity.isSavedCondition()) {
            calcEntity.setAtk(sumAtk());
            calcEntity.setHp(hpDefault);
            calcEntity.setHpLeft(hpLeft);
            //职阶相性
            calcEntity.setAffinityType(ConditionData.getAffinityKeys()[0]);
            //阵营相性
            calcEntity.setAttributeType(ConditionData.getAttributeKeys()[0]);
        }

        /**
         * 如果未保存条件，初始化buffMap
         */
        if (!calcEntity.isSavedBuff()) {

        }
        /**
         * 需要3张卡判断的参数
         */
        //伤害随机
        double dmgRandomMax = 1.1;
        double dmgRandomMin = 0.9;
        double dmgRandomAvg = 1.0;
        //设置个平均值吧
        double random = dmgRandomAvg;
        //宝具卡位置
        int npPosition = ParamsUtil.getNpPosition(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3());
        /**
         * 单独卡计算的部分
         */
        //选个卡计算吧
        String cardType = calcEntity.getCardType2();
        int position = 2;
        //atk
        double atk = calcEntity.getAtk();
        //宝具倍率
        double npDmgMultiplier = calcEntity.getNpDmgMultiplier();
        //卡牌伤害倍率
        double cardDmgMultiplier = mergecardDmgMultiplier(cardType);
        /**
         * 卡牌buff(魔放)
         * 1，被动buff，这个稳
         * 2，全局buff，输入的全上
         * 3，判断宝具前，宝具后buff，有选择地上
         */
        double quickBuff = servant.quickBuffN + calcEntity.getQuickBuffP();
        double artsBuff = servant.artsBuffN + calcEntity.getArtsBuffP();
        double busterBuff = servant.busterBuffN + calcEntity.getBusterBuffP();
        //判断宝具卡前还是后，按需取buff
        if (position < npPosition) {
            //todo 宝具前buff
            quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_BE);
            artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_BE);
            busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_BE);
        } else {
            //宝具后buff
            quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_AF);
            artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_AF);
            busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_AF);
        }
        //最终用于计算的魔放结果
        double effectiveBuff = ParamsUtil.mergeEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);

        //职阶系数
        double classAtkMod = ParamsUtil.mergeclassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = ParamsUtil.mergeAffinityMod(calcEntity.getAffinityType());
        //阵营克制
        double attributeMod = ParamsUtil.mergeAttributeMod(calcEntity.getAttributeType());
        //攻击buff
        double atkBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getAtkUp(), calcEntity.getAtkDown());
        if (position < npPosition) {
            atkBuff = atkBuff + safeGetBuffMap(BuffData.ATK_UP_BE);
        } else {
            atkBuff = atkBuff + safeGetBuffMap(BuffData.ATK_UP_AF);
        }
        //防御buff
        double defBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getDefUp(), calcEntity.getDefDown());
        //特攻
        double specialBuff = calcEntity.getSpecialBuff();
        //特防
        double specialDefBuff = calcEntity.getSpecialDefBuff();
        //宝具威力
        double npPowerBuff = ParamsUtil.mergeBuffDebuff(calcEntity.getNpPowerUp(), calcEntity.getNpPowerDown());
        //宝具特攻
        double npSpecialBuff = calcEntity.getNpSpecialBuff();
        //宝具特攻不能为0
        if (npSpecialBuff == 0) {
            npSpecialBuff = 1;
        }
        //固伤
        double selfDmgBuff = calcEntity.getSelfDmgBuff();
        //固防
        double selfDmgDefBuff = calcEntity.getSelfDmgDefBuff();
        Log.d(TAG, MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} ", atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff));
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

    //安全取buff
    public double safeGetBuffMap(String key) {
        if (calcEntity.getBuffMap().get(key) == null) {
            return 0;
        } else {
            return calcEntity.getBuffMap().get(key);
        }
    }

}
