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

import org.phantancy.fgocalc.entity.CalcConditionVO;
import org.phantancy.fgocalc.logic.CardLogic;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.common.Formula;
import org.phantancy.fgocalc.common.ParamsUtil;
import org.phantancy.fgocalc.data.BuffData;
import org.phantancy.fgocalc.data.repository.CalcRepository;
import org.phantancy.fgocalc.data.InfoBuilder;
import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository;
import org.phantancy.fgocalc.data.ServantAvatarData;
import org.phantancy.fgocalc.entity.BuffInputEntity;
import org.phantancy.fgocalc.entity.CardPickEntity;
import org.phantancy.fgocalc.entity.CalcEntity;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;
import org.phantancy.fgocalc.entity.ResultDmg;
import org.phantancy.fgocalc.entity.ResultEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.entity.SvtExpEntity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.phantancy.fgocalc.common.Constant.ENTRY_SINGLE;
import static org.phantancy.fgocalc.common.ParamsUtil.getCardDmgMultiplier;

//计算Activity ViewModel
public class CalcViewModel extends AndroidViewModel {
    final String TAG = "CalcViewModel";
    public int entry = ENTRY_SINGLE;
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

    //当前页(横向page)
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
    public LiveData<Integer> currentPage = mCurrentPage;

    public void setCurrentPage(int index) {
        mCurrentPage.setValue(index);
    }

    //纵向page 计算条件
    private MutableLiveData<Integer> mConditionPage = new MutableLiveData<>();
    public LiveData<Integer> conditionPage = mConditionPage;

