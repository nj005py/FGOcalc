package org.phantancy.fgocalc.calc.atk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.base.BaseFrag;
import org.phantancy.fgocalc.calc.CalcActy;
import org.phantancy.fgocalc.calc.buff.BuffActy;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionAtk;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.BaseUtils;
import org.phantancy.fgocalc.util.ToolCase;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by HATTER on 2017/11/6.
 */

public class AtkFrag extends BaseFrag implements
        View.OnClickListener,
        AtkContract.View {

    @BindView(R.id.fam_btn_buff)
    Button famBtnBuff;
    @BindView(R.id.fam_btn_clean)
    Button famBtnClean;
    @BindView(R.id.fam_btn_calc)
    Button famBtnCalc;
    @BindView(R.id.fam_et_atk)
    EditText famEtAtk;
    @BindView(R.id.fam_sp_card1)
    Spinner famSpCard1;
    @BindView(R.id.fam_sp_card2)
    Spinner famSpCard2;
    @BindView(R.id.fam_sp_card3)
    Spinner famSpCard3;
    @BindView(R.id.fam_cb_cr1)
    CheckBox famCbCr1;
    @BindView(R.id.fam_cb_cr2)
    CheckBox famCbCr2;
    @BindView(R.id.fam_cb_cr3)
    CheckBox famCbCr3;
    @BindView(R.id.fam_rb_normal)
    RadioButton famRbNormal;
    @BindView(R.id.fam_rb_weak)
    RadioButton famRbWeak;
    @BindView(R.id.fam_rb_weakened)
    RadioButton famRbWeakened;
    @BindView(R.id.fam_rg_weak)
    RadioGroup famRgWeak;
    @BindView(R.id.fam_rb_tnormal)
    RadioButton famRbTnormal;
    @BindView(R.id.fam_rb_tweak)
    RadioButton famRbTweak;
    @BindView(R.id.fam_rb_tweakened)
    RadioButton famRbTweakened;
    @BindView(R.id.fam_rg_team)
    RadioGroup famRgTeam;
    @BindView(R.id.fam_rb_random_min)
    RadioButton famRbRandomMin;
    @BindView(R.id.fam_rb_random_max)
    RadioButton famRbRandomMax;
    @BindView(R.id.fam_rb_random_average)
    RadioButton famRbRandomAverage;
    @BindView(R.id.fam_rb_random)
    RadioButton famRbRandom;
    @BindView(R.id.fam_rg_random)
    RadioGroup famRgRandom;
    @BindView(R.id.fam_tv_result)
    TextView famTvResult;
    @BindView(R.id.fam_ll_input)
    LinearLayout famLlInput;
    @BindView(R.id.fam_iv_character)
    ImageView famIvCharacter;
    @BindView(R.id.fam_v_character)
    View famVCharacter;
    @BindView(R.id.fam_tv_character)
    TextView famTvCharacter;
    @BindView(R.id.fam_rl_character)
    RelativeLayout famRlCharacter;
    Unbinder unbinder;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.fam_rb_weak_b)
    RadioButton famRbWeakB;
    Unbinder unbinder1;
    @BindView(R.id.fam_sp_essence)
    Spinner famSpEssence;
    @BindView(R.id.fam_et_hp_total)
    EditText famEtHpTotal;
    @BindView(R.id.fam_et_hp_left)
    EditText famEtHpLeft;
    @BindView(R.id.fam_ll_hp)
    LinearLayout famLlHp;
    @BindView(R.id.fam_iv_color)
    ImageView famIvColor;
    @BindView(R.id.fam_cb_upgraded)
    CheckBox famCbUpgraded;
    @BindView(R.id.fam_sp_lv)
    Spinner famSpLv;
    @BindView(R.id.fam_sv_result)
    ScrollView famSvResult;
    private AtkContract.Presenter mPresenter;
    private int atk = 0;
    private String[] cardValues = {"b", "a", "q", "np"};
    //环境类型的参数例如选卡、是否暴击、职阶相性、阵营相性、乱数补正
    private String cardType1, cardType2, cardType3;//1,2,3号位选卡
    private boolean ifEx = false;//是否ex卡（4号位卡）
    private boolean ifCr1, ifCr2, ifCr3;//1-3号位是否暴击
    private int weakType = 1;//职阶相性类型
    private double teamCor = 1.0, //阵营相性
            randomCor = 1.0;//平均乱数补正
    private double trumpTimes;
    private int essenceAtk = 0;//礼装atk
    private int[] essenceAtks;
    private int[] lv;
    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private ConditionAtk conAtk;
    private ConditionTrump conT;
    private int id;
    private int hpTotal = 0, hpLeft = 0;
    private String[] lvStr;
    private boolean isUpgraded,//是否有宝具本
            isPreTimes;//是否是旧倍率
    private String trumpColor;
    private int curPos = 0;
    private List<Double> curLv = new ArrayList<>();
    private List<Double> preLv = new ArrayList<>();


    //require empty public constructor
    public AtkFrag() {
    }

    public static AtkFrag newInstance() {
        return new AtkFrag();
    }

    @Override
    public void setPresenter(AtkContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_atk_mvp, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int height = BaseUtils.getNavigationHeight(getContext());
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servantItem");
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        if (servantItem != null) {
            setDefault();
            trumpColor = servantItem.getTrump_color();
            //声明一个简单simpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(ctx, ToolCase.getCommandNPCards(trumpColor), R.layout.item_card_type,
                    new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
            essenceAtks = getResources().getIntArray(R.array.essence_atk);
            famSpCard1.setAdapter(simpleAdapter);
            famSpCard2.setAdapter(simpleAdapter);
            famSpCard3.setAdapter(simpleAdapter);
            famTvResult.setMovementMethod(new ScrollingMovementMethod());
            ToolCase.spInitDeep(ctx, essenceAtks, famSpEssence);
            id = servantItem.getId();
            isUpgraded = servantItem.getTrump_upgraded() == 1 ? true : false;
            lv = getResources().getIntArray(R.array.trump_lv);
            lvStr = getResources().getStringArray(R.array.trump_lv_str);
            if (isUpgraded) {
                famCbUpgraded.setVisibility(View.VISIBLE);
            } else {
                famCbUpgraded.setVisibility(View.GONE);
            }
            showTrumpColor();
            ToolCase.spInitDeep(ctx, lvStr, famSpLv);
            //id66，131为双子需要显血
            if (id == 66 || id == 131) {
                famLlHp.setVisibility(View.VISIBLE);
                //都需要血量信息
                famLlHp.setVisibility(View.VISIBLE);
                //66r双子，附加倍率<超蓄力威力提升> (此倍率×自身已损失HP所占百分比,与宝具倍率加算)
                //131弓双子，HP降低，宝具威力提高【※总倍率＝攻击倍率+HP特攻倍率*（1—现在HP/最大HP)】
            } else {
                famLlHp.setVisibility(View.GONE);
            }
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
        super.onDestroy();
        unbinder.unbind();
    }

    //默认hp atk
    private void setDefault() {
        ToolCase.setViewValue(famEtAtk, new StringBuilder().append(servantItem.getDefault_atk()).toString());
        ToolCase.setViewValue(famEtHpTotal, new StringBuilder().append(servantItem.getDefault_hp()).toString());
        ToolCase.setViewValue(famEtHpLeft, new StringBuilder().append(servantItem.getDefault_hp()).toString());
        //判断是否是alterego
        String classType = servantItem.getClass_type();
        if (classType.toLowerCase().equals("alterego")) {
            famRbWeakB.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        famBtnCalc.setOnClickListener(this);
        famBtnClean.setOnClickListener(this);
        famRlCharacter.setOnClickListener(this);
        //buff设置开关
        famBtnBuff.setOnClickListener(this);
        //1-3号卡选卡
        famSpCard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cardType1 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType1 = cardValues[0];
            }

        });

        famSpCard2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cardType2 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType2 = cardValues[0];
            }

        });

        famSpCard3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cardType3 = cardValues[position];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                cardType3 = cardValues[0];
            }

        });
        //1-3号卡是否暴击
        famCbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr1 = true;
                } else {
                    ifCr1 = false;
                }
            }
        });

        famCbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr2 = true;
                } else {
                    ifCr2 = false;
                }
            }
        });

        famCbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr3 = true;
                } else {
                    ifCr3 = false;
                }
            }
        });
        //职阶相性
        famRgWeak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fam_rb_normal:
                        weakType = 1;
                        break;
                    case R.id.fam_rb_weak:
                        weakType = 2;
                        break;
                    case R.id.fam_rb_weakened:
                        weakType = 3;
                        break;
                    case R.id.fam_rb_weak_b:
                        weakType = 4;
                        break;
                }
            }
        });
        //阵营相性
        famRgTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fam_rb_tnormal:
                        teamCor = 1.0;
                        break;
                    case R.id.fam_rb_tweak:
                        teamCor = 1.1;
                        break;
                    case R.id.fam_rb_tweakened:
                        teamCor = 0.9;
                        break;
                }
            }
        });
        //乱数补正
        famRgRandom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fam_rb_random_min:
                        randomCor = mPresenter.getRan(Constant.TYPE_MIN);
                        break;
                    case R.id.fam_rb_random_max:
                        randomCor = mPresenter.getRan(Constant.TYPE_MAX);
                        break;
                    case R.id.fam_rb_random_average:
                        randomCor = mPresenter.getRan(Constant.TYPE_AVERAGE);
                        break;
                    case R.id.fam_rb_random:
                        randomCor = mPresenter.getRan(Constant.TYPE_RANDOM);
                        break;
                }
            }
        });

        famTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolCase.copy2Clipboard(ctx, famTvResult);
                return false;
            }
        });

        famSpEssence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atk = Integer.valueOf(ToolCase.getViewValue(famEtAtk));
                atk = atk - essenceAtk + essenceAtks[position];
                ToolCase.setViewValue(famEtAtk, String.valueOf(atk));
                essenceAtk = essenceAtks[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        famCbUpgraded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        famSpLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (servantItem != null) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fam_btn_calc:
                if (validateData()) {
                    mPresenter.getReady(conAtk, conT);
                }
                break;
            case R.id.fam_btn_clean:
                mPresenter.clean();
                famTvResult.setText(getResources().getString(R.string.about_calc_result));
                break;
            case R.id.fam_btn_buff:
                buffsItem = ((CalcActy) mActy).getBuffsItem();
                Intent i = new Intent(ctx, BuffActy.class);
                i.putExtra("servantItem", servantItem);
                i.putExtra("buffsItem", buffsItem);
                startActivityForResult(i, Constant.SET_BUFF);
                mActy.overridePendingTransition(R.anim.push_half_in, 0);
                break;
            case R.id.fam_rl_character:
                famRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    //卡色Buff倍率
    private void showTrumpColor() {
        switch (trumpColor) {
            case "b":
                famIvColor.setImageResource(R.mipmap.buster);
                break;
            case "a":
                famIvColor.setImageResource(R.mipmap.arts);
                break;
            case "q":
                famIvColor.setImageResource(R.mipmap.quick);
                break;
        }
    }

    @Override
    public void setResult(Object result) {
        if (result instanceof String){
            ToolCase.setViewValue(famTvResult, (String) result);
//        int offset = famTvResult.getLineCount() * famTvResult.getLineHeight();
//        if (offset > famTvResult.getHeight()) {
//            famTvResult.scrollTo(0, offset - famTvResult.getHeight());
//        }
            famSvResult.post(new Runnable() {
                @Override
                public void run() {
                    famSvResult.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }else if (result instanceof Spanned) {
            famTvResult.setText((Spanned)result,TextView.BufferType.SPANNABLE);
            famSvResult.post(new Runnable() {
                @Override
                public void run() {
                    famSvResult.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    //检查数据
    private boolean validateData() {
        if (!ToolCase.notEmpty(famEtAtk)) {
            setCharacter("ATK是必填项！Buff可以不填！\n能帮上点忙吗？");
            return false;
        }
        atk = Integer.valueOf(ToolCase.getViewValue(famEtAtk));
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        conAtk = mPresenter.getCondition(atk, cardType1, cardType2, cardType3,
                ifEx, ifCr1, ifCr2, ifCr3, weakType, teamCor, randomCor, servantItem, buffsItem);
        //双子特殊处理倍率
        if (id == 66 || id == 131) {
            hpTotal = ToolCase.getViewInt(famEtHpTotal);
            hpLeft = ToolCase.getViewInt(famEtHpLeft);
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
                teamCor, randomCor, trumpTimes, servantItem, buffsItem);
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

    @Override
    public void setCharacter(String str) {
        famRlCharacter.setVisibility(View.VISIBLE);
        famTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
        famTvCharacter.setText(str);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
