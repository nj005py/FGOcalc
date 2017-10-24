package com.phantancy.fgocalc.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.BaseUtils;
import com.phantancy.fgocalc.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2017/3/3.
 */
public class TrumpFrag extends BaseFrag {

    @BindView(R.id.ft_et_atk)
    EditText ftEtAtk;
    @BindView(R.id.ft_sp_lv)
    Spinner ftSpLv;
    @BindView(R.id.ft_rb_normal)
    RadioButton ftRbNormal;
    @BindView(R.id.ft_rb_weak)
    RadioButton ftRbWeak;
    @BindView(R.id.ft_rb_weakened)
    RadioButton ftRbWeakened;
    @BindView(R.id.ft_rg_weak)
    RadioGroup ftRgWeak;
    @BindView(R.id.ft_rb_tnormal)
    RadioButton ftRbTnormal;
    @BindView(R.id.ft_rb_tweak)
    RadioButton ftRbTweak;
    @BindView(R.id.ft_rb_tweakened)
    RadioButton ftRbTweakened;
    @BindView(R.id.ft_rg_team)
    RadioGroup ftRgTeam;
    @BindView(R.id.ft_rb_random_min)
    RadioButton ftRbRandomMin;
    @BindView(R.id.ft_rb_random_max)
    RadioButton ftRbRandomMax;
    @BindView(R.id.ft_rb_random_average)
    RadioButton ftRbRandomAverage;
    @BindView(R.id.ft_rb_random)
    RadioButton ftRbRandom;
    @BindView(R.id.ft_rg_random)
    RadioGroup ftRgRandom;
    @BindView(R.id.ft_et_atkup)
    EditText ftEtAtkup;
    @BindView(R.id.ft_et_busterup)
    EditText ftEtBusterup;
    @BindView(R.id.ft_et_artsup)
    EditText ftEtArtsup;
    @BindView(R.id.ft_et_quickup)
    EditText ftEtQuickup;
    @BindView(R.id.ft_et_trumpup)
    EditText ftEtTrumpup;
    @BindView(R.id.ft_tv_result)
    TextView ftTvResult;
    @BindView(R.id.ft_ll_input)
    LinearLayout ftLlInput;
    @BindView(R.id.ft_btn_calc)
    Button ftBtnCalc;
    @BindView(R.id.ft_btn_clean)
    Button ftBtnClean;
    @BindView(R.id.ft_iv_character)
    ImageView ftIvCharacter;
    @BindView(R.id.ft_v_character)
    View ftVCharacter;
    @BindView(R.id.ft_tv_character)
    TextView ftTvCharacter;
    @BindView(R.id.ft_rl_character)
    RelativeLayout ftRlCharacter;
    int[] lv;
    @BindView(R.id.ft_tb_buff)
    ToggleButton ftTbBuff;
    @BindView(R.id.ft_ll_buff)
    LinearLayout ftLlBuff;
    @BindView(R.id.ft_iv_color)
    ImageView ftIvColor;
    @BindView(R.id.ft_ll_hp)
    LinearLayout ftLlHp;
    @BindView(R.id.ft_et_defdown)
    EditText ftEtDefdown;
    @BindView(R.id.ft_et_spup)
    EditText ftEtSpup;
    @BindView(R.id.ft_et_trump_spup)
    EditText ftEtTrumpSpup;
    @BindView(R.id.ft_ll_calc)
    LinearLayout ftLlCalc;
    @BindView(R.id.ft_et_hp_total)
    EditText ftEtHpTotal;
    @BindView(R.id.ft_et_hp_left)
    EditText ftEtHpLeft;
    @BindView(R.id.ft_et_trump_extra_times)
    EditText ftEtTrumpExtraTimes;
    @BindView(R.id.ft_ll_extra_times)
    LinearLayout ftLlExtraTimes;

    private View view;
    private ServantItem servantItem;
    private int weakType = 1;//克制类型1 白值，2 克制，3 被克
    private int hpTotal = 0,hpLeft = 0;
    private int id = 0, atk = 0, overallAttack = 0;
    private double atkCor = 0.23, trumpTimes = 0, cardTimes = 0, cardBuff = 0, bCardBuff = 0, aCardBuff = 0, qCardBuff = 0,
            busterBuff, quickBuff, artsBuff,//用于接收职阶技能
            classCor = 0, weakCor = 1, teamCor = 1.0, randomCor = 0.9, atkBuff = 0, enemyDefence = 0,
            specialBuff = 0, specialDefence = 0, trumpPowerBuff = 0, trumpBuff = 1, solidBuff = 0, solidDefence = 0;
    private double extraTimes = 0;//双子需要额外倍率
    private String trumpColor = "", classType = "";
    private String result = "";//存放计算结果