    public void setConditionPage(int index) {
        mConditionPage.setValue(index);
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
        //初始化计算参数
        calcEntity.setAtk(sumAtk());
        calcEntity.setHp(hpDefault);
        calcEntity.setHpLeft(hpLeft);
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
    private final MutableLiveData<List<CardPickEntity>> mCardPicks = new MutableLiveData<>();

    public LiveData<List<CardPickEntity>> cardPicks = mCardPicks;

    public void parsePickCards() {
        String x = servant.cards;
        int id = 0;
        List<CardPickEntity> list = new ArrayList<>();
        //平A
        for (char y : x.toCharArray()) {
            list.add(CardLogic.parseCardPickEntity(id, y));
            id++;
        }
        //宝具
        if (calcEntity.getNpEntity() != null) {
            list.add(CardLogic.parseCardPickNp(id, calcEntity.getNpEntity().npColor));
        }
        mCardPicks.setValue(list);
    }

    //更新宝具
    public void parsePickCards(NoblePhantasmEntity np) {
        String x = servant.cards;
        int id = 0;
        List<CardPickEntity> list = new ArrayList<>();
        //平A
        for (char y : x.toCharArray()) {
            list.add(CardLogic.parseCardPickEntity(id, y));
            id++;
        }
        //宝具
        list.add(CardLogic.parseCardPickNp(id, np.npColor));
        mCardPicks.setValue(list);
    }

    private List<CardPickEntity> pickedCards = new ArrayList<>();

    public void setPickedCards(List<CardPickEntity> pickedCards) {
        this.pickedCards = pickedCards;
    }

    //获取fgowiki链接
    public String getServantWiki(ServantEntity svt) {
        return new StringBuilder(Constant.WIKI_URL)
                .append(svt.id)
                .toString();
    }

    public String getServantMooncell(ServantEntity svt) {
        return new StringBuilder(Constant.MOONCELL_URL)
                .append(svt.nameLink)
                .toString();
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
    public void saveCondition(String atk, String hp, String hpLeft, double[] enemyNpMods,
                              double[] enemyStarMods, CalcConditionVO conditionVO) {
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
        //敌人
        calcEntity.setEnemysNpMod(enemyNpMods);
        calcEntity.setEnemysStarMod(enemyStarMods);
        /**
         * 保存UI信息
         */
        calcEntity.setCalcConditionVO(conditionVO);
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
     * @param it 宝具
     * @param lv 宝具等级
     */
    public void parseNpBuff(NoblePhantasmEntity it, String lv) {
        //buff随宝具
        String lvBuff = "";
        switch (lv) {
            case "一宝":
                lvBuff = it.ocBuffLv1;
                break;
            case "二宝":
                lvBuff = it.ocBuffLv2;
                break;
            case "三宝":
                lvBuff = it.ocBuffLv3;
                break;
            case "四宝":
                lvBuff = it.ocBuffLv4;
                break;
            case "五宝":
                lvBuff = it.ocBuffLv5;
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
        calcEntity.setUiBuffs(buffs);
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
        //选择的卡
        this.pickedCards = pickedCards;

        if (pickedCards != null && pickedCards.size() == 3) {
            //设置卡片
            calcEntity.setCardType1(pickedCards.get(0).getName());
            calcEntity.setCardType2(pickedCards.get(1).getName());
            calcEntity.setCardType3(pickedCards.get(2).getName());
            //计算伤害
            //伤害随机
            double dmgRandomMax = 1.1;
            double dmgRandomMin = 0.9;
//            StringBuilder resBuilder = new StringBuilder();
//            resBuilder.append("max:\n")
//                    .append(fourCardsDmg(dmgRandomMax).getDes())
//                    .append("min:\n")
//                    .append(fourCardsDmg(dmgRandomMin).getDes());
            //res
            ResultDmg max = fourCardsDmg(dmgRandomMax);
            ResultDmg min = fourCardsDmg(dmgRandomMin);

            //计算Np np随机由敌人职阶决定
//            resBuilder.append(fourCardsNp().getDes())
//                    .append("\n");

            ResultDmg np = fourCardsNp();
            //计算打星
//            resBuilder.append(fourCardsStar().getDes());
            ResultDmg star = fourCardsStar();
            //显示结果
//            calcResult.setValue(resBuilder.toString());
            handleResult(min, max, np, star);
        }

    }

    private MutableLiveData<List<ResultEntity>> mResultList = new MutableLiveData<>();
    public LiveData<List<ResultEntity>> resultList = mResultList;

    //处理结果
    private void handleResult(ResultDmg min, ResultDmg max, ResultDmg np, ResultDmg star) {

        ResultEntity res1 = new ResultEntity(ResultEntity.Companion.getTYPE_CARD(),
                calcEntity.getCardType1(), min.getC1(), max.getC1(),"", np.getC1(), star.getC1(),
                "", ServantAvatarData.getServantAvatar(servant.id));
        ResultEntity res2 = new ResultEntity(ResultEntity.Companion.getTYPE_CARD(),
                calcEntity.getCardType2(), min.getC2(), max.getC2(),"", np.getC2(), star.getC2(), "", ServantAvatarData.getServantAvatar(servant.id));
        ResultEntity res3 = new ResultEntity(ResultEntity.Companion.getTYPE_CARD(),
                calcEntity.getCardType3(), min.getC3(), max.getC3(),"", np.getC3(), star.getC3(), "", ServantAvatarData.getServantAvatar(servant.id));
        ResultEntity res4 = new ResultEntity(ResultEntity.Companion.getTYPE_CARD(),
                calcEntity.getCardType4(), min.getC4(), max.getC4(), "",np.getC4(), star.getC4(), "", ServantAvatarData.getServantAvatar(servant.id));

        StringBuilder sumBuilder = new StringBuilder();
        sumBuilder.append("伤害总计：")
                .append(min.getSum())
                .append("-")
                .append(max.getSum())
                .append("\n");
        sumBuilder.append("np总计：")
                .append(np.getSum())
                .append("\n");
        sumBuilder.append("打星总计：")
                .append(star.getSum())
                .append("\n");
        ResultEntity resSum = new ResultEntity(ResultEntity.Companion.getTYEP_SUM(),
                "", "", "", "", "","", sumBuilder.toString(), ServantAvatarData.getServantAvatar(servant.id));
        List<ResultEntity> list = new ArrayList<>();
        list.add(res1);
        list.add(res2);
        list.add(res3);
        list.add(res4);
        list.add(resSum);
        mResultList.setValue(list);
    }

    //清理结果
    public void cleanResult() {
        List<ResultEntity> list = new ArrayList<>();
        mResultList.setValue(list);
    }

    //计算4张卡的伤害
    private ResultDmg fourCardsDmg(double random) {
        /**
         * 需要3张卡判断的参数
         */
        //是否同色
        calcEntity.setSameColor(ParamsUtil.isCardsSameColor(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3()));
        //是否红链
        calcEntity.setBusterChain(ParamsUtil.isCardsBusterChain(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3()));
        //每张卡伤害结果
        double res1 = dmgCalc(calcEntity.getCardType1(), 1, random);
        double res2 = dmgCalc(calcEntity.getCardType2(), 2, random);
        double res3 = dmgCalc(calcEntity.getCardType3(), 3, random);
        double res4 = dmgCalc(calcEntity.getCardType4(), 4, random);
        res1 = Math.floor(res1);
        res2 = Math.floor(res2);
        res3 = Math.floor(res3);
        res4 = Math.floor(res4);
        double sum = res1 + res2 + res3 + res4;
        String des = MessageFormat.format("c1:{0}\nc2:{1}\nc3:{2}\nc4:{3}\nsum:{4}\n\n",
                ParamsUtil.dmgResFormat(res1),
                ParamsUtil.dmgResFormat(res2),
                ParamsUtil.dmgResFormat(res3),
                ParamsUtil.dmgResFormat(res4),
                ParamsUtil.dmgResFormat(sum));
        ResultDmg result = new ResultDmg(
                ParamsUtil.dmgResFormat(res1),
                ParamsUtil.dmgResFormat(res2),
                ParamsUtil.dmgResFormat(res3),
                ParamsUtil.dmgResFormat(res4),
                ParamsUtil.dmgResFormat(sum),
                des
        );
        return result;
    }

    /**
     * 伤害计算准备，计算单卡伤害
     *
     * @param cardType
     * @param position
     * @param random
     * @return
     */
    private double dmgCalc(String cardType, int position, double random) {
        //判断卡片类型，宝具卡或普攻卡
        return ParamsUtil.isNp(cardType) ? npDmg(cardType, random) : dmg(cardType, position, random);
    }

    //普攻伤害
    private double dmg(String cardType, int position, double random) {
        /**
         * 准备条件
         */
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
        //atk
        double atk = calcEntity.getAtk();
        //卡牌伤害倍率
        double cardDmgMultiplier = ParamsUtil.getCardDmgMultiplier(cardType);
        //位置补正
        double positionMod = ParamsUtil.getDmgPositionMod(position);
        /**
         * 宝具前，平A需要考虑:全buff+被动buff
         * 宝具，平A不考虑
         * 宝具后，平A需要考虑:全buff+被动buff+伤害前buff+伤害后buff=宝具前+伤害前buff+伤害后buff
         */
        //判断宝具卡前还是后，按需取buff
        double quickBuff = 0;
        double artsBuff = 0;
        double busterBuff = 0;
        double effectiveBuff = 0;

        if (!ParamsUtil.isEx(cardType)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP);
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP);
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP);
            //宝具前buff
            if (position > npPosition) {
                //宝具后buff
                quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_BE) + safeGetBuffMap(BuffData.QUICK_UP_AF);
                artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_BE) + safeGetBuffMap(BuffData.ARTS_UP_AF);
                busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_BE) + safeGetBuffMap(BuffData.BUSTER_UP_AF);
            }
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        }

        //首卡加成
        double firstCardMod = ParamsUtil.getDmgFirstCardMod(cardType1);
        //职阶系数
        double classAtkMod = ParamsUtil.getClassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = calcEntity.getAffinityMod();
        //阵营克制
        double attributeMod = calcEntity.getAttributeMod();
        //攻击buff
        double atkBuff = safeGetBuffMap(BuffData.ATK_UP);
        if (position > npPosition) {
            atkBuff = atkBuff + safeGetBuffMap(BuffData.ATK_UP_BE) + safeGetBuffMap(BuffData.ATK_UP_AF);
        }
        //防御buff
        double defBuff = 0d;
        //特攻
        double specialBuff = safeGetBuffMap(BuffData.SPECIAL_UP);
        //特防
        double specialDefBuff = 0d;
        /**
         * 暴击buff
         */
        //判断暴击
        boolean isCritical = ParamsUtil.isCritical(position, calcEntity.isCritical1(),
                calcEntity.isCritical2(),
                calcEntity.isCritical3());
        double criticalBuff = ParamsUtil.getCriticalBuff(isCritical, cardType,
                safeGetBuffMap(BuffData.CRITICAL_UP),
                0d,
                safeGetBuffMap(BuffData.CRITICAL_QUICK_UP),
                safeGetBuffMap(BuffData.CRITICAL_ARTS_UP),
                safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP));
        criticalBuff = criticalBuff + servant.criticalBuffN;
        //宝具卡后
        if (position > npPosition) {
            criticalBuff = criticalBuff + ParamsUtil.getCriticalBuff(isCritical, cardType,
                    safeGetBuffMap(BuffData.CRITICAL_UP_BE) + safeGetBuffMap(BuffData.CRITICAL_UP_AF),
                    0,
                    safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_BE) + safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_AF),
                    safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_BE) + safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_AF),
                    safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_BE) + safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_AF));
        }
        //暴击补正
        double criticalMod = ParamsUtil.getDmgCriticalMod(isCritical);
        //ex卡补正
        double exDmgBouns = ParamsUtil.getExDmgBouns(cardType, isSameColor);
        //固伤
        double selfDmgBuff = safeGetBuffMap(BuffData.SELF_DAMAGE_UP);
        //固防
        double selfDmgDefBuff = 0d;
        //红链
        double busterChainMod = ParamsUtil.mergeBusterChainMod(cardType, isBusterChain);

        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBouns, selfDmgBuff, selfDmgDefBuff, busterChainMod);
    }

    //宝具伤害
    private double npDmg(String cardType, double random) {
        /**
         * 需要3张卡判断的参数
         */
        /**
         * 单独卡计算的部分
         */
        //atk
        double atk = calcEntity.getAtk();
        //宝具倍率
        double npDmgMultiplier = getNpMultiplier(servant.id, calcEntity.getNpDmgMultiplier(),
                safeGetBuffMap(BuffData.NP_MULTIPLIER_UP_BE), (double) (calcEntity.getHpLeft()), (double) (calcEntity.getHp()));
        //卡牌伤害倍率
        double cardDmgMultiplier = getCardDmgMultiplier(cardType);
        /**
         * 宝具buff:全buff+被动buff+伤害前buff
         */
        /**
         * 卡牌buff(魔放)
         */
        double quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP) + safeGetBuffMap(BuffData.QUICK_UP_BE);
        double artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP) + safeGetBuffMap(BuffData.ARTS_UP_BE);
        double busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP) + safeGetBuffMap(BuffData.BUSTER_UP_BE);
        //最终用于计算的魔放结果
        double effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //职阶系数
        double classAtkMod = ParamsUtil.getClassAtkMod(servant.classType);
        //职阶克制
        double affinityMod = calcEntity.getAffinityMod();
        //阵营克制
        double attributeMod = calcEntity.getAttributeMod();
        //攻击buff
        double atkBuff = safeGetBuffMap(BuffData.ATK_UP) + safeGetBuffMap(BuffData.ATK_UP_BE);
        //防御buff
        double defBuff = 0d;
        //特攻
        double specialBuff = safeGetBuffMap(BuffData.SPECIAL_UP);
        //特防
        double specialDefBuff = 0d;
        //宝具威力
        double npPowerBuff = safeGetBuffMap(BuffData.NP_POWER_UP);
        //宝具特攻
        double npSpecialBuff = safeGetBuffMap(BuffData.NP_SPECICAL_UP_BE);
        //宝具特攻不能为0
        if (npSpecialBuff == 0) {
            npSpecialBuff = 1;
        }
        //固伤
        double selfDmgBuff = safeGetBuffMap(BuffData.SELF_DAMAGE_UP);
        //固防
        double selfDmgDefBuff = 0;
        Log.d(TAG, MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} ", atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff));
        return Formula.npDamageFormula(atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff);
    }

    /**
     * np计算，宝具充能计算
     */
