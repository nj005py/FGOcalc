package com.phantancy.fgocalc.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.item.CardItem;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.BaseUtils;
import com.phantancy.fgocalc.util.SharedPreferencesUtils;
import com.phantancy.fgocalc.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2017/2/7.
 */
public class AtkFrag extends BaseFrag {

    @BindView(R.id.fa_et_atk)
    EditText faEtAtk;
    @BindView(R.id.fa_sp_card1)
    Spinner faSpCard1;
    @BindView(R.id.fa_sp_card2)
    Spinner faSpCard2;
    @BindView(R.id.fa_sp_card3)
    Spinner faSpCard3;
    @BindView(R.id.fa_rb_normal)
    RadioButton faRbNormal;
    @BindView(R.id.fa_rb_weak)
    RadioButton faRbWeak;
    @BindView(R.id.fa_rb_weakened)
    RadioButton faRbWeakened;
    @BindView(R.id.fa_rg_weak)
    RadioGroup faRgWeak;
    @BindView(R.id.fa_rb_tnormal)
    RadioButton faRbTnormal;
    @BindView(R.id.fa_rb_tweak)
    RadioButton faRbTweak;
    @BindView(R.id.fa_rb_tweakened)
    RadioButton faRbTweakened;
    @BindView(R.id.fa_rg_team)
    RadioGroup faRgTeam;
    @BindView(R.id.fa_tv_result)
    TextView faTvResult;
    @BindView(R.id.fa_ll_input)
    LinearLayout faLlInput;
    @BindView(R.id.fa_btn_calc)
    Button faBtnCalc;
    @BindView(R.id.fa_btn_clean)
    Button faBtnClean;
    @BindView(R.id.fa_rb_random_min)
    RadioButton faRbRandomMin;
    @BindView(R.id.fa_rb_random_max)
    RadioButton faRbRandomMax;
    @BindView(R.id.fa_rb_random_average)
    RadioButton faRbRandomAverage;
    @BindView(R.id.fa_rb_random)
    RadioButton faRbRandom;
    @BindView(R.id.fa_rg_random)
    RadioGroup faRgRandom;
    @BindView(R.id.fa_cb_cr1)
    CheckBox faCbCr1;
    @BindView(R.id.fa_cb_cr2)
    CheckBox faCbCr2;
    @BindView(R.id.fa_cb_cr3)
    CheckBox faCbCr3;
    @BindView(R.id.fa_et_atkup)
    EditText faEtAtkup;
    @BindView(R.id.fa_et_busterup)
    EditText faEtBusterup;
    @BindView(R.id.fa_et_artsup)
    EditText faEtArtsup;
    @BindView(R.id.fa_et_quickup)
    EditText faEtQuickup;
    @BindView(R.id.fa_et_criticalup)
    EditText faEtCriticalup;
    @BindView(R.id.fa_iv_character)
    ImageView faIvCharacter;
    @BindView(R.id.fa_v_character)
    View faVCharacter;
    @BindView(R.id.fa_tv_character)
    TextView faTvCharacter;
    @BindView(R.id.fa_rl_character)
    RelativeLayout faRlCharacter;
    @BindView(R.id.fa_tb_buff)
    ToggleButton faTbBuff;
    @BindView(R.id.fa_et_defdown)
    EditText faEtDefdown;
    @BindView(R.id.fa_et_spup)
    EditText faEtSpup;
    @BindView(R.id.fa_ll_buff)
    LinearLayout faLlBuff;
    @BindView(R.id.fa_ll_calc)
    LinearLayout faLlCalc;
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private int atk = 0, overallAttack = 0;
    private String result = "", cardType1, cardType2, cardType3;
    private String[] cardValues = {"b", "a", "q"};
    private double enemyDefence = 0, specialDefence = 0, solidDefence = 0, busterChain = 0, teamCor = 1.0, randomCor = 0.9,
            bCardBuff = 0, aCardBuff = 0, qCardBuff = 0, criticalBuff = 0, atkBuff = 0;
    private double specialBuff = 0;//特攻
    private boolean ifEx = false, ifCr1, ifCr2, ifCr3;
    private ServantItem servantItem;
    private int weakType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_atk, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        init();
    }

    private void init() {
        int height = BaseUtils.getNavigationHeight(getContext());
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)faLlCalc.getLayoutParams();
        lp.bottomMargin = height;
        faLlCalc.setLayoutParams(lp);
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servants");
        //声明一个简单simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, getListData(), R.layout.item_card_type,
                new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
        faSpCard1.setAdapter(simpleAdapter);
        faSpCard2.setAdapter(simpleAdapter);
        faSpCard3.setAdapter(simpleAdapter);
        setListener();
    }

    private void setListener() {
        faBtnCalc.setOnClickListener(this);
        faBtnClean.setOnClickListener(this);
        faRlCharacter.setOnClickListener(this);
        //宝具设置开关
        faTbBuff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    faLlBuff.setVisibility(View.VISIBLE);
                } else {
                    faLlBuff.setVisibility(View.GONE);
                }
            }
        });
        faSpCard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //parent为一个Map结构的和数据