    /**
     * ATK×攻击补正×[宝具倍率×卡牌伤害倍率×(1+卡牌BUFF)]×职阶补正×职阶相性补正×阵营相性补正×乱数补正×(1+攻击力BUFF—敌方防御力BUFF)×(1+特攻威力BUFF—敌方特防威力BUFF+宝具威力BUFF)×宝具特攻
     * +(固定伤害BUFF—敌方固定伤害BUFF)
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_trump, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }

    private void init() {
        int height = BaseUtils.getNavigationHeight(getContext());
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ftLlCalc.getLayoutParams();
        lp.bottomMargin = height;
        ftLlCalc.setLayoutParams(lp);
        lv = getResources().getIntArray(R.array.trump_lv);
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servants");
        if (servantItem != null) {
            id = servantItem.getId();
            //id66，131为双子需要显血
            if (id == 66 || id == 131) {
                ftLlHp.setVisibility(View.VISIBLE);
                //都需要血量信息
                ftLlHp.setVisibility(View.VISIBLE);
                //66r双子，附加倍率<超蓄力威力提升> (此倍率×自身已损失HP所占百分比,与宝具倍率加算)
                //131弓双子，HP降低，宝具威力提高【※总倍率＝攻击倍率+HP特攻倍率*（1—现在HP/最大HP)】
                if (id == 66) {
                    ftTbBuff.setChecked(true);
                    ftLlBuff.setVisibility(View.VISIBLE);
                    ftLlExtraTimes.setVisibility(View.VISIBLE);
                }
            } else {
                ftLlHp.setVisibility(View.GONE);
            }
            spInitSimple(mContext, lv, ftSpLv);
            classType = servantItem.getClass_type();
            trumpColor = servantItem.getTrump_color();
            busterBuff = servantItem.getBuster_buff();
            artsBuff = servantItem.getArts_buff();
            quickBuff = servantItem.getQuick_buff();
            trumpTimes = servantItem.getTrump_lv1();//默认1宝倍率
            cardTimes();
            classCor();
        }
        setListener();
    }

    private void setListener() {
        ftBtnCalc.setOnClickListener(this);
        ftBtnClean.setOnClickListener(this);
        ftRlCharacter.setOnClickListener(this);
        //宝具等级
        ftSpLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (servantItem != null) {
                    switch (position) {
                        case 0:
                            trumpTimes = servantItem.getTrump_lv1();
                            break;
                        case 1:
                            trumpTimes = servantItem.getTrump_lv2();
                            break;
                        case 2:
                            trumpTimes = servantItem.getTrump_lv3();
                            break;
                        case 3:
                            trumpTimes = servantItem.getTrump_lv4();
                            break;
                        case 4:
                            trumpTimes = servantItem.getTrump_lv5();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //职介相性
        ftRgWeak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ft_rb_normal:
                        weakType = 1;
                        break;
                    case R.id.ft_rb_weak:
                        weakType = 2;
                        break;
                    case R.id.ft_rb_weakened:
                        weakType = 3;
                        break;
                }
                weakCor();
            }
        });
        //阵营相应
        ftRgTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ft_rb_tnormal:
                        teamCor = 1.0;
                        break;
                    case R.id.ft_rb_tweak:
                        teamCor = 1.1;
                        break;
                    case R.id.ft_rb_tweakened:
                        teamCor = 0.9;
                        break;
                }
            }
        });
        //乱数补正
        ftRgRandom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ft_rb_random_min:
                        randomCor = 0.9;
                        break;
                    case R.id.ft_rb_random_max:
                        randomCor = 1.1;
                        break;
                    case R.id.ft_rb_random_average:
                        randomCor = (0.9 + 1.1) / 2;
                        break;
                    case R.id.ft_rb_random:
                        randomCor = getRan();
                        break;
                }
            }
        });
        //宝具设置开关
        ftTbBuff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ftLlBuff.setVisibility(View.VISIBLE);
                } else {
                    ftLlBuff.setVisibility(View.GONE);
                }
            }
        });
        ftTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("fgocalc_trump", ftTvResult.getText());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.displayShortToast(mContext, "结果已复制剪切板");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ft_btn_calc:
                if (validateData()) {
                    trumpInFact();
                }
                break;
            case R.id.ft_btn_clean:
                ftTvResult.setText("");
                result = "";
                break;
            case R.id.ft_rl_character:
                ftRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    //卡色Buff倍率
    private void cardTimes() {
        switch (trumpColor) {
            case "b":
                cardBuff = busterBuff + bCardBuff;
                cardTimes = 1.5;
                ftIvColor.setImageResource(R.mipmap.buster);
                break;
            case "a":
                cardBuff = artsBuff + aCardBuff;
                cardTimes = 1.0;
                ftIvColor.setImageResource(R.mipmap.arts);
                break;
            case "q":
                cardBuff = quickBuff + qCardBuff;
                cardTimes = 0.8;
                ftIvColor.setImageResource(R.mipmap.quick);
                break;
        }
    }

    //职介补正
    private void classCor() {
        String classCache = classType.toLowerCase();
        switch (classCache) {
            case "saber":
                classCor = 1.00;
                break;
            case "archer":
                classCor = 0.95;
                break;
            case "lancer":
                classCor = 1.05;
                break;
            case "rider":
                classCor = 1.0;
                break;
            case "caster":
                classCor = 0.9;
                break;
            case "assassin":
                classCor = 0.9;
                break;
            case "berserker":
                classCor = 1.1;
                break;
            case "ruler":
                classCor = 1.1;
                break;
            case "shielder":
                classCor = 1.0;
                break;
            case "alterego":
                classCor = 1.0;
                break;
            case "avenger":
                classCor = 1.1;
                break;
            case "beast":
                classCor = 1.0;
                break;
            case "mooncancer":
                classCor = 1.0;
                break;
        }
    }

    //克制系数
    private void weakCor() {
        switch (weakType) {
            case 1:
                weakCor = 1.0;
                break;
            case 2:
                if (classType.equals("Berserker")) {
                    weakCor = 1.5;
                } else {
                    weakCor = 2.0;
                }
                break;
            case 3:
                weakCor = 0.5;
                break;
        }
    }

    //乱数补正
    private double getRan() {
        return Math.random() * 0.2 + 0.9;
    }

    /**
     * ATK×攻击补正×[宝具倍率×卡牌伤害倍率×(1+卡牌BUFF)]×职阶补正×职阶相性补正×阵营相性补正×乱数补正×(1+攻击力BUFF—敌方防御力BUFF)×(1+特攻威力BUFF—敌方特防威力BUFF+宝具威力BUFF)×宝具特攻
     * +(固定伤害BUFF—敌方固定伤害BUFF)
     */
    private void trumpInFact() {
        double attack = atk * atkCor * (trumpTimes * cardTimes * (1 + cardBuff)) * classCor * weakCor * teamCor * randomCor *
                (1 + atkBuff + enemyDefence) * (1 + specialBuff - specialDefence + trumpPowerBuff) * trumpBuff + (solidBuff - solidDefence);
        int attackInt = (int) Math.floor(attack);
        overallAttack = attackInt;
        if (!notEmpty(result)) {
            result = "宝具伤害----->" + overallAttack;
        } else {
            result = result + "\n宝具伤害----->" + overallAttack;
        }
        tvSet(ftTvResult, result);
    }