//    private OneTurnResult oneTurnNp(ServantEntity svt, InputData data) {
//        String cardType1 = data.cardType1;
//        double np1;
//        double np2;
//        double np3;
//        double np4;
//        return new OneTurnResult();
//    }
    //计算4张卡的np
    private ResultDmg fourCardsNp() {
        double enemyNpMod = calcEntity.getEnemysNpMod()[0];
        double[] res1 = npGenCalc(calcEntity.getCardType1(), 1, enemyNpMod);
        double[] res2 = npGenCalc(calcEntity.getCardType2(), 2, enemyNpMod);
        double[] res3 = npGenCalc(calcEntity.getCardType3(), 3, enemyNpMod);
        double[] res4 = npGenCalc(calcEntity.getCardType4(), 4, enemyNpMod);
        double sum = getNpRes(res1, calcEntity.getCardType1()) + getNpRes(res2, calcEntity.getCardType2())
                + getNpRes(res3, calcEntity.getCardType3()) + getNpRes(res4, calcEntity.getCardType4());
        String des = MessageFormat.format("c1:{0}\nc2:{1}\nc3:{2}\nc4{3}\nsum:{4}",
                parseNpRes(res1, calcEntity.getCardType1()),
                parseNpRes(res2, calcEntity.getCardType2()),
                parseNpRes(res3, calcEntity.getCardType3()),
                parseNpRes(res4, calcEntity.getCardType4()),
                ParamsUtil.npGenResFormat(sum));
        ResultDmg result = new ResultDmg(
                parseNpRes(res1, calcEntity.getCardType1()),
                parseNpRes(res2, calcEntity.getCardType2()),
                parseNpRes(res3, calcEntity.getCardType3()),
                parseNpRes(res4, calcEntity.getCardType4()),
                ParamsUtil.npGenResFormat(sum),
                des
        );
        return result;
    }

    private String parseNpRes(double[] res, String cardType) {
        StringBuilder builder = new StringBuilder();
        if (ParamsUtil.isNp(cardType)) {
            String sum = ParamsUtil.npGenResFormat(getNpRes(res, cardType));
            builder.append(sum)
                    .append(" (");
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                builder.append(ParamsUtil.npGenResFormat(res[i]));
                if (i < calcEntity.getEnemyCount() - 1) {
                    builder.append(", ");
                }
            }
            builder.append(")");
        } else {
            builder.append(ParamsUtil.npGenResFormat(res[0]));
        }
        return builder.toString();
    }

    private double getNpRes(double[] res, String cardType) {
        if (ParamsUtil.isNp(cardType)) {
            double sum = 0;
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                sum += res[i];
            }
            return sum;
        } else {
            return res[0];
        }
    }

    private double[] npGenCalc(String cardType, int position, double enemyNpMod) {
        return ParamsUtil.isNp(cardType) ? npNpGenDelegate(cardType, position, enemyNpMod)
                : npGen(cardType, position, enemyNpMod);
    }

    //普攻np
    private double[] npGen(String cardType, int position, double enemyNpMod) {
        /**
         * 准备条件
         */
        //首卡类型，看染色
        String cardType1 = calcEntity.getCardType1();
        //宝具卡位置
        int npPosition = ParamsUtil.getNpPosition(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3());
        /**
         * 单独卡计算的部分
         */
        //np获取率
        double na = ParamsUtil.getNa(cardType, servant.quickNa, servant.artsNa, servant.busterNa, servant.exNa, servant.npHit);
        //hit数
        double hits = ParamsUtil.getHits(cardType, servant.quickHit, servant.artsHit, servant.busterHit, servant.exHit, servant.npHit);
        //卡牌np倍率
        double cardNpMultiplier = ParamsUtil.getCardNpMultiplier(cardType);
        //位置加成
        double positionMod = ParamsUtil.getNpPositionMod(position);
        /**
         * 宝具前，平A需要考虑:全buff+被动buff
         * 宝具，平A不考虑
         * 宝具后，平A需要考虑:全buff+被动buff+伤害前buff+伤害后buff=宝具前+伤害前buff+伤害后buff
         */
        //魔放
        double quickBuff = 0;
        double artsBuff = 0;
        double busterBuff = 0;
        double effectiveBuff = 0;

        if (!ParamsUtil.isEx(cardType)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP);
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP);
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP);
            //宝具前buff
            if (position > npPosition) {
                //宝具后buff
                quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_BE) + safeGetBuffMap(BuffData.QUICK_UP_AF);
                artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_BE) + safeGetBuffMap(BuffData.ARTS_UP_AF);
                busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_BE) + safeGetBuffMap(BuffData.BUSTER_UP_AF);
            }
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        }
        //首卡加成
        double firstCardMod = ParamsUtil.getNpFirstCardMod(cardType1);
        //黄金率
        double npBuff = safeGetBuffMap(BuffData.NPC_UP);
        if (position > npPosition) {
            //宝具后
            npBuff = npBuff + safeGetBuffMap(BuffData.NPC_UP_BE) + safeGetBuffMap(BuffData.NPC_UP_AF);
        }
        //暴击补正
        //判断暴击
        boolean isCritical = ParamsUtil.isCritical(position, calcEntity.isCritical1(),
                calcEntity.isCritical2(),
                calcEntity.isCritical3());
        double criticalMod = ParamsUtil.getNpCriticalMod(isCritical, cardType);
        //overkill补正
        boolean isOverkill = ParamsUtil.isOverkill(position, calcEntity.isOverkill1(), calcEntity.isOverkill2(),
                calcEntity.isOverkill3(), calcEntity.isOverkill4());
        double overkillMod = ParamsUtil.getNpOverkillMod(isOverkill);
        double[] res = new double[3];
        res[0] = Formula.npGenerationFormula(na, hits, cardNpMultiplier, positionMod, effectiveBuff, firstCardMod,
                npBuff, criticalMod, overkillMod, enemyNpMod);
        return res;
    }

    //宝具np多情况计算
    private double[] npNpGenDelegate(String cardType, int position, double enemyNpMod) {
        double[] res = new double[3];
        //辅助宝具不用算直接为0
        if (servant.npType.equals("support")) {
            res[0] = 0;
            return res;
        }
        //单体宝具只算第一个敌人
        if (servant.npType.equals("one")) {
            res[0] = npNpGen(cardType, position, enemyNpMod);
        }
        //光炮宝具算整个敌人列表
        if (servant.npType.equals("all")) {
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                //判断是否设置敌人
                enemyNpMod = calcEntity.getEnemysNpMod()[i];
                //计算
                res[i] = npNpGen(cardType, position, enemyNpMod);
            }
            return res;
        }
        return res;
    }

    //宝具np计算
    private double npNpGen(String cardType, int position, double enemyNpMod) {
        //np获取率
        double na = ParamsUtil.getNa(cardType, servant.quickNa, servant.artsNa, servant.busterNa, servant.exNa, servant.npNa);
        //hit数
        double hits = ParamsUtil.getHits(cardType, servant.quickHit, servant.artsHit, servant.busterHit, servant.exHit, servant.npHit);
        //卡牌np倍率
        double cardNpMultiplier = ParamsUtil.getCardNpMultiplier(cardType);
        /**
         * 宝具前，平A需要考虑:全buff+被动buff
         * 宝具，平A不考虑
         * 宝具后，平A需要考虑:全buff+被动buff+伤害前buff+伤害后buff=宝具前+伤害前buff+伤害后buff
         */
        //魔放
        double quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP) + safeGetBuffMap(BuffData.QUICK_UP_BE);
        double artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP) + safeGetBuffMap(BuffData.ARTS_UP_BE);
        double busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP) + safeGetBuffMap(BuffData.BUSTER_UP_BE);
        //最终用于计算的魔放结果
        double effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        //黄金率
        double npBuff = safeGetBuffMap(BuffData.NPC_UP) + safeGetBuffMap(BuffData.NPC_UP_BE);
        //overkill补正
        boolean isOverkill = ParamsUtil.isOverkill(position, calcEntity.isOverkill1(), calcEntity.isOverkill2(),
                calcEntity.isOverkill3(), calcEntity.isOverkill4());
        double overkillMod = ParamsUtil.getNpOverkillMod(isOverkill);
        return Formula.npNpGenerationFormula(na, hits, cardNpMultiplier, effectiveBuff, npBuff, overkillMod, enemyNpMod);
    }

    /**
     * 打星计算
     */

    //每hit掉星率
    private double starDropRatePerHit(String cardType, int position, double enemyStarMod) {
        /**
         * 准备条件
         */
        //首卡类型，看染色
        String cardType1 = calcEntity.getCardType1();
        //宝具卡位置
        int npPosition = ParamsUtil.getNpPosition(calcEntity.getCardType1(), calcEntity.getCardType2(), calcEntity.getCardType3());
        /**
         * 单独卡计算的部分
         */
        //出星率
        double starRate = servant.starGeneration;
        //卡牌补正，受卡色、位置影响
        double cardStarMultiplier = ParamsUtil.getCardStarMultiplier(cardType, position);
        //魔放
        double quickBuff = 0;
        double artsBuff = 0;
        double busterBuff = 0;
        double effectiveBuff = 0;

        if (!ParamsUtil.isEx(cardType)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP);
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP);
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP);
            //宝具前buff
            if (position > npPosition) {
                //宝具后buff
                quickBuff = quickBuff + safeGetBuffMap(BuffData.QUICK_UP_BE) + safeGetBuffMap(BuffData.QUICK_UP_AF);
                artsBuff = artsBuff + safeGetBuffMap(BuffData.ARTS_UP_BE) + safeGetBuffMap(BuffData.ARTS_UP_AF);
                busterBuff = busterBuff + safeGetBuffMap(BuffData.BUSTER_UP_BE) + safeGetBuffMap(BuffData.BUSTER_UP_AF);
            }
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        }
        //首卡补正quick 20% 非quick 0，判断卡色，返回结果
        double firstCardMod = ParamsUtil.getStarFirstCardMod(cardType1);
        //星星发生率Buff-星星发生率DeBuff
        double starRateBuff = servant.starGenerationN + safeGetBuffMap(BuffData.STAR_UP);
        if (position > npPosition) {
            //宝具后
            starRateBuff = starRateBuff + safeGetBuffMap(BuffData.STAR_UP_BE) + safeGetBuffMap(BuffData.STAR_UP_AF);
        }
        //暴击补正：暴击时计算此项。为定值[20%]
        //判断暴击
        boolean isCritical = ParamsUtil.isCritical(position, calcEntity.isCritical1(),
                calcEntity.isCritical2(),
                calcEntity.isCritical3());
        double criticalMod = ParamsUtil.getStarCtriticalMod(isCritical);
        //敌方星星发生率Buff,此项基本不进行计算。
        double enemyStarBuff = 0;
        //常数1
        double overkillMultiplier = 1;
        //触发时，定值30%
        boolean isOverkill = ParamsUtil.isOverkill(position, calcEntity.isOverkill1(), calcEntity.isOverkill2(),
                calcEntity.isOverkill3(), calcEntity.isOverkill4());
        double overkillAdd = ParamsUtil.getOverkillAdd(isOverkill);
        return Formula.starDropRatePerHitFormula(starRate, cardStarMultiplier, effectiveBuff, firstCardMod, starRateBuff,
                criticalMod, enemyStarBuff, enemyStarMod, overkillMultiplier, overkillAdd);
    }

    //宝具每hit掉星率
    private double npStarDropRatePerHit(String cardType, int position, double enemyStarMod) {
        double starRate = servant.starGeneration;
        double cardStarRate = ParamsUtil.getCardStarRate(cardType);
        //魔放
        double quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP) + safeGetBuffMap(BuffData.QUICK_UP_BE);
        double artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP) + safeGetBuffMap(BuffData.ARTS_UP_BE);
        double busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP) + safeGetBuffMap(BuffData.BUSTER_UP_BE);
        double effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff);
        double starRateBuff = servant.starGenerationN + safeGetBuffMap(BuffData.STAR_UP) + safeGetBuffMap(BuffData.STAR_UP_BE);
        //敌方星星发生率Buff,此项基本不进行计算。
        double enemyStarBuff = 0;
        //常数1
        double overkillMultiplier = 1;
        //触发时，定值30%
        boolean isOverkill = ParamsUtil.isOverkill(position, calcEntity.isOverkill1(), calcEntity.isOverkill2(),
                calcEntity.isOverkill3(), calcEntity.isOverkill4());
        double overkillAdd = ParamsUtil.getOverkillAdd(isOverkill);
        return Formula.npStarDropRatePerHitFormula(starRate, cardStarRate, effectiveBuff, starRateBuff,
                enemyStarBuff, enemyStarMod, overkillMultiplier, overkillAdd);
    }

    //计算产星数量
    private double starDropNumber(double rate, int hit) {
        if (rate > 3d) {
            rate = 3d;
        }
        double count = rate * hit;
        if (count < 0d) {
            count = 0d;
        }
        return count;
    }

    //宝具打星多情况计算
    private double[] npStarDropNumberDelegate(String cardType, int position, int hit) {
        double[] res = new double[3];
        //辅助宝具不用算直接为0
        if (servant.npType.equals("support")) {
            res[0] = 0;
            return res;
        }
        //单体宝具只算第一个敌人
        if (servant.npType.equals("one")) {
            res[0] = starDropNumber(npStarDropRatePerHit(cardType, position, calcEntity.getEnemysStarMod()[0]), hit);
        }
        //光炮宝具算整个敌人列表
        if (servant.npType.equals("all")) {
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                //计算
                res[i] = starDropNumber(npStarDropRatePerHit(cardType, position, calcEntity.getEnemysStarMod()[i]), hit);
            }
            return res;
        }
        return res;
    }

    //判断卡牌类型，分开计算
    private double[] calcStarDropNumber(String cardType, int position, double enemyStarMod) {
        int hit = ParamsUtil.getHits(cardType, servant.quickHit, servant.artsHit, servant.busterHit, servant.exHit, servant.npHit);
        double[] res = new double[3];
        if (ParamsUtil.isNp(cardType)) {
            //宝具卡
            res = npStarDropNumberDelegate(cardType, position, hit);
        } else {
            //普攻
            res[0] = starDropNumber(starDropRatePerHit(cardType, position, enemyStarMod), hit);
        }
        return res;
    }

    //计算4张卡的暴击星
    private ResultDmg fourCardsStar() {
        double enemyStarMod = calcEntity.getEnemysStarMod()[0];
        double[] res1 = calcStarDropNumber(calcEntity.getCardType1(), 1, enemyStarMod);
        double[] res2 = calcStarDropNumber(calcEntity.getCardType2(), 2, enemyStarMod);
        double[] res3 = calcStarDropNumber(calcEntity.getCardType3(), 3, enemyStarMod);
        double[] res4 = calcStarDropNumber(calcEntity.getCardType4(), 4, enemyStarMod);
        double sum = getStarRes(res1, calcEntity.getCardType1()) + getStarRes(res2, calcEntity.getCardType2())
                + getStarRes(res3, calcEntity.getCardType3()) + getStarRes(res4, calcEntity.getCardType4());
        String des = MessageFormat.format("c1:{0}\nc2:{1}\nc3:{2}\nc4{3}\nsum:{4}",
                parseStarRes(res1, calcEntity.getCardType1()),
                parseStarRes(res2, calcEntity.getCardType2()),
                parseStarRes(res3, calcEntity.getCardType3()),
                parseStarRes(res4, calcEntity.getCardType4()),
                ParamsUtil.starDropResFormat(sum)
        );
        ResultDmg result = new ResultDmg(
                parseStarRes(res1, calcEntity.getCardType1()),
                parseStarRes(res2, calcEntity.getCardType2()),
                parseStarRes(res3, calcEntity.getCardType3()),
                parseStarRes(res4, calcEntity.getCardType4()),
                ParamsUtil.starDropResFormat(sum),
                des
        );
        return result;
    }

    private double getStarRes(double[] res, String cardType) {
        if (ParamsUtil.isNp(cardType)) {
            double sum = 0;
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                sum += res[i];
            }
            return sum;
        } else {
            return res[0];
        }
    }

    private String parseStarRes(double[] res, String cardType) {
        StringBuilder builder = new StringBuilder();
        if (ParamsUtil.isNp(cardType)) {
            String sum = ParamsUtil.starDropResFormat(getNpRes(res, cardType));
            builder.append(sum)
                    .append(" (");
            for (int i = 0; i < calcEntity.getEnemyCount(); i++) {
                builder.append(ParamsUtil.starDropResFormat(res[i]));
                if (i < calcEntity.getEnemyCount() - 1) {
                    builder.append(", ");
                }
            }
            builder.append(")");

        } else {
            builder.append(ParamsUtil.starDropResFormat(res[0]));
        }
        return builder.toString();
    }

    //安全取buff
    public double safeGetBuffMap(String key) {
        if (calcEntity.getBuffMap().get(key) == null) {
            return 0;
        } else {
            return calcEntity.getBuffMap().get(key);
        }
    }

    //宝具倍率处理
    public double getNpMultiplier(int svtId, double npDmgMultiplier, double npMultiplierUp, double hpLeft, double hp) {
        if (svtId == 66 || svtId == 161) {
            //骑双子,土方岁三
            npDmgMultiplier = npDmgMultiplier + (npMultiplierUp * (1 - (hpLeft / hp)));
        } else if (svtId == 131) {
            //弓双子
            npDmgMultiplier = npDmgMultiplier + (6 * (1 - (hpLeft / hp)));
        } else {
            npDmgMultiplier = npDmgMultiplier + npMultiplierUp;
        }
        return npDmgMultiplier;
    }

    //土方岁三 技能3buff
    public double getHijiKataCriticalBuff() {
        double buff = 0.2 + 0.8 * (1 - (calcEntity.getHpLeft() / calcEntity.getHp()));
        return buff;
    }
}
