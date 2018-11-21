package org.phantancy.fgocalc.calc.trump;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.base.BaseFrag;
import org.phantancy.fgocalc.calc.CalcActy;
import org.phantancy.fgocalc.calc.buff.BuffActy;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HATTER on 2017/11/7.
 */

public class TrumpFrag extends BaseFrag implements
        TrumpContract.View,
        View.OnClickListener {

    @BindView(R.id.ftm_btn_buff)
    Button ftmBtnBuff;
    @BindView(R.id.ftm_btn_clean)
    Button ftmBtnClean;
    @BindView(R.id.ftm_btn_calc)
    Button ftmBtnCalc;
    @BindView(R.id.ftm_iv_color)
    ImageView ftmIvColor;
    @BindView(R.id.ftm_et_atk)
    EditText ftmEtAtk;
    @BindView(R.id.ftm_et_hp_total)
    EditText ftmEtHpTotal;
    @BindView(R.id.ftm_et_hp_left)
    EditText ftmEtHpLeft;
    @BindView(R.id.ftm_ll_hp)
    LinearLayout ftmLlHp;
    @BindView(R.id.ftm_sp_lv)
    Spinner ftmSpLv;
    @BindView(R.id.ftm_rb_normal)
    RadioButton ftmRbNormal;
    @BindView(R.id.ftm_rb_weak)
    RadioButton ftmRbWeak;
    @BindView(R.id.ftm_rb_weakened)
    RadioButton ftmRbWeakened;
    @BindView(R.id.ftm_rg_weak)
    RadioGroup ftmRgWeak;
    @BindView(R.id.ftm_rb_tnormal)
    RadioButton ftmRbTnormal;
    @BindView(R.id.ftm_rb_tweak)
    RadioButton ftmRbTweak;
    @BindView(R.id.ftm_rb_tweakened)
    RadioButton ftmRbTweakened;
    @BindView(R.id.ftm_rg_team)
    RadioGroup ftmRgTeam;
    @BindView(R.id.ftm_rb_random_min)
    RadioButton ftmRbRandomMin;
    @BindView(R.id.ftm_rb_random_max)
    RadioButton ftmRbRandomMax;
    @BindView(R.id.ftm_rb_random_average)
    RadioButton ftmRbRandomAverage;
    @BindView(R.id.ftm_rb_random)
    RadioButton ftmRbRandom;
    @BindView(R.id.ftm_rg_random)
    RadioGroup ftmRgRandom;
    @BindView(R.id.ftm_ll_input)
    LinearLayout ftmLlInput;
    @BindView(R.id.ftm_tv_result)
    TextView ftmTvResult;
    @BindView(R.id.ftm_iv_character)
    ImageView ftmIvCharacter;
    @BindView(R.id.ftm_v_character)
    View ftmVCharacter;
    @BindView(R.id.ftm_tv_character)
    TextView ftmTvCharacter;
    @BindView(R.id.ftm_rl_character)
    RelativeLayout ftmRlCharacter;
    Unbinder unbinder;
    @BindView(R.id.ftm_cb_upgraded)
    CheckBox ftmCbUpgraded;
    @BindView(R.id.ftm_rb_weak_b)
    RadioButton ftmRbWeakB;
    @BindView(R.id.ftm_sp_essence)
    Spinner ftmSpEssence;
    @BindView(R.id.ftm_sv_result)
    ScrollView ftmSvResult;
    @BindView(R.id.ftm_sp_fufu)
    Spinner ftmSpFufu;
    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private int id;
    private int atk;
    private int hpTotal = 0, hpLeft = 0;
    //环境类型的参数例如选卡、是否暴击、职阶相性、阵营相性、乱数补正
    private String trumpColor;
    private int weakType = 1;//职阶相性类型
    private double teamCor = 1.0, //阵营相性
            randomCor = 1.0,//乱数补正
            trumpTimes = 0;//宝具倍率
    private String npLv = "一宝";//宝具等级
    private int[] lv;
    private int essenceAtk = 0;//礼装atk
    private int fufuAtk = 1000;//芙芙atk
    private int[] essenceAtks;
    private int[] fufuAtks;
    private String[] lvStr;
    private boolean isUpgraded,//是否有宝具本
            isPreTimes;//是否是旧倍率
    private int curPos = 0;
    private ConditionTrump conT;
    private List<Double> curLv = new ArrayList<>();
    private List<Double> preLv = new ArrayList<>();

    @NonNull
    private TrumpContract.Presenter mPresenter;

    public TrumpFrag() {
    }

    public static TrumpFrag newInstance() {
        return new TrumpFrag();
    }

    @Override
    public void setPresenter(TrumpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_trump_mvp, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servantItem");
        ftmTvResult.setMovementMethod(new ScrollingMovementMethod());
        lv = getResources().getIntArray(R.array.trump_lv);
        lvStr = getResources().getStringArray(R.array.trump_lv_str);
        essenceAtks = getResources().getIntArray(R.array.essence_atk);
        fufuAtks = getResources().getIntArray(R.array.fufu_atk);
        if (servantItem != null) {
            setDefault();
            id = servantItem.getId();
            isUpgraded = servantItem.getTrump_upgraded() == 1 ? true : false;
            trumpColor = servantItem.getTrump_color();
            //判断是否是alterego
            String classType = servantItem.getClass_type();
            if (classType.toLowerCase().equals("alterego")) {
                ftmRbWeakB.setVisibility(View.VISIBLE);
            }
            if (isUpgraded) {
                ftmCbUpgraded.setVisibility(View.VISIBLE);
            } else {
                ftmCbUpgraded.setVisibility(View.GONE);
            }
            showTrumpColor();
            //id66，131为双子需要显血
            if (id == 66 || id == 131) {
                ftmLlHp.setVisibility(View.VISIBLE);
                //都需要血量信息
                ftmLlHp.setVisibility(View.VISIBLE);
                //66r双子，附加倍率<超蓄力威力提升> (此倍率×自身已损失HP所占百分比,与宝具倍率加算)
                //131弓双子，HP降低，宝具威力提高【※总倍率＝攻击倍率+HP特攻倍率*（1—现在HP/最大HP)】
            } else {
                ftmLlHp.setVisibility(View.GONE);
            }
//            ToolCase.spInitSimple(ctx, lv, ftmSpLv);
            ToolCase.spInitDeep(ctx, lvStr, ftmSpLv);
            ToolCase.spInitDeep(ctx, essenceAtks, ftmSpEssence);
            ToolCase.spInitDeep(ctx, fufuAtks, ftmSpFufu);
            ftmSpFufu.setSelection(2);
            curLv.add(servantItem.getTrump_lv1());
            curLv.add(servantItem.getTrump_lv2());
            curLv.add(servantItem.getTrump_lv3());
            curLv.add(servantItem.getTrump_lv4());
            curLv.add(servantItem.getTrump_lv5());
            preLv.add(servantItem.getTrump_lv1_before());
            preLv.add(servantItem.getTrump_lv2_before());
            preLv.add(servantItem.getTrump_lv3_before());
            preLv.add(servantItem.getTrump_lv4_before());
            preLv.add(servantItem.getTrump_lv5_before());
            setListener();
        }
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //默认hp atk
    private void setDefault() {
        ToolCase.setViewValue(ftmEtAtk, new StringBuilder().append(servantItem.getDefault_atk()).toString());
        ToolCase.setViewValue(ftmEtHpTotal, new StringBuilder().append(servantItem.getDefault_hp()).toString());
        ToolCase.setViewValue(ftmEtHpLeft, new StringBuilder().append(servantItem.getDefault_hp()).toString());
    }

    private void setListener() {
        ftmBtnCalc.setOnClickListener(this);
        ftmBtnClean.setOnClickListener(this);
        ftmBtnBuff.setOnClickListener(this);
        ftmRlCharacter.setOnClickListener(this);
        ftmCbUpgraded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isPreTimes = true;
                    trumpTimes = preLv.get(curPos);
                } else {
                    isPreTimes = false;
                    trumpTimes = curLv.get(curPos);
                }
            }
        });
        //宝具等级
        ftmSpLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (servantItem != null) {
                    npLv = lvStr[position];
                    switch (position) {
                        case 0:
                            curPos = 0;
                            if (isPreTimes) {
                                trumpTimes = servantItem.getTrump_lv1_before();
                            } else {
                                trumpTimes = servantItem.getTrump_lv1();
                            }
                            break;
                        case 1:
                            curPos = 1;
                            if (isPreTimes) {
                                trumpTimes = servantItem.getTrump_lv2_before();
                            } else {
                                trumpTimes = servantItem.getTrump_lv2();
                            }
                            break;
                        case 2:
                            curPos = 2;
                            if (isPreTimes) {
                                trumpTimes = servantItem.getTrump_lv3_before();
                            } else {
                                trumpTimes = servantItem.getTrump_lv3();
                            }
                            break;
                        case 3:
                            curPos = 3;
                            if (isPreTimes) {
                                trumpTimes = servantItem.getTrump_lv4_before();
                            } else {
                                trumpTimes = servantItem.getTrump_lv4();
                            }
                            break;
                        case 4:
                            curPos = 4;
                            if (isPreTimes) {
                                trumpTimes = servantItem.getTrump_lv5_before();
                            } else {
                                trumpTimes = servantItem.getTrump_lv5();
                            }
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //职介相性
        ftmRgWeak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ftm_rb_normal:
                        weakType = 1;
                        break;
                    case R.id.ftm_rb_weak:
                        weakType = 2;
                        break;
                    case R.id.ftm_rb_weakened:
                        weakType = 3;
                        break;
                    case R.id.ftm_rb_weak_b:
                        weakType = 4;
                        break;
                }
            }
        });
        //阵营相应
        ftmRgTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ftm_rb_tnormal:
                        teamCor = 1.0;
                        break;
                    case R.id.ftm_rb_tweak:
                        teamCor = 1.1;
                        break;
                    case R.id.ftm_rb_tweakened:
                        teamCor = 0.9;
                        break;
                }
            }
        });
        //乱数补正
        ftmRgRandom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ftm_rb_random_min:
                        randomCor = mPresenter.getRan(Constant.TYPE_MIN);
                        break;
                    case R.id.ftm_rb_random_max:
                        randomCor = mPresenter.getRan(Constant.TYPE_MAX);
                        break;
                    case R.id.ftm_rb_random_average:
                        randomCor = mPresenter.getRan(Constant.TYPE_AVERAGE);
                        break;
                    case R.id.ftm_rb_random:
                        randomCor = mPresenter.getRan(Constant.TYPE_RANDOM);
                        break;
                }
            }
        });
        ftmTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolCase.copy2Clipboard(ctx, ftmTvResult);
                return false;
            }
        });
        ftmSpEssence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atk = Integer.valueOf(ToolCase.getViewValue(ftmEtAtk));
                atk = atk - essenceAtk + essenceAtks[position];
                ToolCase.setViewValue(ftmEtAtk, String.valueOf(atk));
                essenceAtk = essenceAtks[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ftmSpFufu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atk = Integer.valueOf(ToolCase.getViewValue(ftmEtAtk));
                atk = atk - fufuAtk + fufuAtks[position];
                ToolCase.setViewValue(ftmEtAtk,String.valueOf(atk));
                fufuAtk = fufuAtks[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ftm_btn_calc:
                if (validateData()) {
                    mPresenter.getReady(conT);
                }
                break;
            case R.id.ftm_btn_clean:
                ftmTvResult.setText(getResources().getString(R.string.about_calc_result));
                mPresenter.clear();
                break;
            case R.id.ftm_btn_buff:
                buffsItem = ((CalcActy) mActy).getBuffsItem();
                Intent i = new Intent(ctx, BuffActy.class);
                i.putExtra("servantItem", servantItem);
                i.putExtra("buffsItem", buffsItem);
                startActivityForResult(i, Constant.SET_BUFF);
                mActy.overridePendingTransition(R.anim.push_half_in, 0);
                break;
            case R.id.ftm_rl_character:
                ftmRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setCharacter(String str) {
        ftmRlCharacter.setVisibility(View.VISIBLE);
        ftmTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
        ftmTvCharacter.setText(str);
    }

    //卡色Buff倍率
    private void showTrumpColor() {
        switch (trumpColor) {
            case "b":
                ftmIvColor.setImageResource(R.drawable.buster);
                break;
            case "a":
                ftmIvColor.setImageResource(R.drawable.arts);
                break;
            case "q":
                ftmIvColor.setImageResource(R.drawable.quick);
                break;
        }
    }

    @Override
    public void setResult(String result) {
        ToolCase.setViewValue(ftmTvResult, result);
        ftmSvResult.post(new Runnable() {
            @Override
            public void run() {
                ftmSvResult.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private boolean validateData() {
        String atkStr = ToolCase.getViewValue(ftmEtAtk);
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        if (TextUtils.isEmpty(atkStr)) {
            setCharacter("ATK是必填项！！！");
            return false;
        } else {
            atk = Integer.valueOf(atkStr);
        }
        //双子特殊处理倍率
        if (id == 66 || id == 131) {
            hpTotal = ToolCase.getViewInt(ftmEtHpTotal);
            hpLeft = ToolCase.getViewInt(ftmEtHpLeft);
            Log.d(TAG, "total:" + hpTotal + " left:" + hpLeft);
            if (hpTotal == 0 || hpLeft == 0) {
                setCharacter("hp信息不全！！！");
                return false;
            }
            //66r双子，附加倍率<超蓄力威力提升> (此倍率×自身已损失HP所占百分比,与宝具倍率加算)
            //131弓双子，HP降低，宝具威力提高【※总倍率＝攻击倍率+HP特攻倍率*（1—现在HP/最大HP)】
            //66 r双子
            if (id == 66) {
                if (buffsItem == null) {
                    setCharacter("骑阶双子的附加倍率必填！\n附加倍率随oc1200%,1600%,1800%,1900%,2000%");
                    return false;
                }
                if (buffsItem.getExtraTimes() == 0) {
                    setCharacter("骑阶双子的附加倍率必填！\n附加倍率随oc1200%,1600%,1800%,1900%,2000%");
                    return false;
                }
            }
        }
        conT = mPresenter.getConditionTrump(atk, hpTotal, hpLeft, trumpColor, weakType,
                teamCor, randomCor, trumpTimes, servantItem, buffsItem,npLv);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.SET_BUFF) {
                if (data != null) {
                    BuffsItem item = (BuffsItem) data.getSerializableExtra("buffsItem");
                    if (item != null) {
                        buffsItem = item;
                        ((CalcActy) mActy).setBuffsItem(buffsItem);
                    }
                }
            }
        }
    }
}