    private boolean validateData() {
        String atkStr = etValue(ftEtAtk);
        if (!notEmpty(atkStr)) {
            setCharacter("ATK是必填项！\n能帮上点忙吗？");
            return false;
        } else {
            atk = Integer.valueOf(atkStr);
        }
        atkBuff = etPercent(ftEtAtkup);
        enemyDefence = etPercent(ftEtDefdown);
        trumpPowerBuff = etPercent(ftEtTrumpup);
        bCardBuff = etPercent(ftEtBusterup);
        aCardBuff = etPercent(ftEtArtsup);
        qCardBuff = etPercent(ftEtQuickup);
        specialBuff = etPercent(ftEtSpup);
        trumpBuff = etPercent(ftEtTrumpSpup);
        //无宝具特攻也要写1，不能为0
        if (trumpBuff == 0) {
            trumpBuff = 1;
        } else {
            trumpBuff = 1 + trumpBuff;
        }
        cardTimes();
        //双子特殊处理倍率
        if (id == 66 || id == 131) {
            hpTotal = etInt(ftEtHpTotal);
            hpLeft = etInt(ftEtHpLeft);
            if (hpTotal == 0 || hpLeft == 0) {
                setCharacter("hp信息不全！\n请填写完整");
                return false;
            }
            //66r双子，附加倍率<超蓄力威力提升> (此倍率×自身已损失HP所占百分比,与宝具倍率加算)
            //131弓双子，HP降低，宝具威力提高【※总倍率＝攻击倍率+HP特攻倍率*（1—现在HP/最大HP)】
            //66 r双子
            if (id == 66) {
                extraTimes = etPercent(ftEtTrumpExtraTimes);
                if (extraTimes == 0) {
                    setCharacter("骑阶双子的额外倍率必填！");
                    return false;
                }else{
                    trumpTimes = trumpTimes + extraTimes * (1 - (hpLeft / hpTotal));
                }
            }
            //131 a双子
            if (id == 131) {
                //弓双子额外倍率固定600%
                trumpTimes = trumpTimes + 6 * (1 - (hpLeft / hpTotal));
            }
        }
        return true;
    }

    private void setCharacter(String str){
        ftRlCharacter.setVisibility(View.VISIBLE);
        ftTvCharacter.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
        ftTvCharacter.setText(str);
    }
}