//                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
//                Toast.makeText(MainActivity.this,
//                        map.get("name").toString(),Toast.LENGTH_SHORT).show();
                cardType1 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType1 = cardValues[0];
            }

        });

        faSpCard2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cardType2 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType2 = cardValues[0];
            }

        });

        faSpCard3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cardType3 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType3 = cardValues[0];
            }

        });

        faCbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr1 = true;
                } else {
                    ifCr1 = false;
                }
            }
        });

        faCbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr2 = true;
                } else {
                    ifCr2 = false;
                }
            }
        });

        faCbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr3 = true;
                } else {
                    ifCr3 = false;
                }
            }
        });

        faRgWeak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fa_rb_normal:
                        weakType = 1;
                        break;
                    case R.id.fa_rb_weak:
                        weakType = 2;
                        break;
                    case R.id.fa_rb_weakened:
                        weakType = 3;
                        break;
                }
            }
        });

        faRgTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fa_rb_tnormal:
                        teamCor = 1.0;
                        break;
                    case R.id.fa_rb_tweak:
                        teamCor = 1.1;
                        break;
                    case R.id.fa_rb_tweakened:
                        teamCor = 0.9;
                        break;
                }
            }
        });

        faRgRandom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fa_rb_random_min:
                        randomCor = 0.9;
                        break;
                    case R.id.fa_rb_random_max:
                        randomCor = 1.1;
                        break;
                    case R.id.fa_rb_random_average:
                        randomCor = (0.9 + 1.1) / 2;
                        break;
                    case R.id.fa_rb_random:
                        randomCor = getRan();
                        break;
                }
            }
        });

        faTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("fgocalc_np", faTvResult.getText());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.displayShortToast(mContext, "结果已复制剪切板");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fa_btn_calc:
                if (faEtAtk.getText() == null || faEtAtk.getText().toString().isEmpty()) {
//                    Toast.makeText(mContext, "请输入atk", Toast.LENGTH_SHORT).show();
                    faRlCharacter.setVisibility(View.VISIBLE);
                    faTvCharacter.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
                    SharedPreferencesUtils.setParam(mContext, "ifLily", false);
                } else {
                    atk = Integer.valueOf(faEtAtk.getText().toString());
                    boolean ifsameColor = false;
                    if (cardType1.equals(cardType2) && cardType2.equals(cardType3)) {
                        ifsameColor = true;
                        if (cardType1.equals("b")) {
                            busterChain = 0.2;
                        } else {
                            busterChain = 0;
                        }
                    } else {
                        ifsameColor = false;
                    }
                    Log.d(TAG, " teamCor:" + teamCor + " ifsameColor:" + ifsameColor +
                            " busterChain:" + busterChain);
                    //判断攻击buff
                    if (notEmpty(etValue(faEtAtkup))) {
                        atkBuff = Double.valueOf(etValue(faEtAtkup));
                        atkBuff = atkBuff / 100;
                    } else {
                        atkBuff = 0;
                    }
                    //判断红魔放
                    if (notEmpty(etValue(faEtBusterup))) {
                        bCardBuff = Double.valueOf(etValue(faEtBusterup));
                        bCardBuff = bCardBuff / 100;
                    } else {
                        bCardBuff = 0;
                    }
                    //判断蓝魔放
                    if (notEmpty(etValue(faEtArtsup))) {
                        aCardBuff = Double.valueOf(etValue(faEtArtsup));
                        aCardBuff = aCardBuff / 100;
                    } else {
                        aCardBuff = 0;
                    }
                    //判断绿魔放
                    if (notEmpty(etValue(faEtQuickup))) {
                        qCardBuff = Double.valueOf(etValue(faEtQuickup));
                        qCardBuff = qCardBuff / 100;
                    } else {
                        qCardBuff = 0;
                    }
                    //判断暴击威力提升
                    if (notEmpty(etValue(faEtCriticalup))) {
                        criticalBuff = Double.valueOf(etValue(faEtCriticalup));
                        criticalBuff = criticalBuff / 100;
                    } else {
                        criticalBuff = 0;
                    }
                    //判断减防
                    enemyDefence = etPercent(faEtDefdown);
                    //判断特攻
                    specialBuff = etPercent(faEtSpup);
//                    Card card1 = new Card(cardType1, 1, 0, cardType1, class_type, 0,
//                            0, 0, false, 0, false);
//                    Card card2 = new Card(cardType2, 2, 0, cardType1, class_type, 0,
//                            0, 0, false, 0, false);
//                    Card card3 = new Card(cardType3, 3, 0, cardType1, class_type, 0,
//                            0, 0, false, 0, false);
//                    Card card4 = new Card("ex", 4, 0, cardType1, class_type, 0,
//                            0, 0, false, 0, ifsameColor);
                    //从者，位置，卡色，首卡，同色，暴击,克制
                    CardItem card1 = new CardItem(servantItem, 1, cardType1, cardType1, ifsameColor, ifCr1, weakType,
                            bCardBuff, aCardBuff, qCardBuff, criticalBuff, atkBuff);
                    CardItem card2 = new CardItem(servantItem, 2, cardType2, cardType1, ifsameColor, ifCr2, weakType,
                            bCardBuff, aCardBuff, qCardBuff, criticalBuff, atkBuff);
                    CardItem card3 = new CardItem(servantItem, 3, cardType3, cardType1, ifsameColor, ifCr3, weakType,
                            bCardBuff, aCardBuff, qCardBuff, criticalBuff, atkBuff);
                    CardItem card4 = new CardItem(servantItem, 4, "ex", cardType1, ifsameColor, false, weakType,
                            bCardBuff, aCardBuff, qCardBuff, criticalBuff, atkBuff);
                    atkInFact(card1);
                    atkInFact(card2);
                    atkInFact(card3);
                    atkInFact(card4);
                    faTvResult.setText(result);
                }
                break;
            case R.id.fa_btn_clean:
                faTvResult.setText("");
                result = "";
                break;
            case R.id.fa_rl_character:
                faRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    /***
     * ATK×攻击补正×[卡牌伤害倍率×位置加成×(1+卡牌BUFF)+首位加成]×
     * 职阶补正×职阶相性补正×阵营相性补正×乱数补正×(1+攻击力BUFF—敌方防御力BUFF)×
     * (1+特攻威力BUFF—敌方特防威力BUFF+暴击威力BUFF)×暴击补正×EX攻击奖励
     * +(固定伤害BUFF—敌方固定伤害BUFF)
     * + ATK×Buster Chain加成/
     */
    private void atkInFact(CardItem card) {
        if (card.cardType.equals("ex")) {
            busterChain = 0;
            ifEx = true;
        }
//        double attack = atk * card.atkCor * (card.atkPercent * card.positionBuff * (1 + card.cardBuff) + card.firstCard) *
//                card.classCor * weakCor * teamCor * random * (1 + card.atkBuff - enemyDefence) *
//                (1 + card.specialBuff - specialDefence + card.criticalBuff) * card.criticalCor * card.exPresent
//                + (card.solidAtkBuff - solidDefence) + atk * busterChain;
        double attack = atk * card.atkCor * (card.atkTimes * card.positionBuff * (1 + card.cardBuff) + card.firstCardBuff) *
                card.classCor * card.weakCor * teamCor * randomCor * (1 + card.atkBuff + enemyDefence) *
                (1 + specialBuff - specialDefence + card.criticalBuff) * card.criticalCor * card.exReward
                + (card.solidAtkBuff - solidDefence) + atk * busterChain;
        int attackInt = (int) Math.floor(attack);
        if (result.length() < 1) {
            result = card.cardType + "卡在" + card.cardPosition + "号位的伤害为" + attackInt;
            overallAttack = attackInt;
        } else {
            result = result + "\n" + card.cardType + "卡在" + card.cardPosition + "号位的伤害为" + attackInt;
            overallAttack += attackInt;
            if (ifEx) {
                result = result + "\n" + "合计----->" + overallAttack;
                overallAttack = 0;
                ifEx = false;
            }
        }
    }

    //乱数补正
    private double getRan() {
        return Math.random() * 0.2 + 0.9;
    }

    public List<Map<String, Object>> getListData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.mipmap.buster);
        map.put("name", "Buster");
        list.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.mipmap.arts);
        map2.put("name", "Arts");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.mipmap.quick);
        map3.put("name", "Quick");
        list.add(map3);

        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
