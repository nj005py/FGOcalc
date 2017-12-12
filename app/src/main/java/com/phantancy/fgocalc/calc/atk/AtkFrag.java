package com.phantancy.fgocalc.calc.atk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
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

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.base.BaseFrag;
import com.phantancy.fgocalc.calc.CalcActy;
import com.phantancy.fgocalc.calc.buff.BuffActy;
import com.phantancy.fgocalc.common.Constant;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.ConditionAtk;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.BaseUtils;
import com.phantancy.fgocalc.util.SharedPreferencesUtils;
import com.phantancy.fgocalc.util.ToolCase;

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
    private AtkContract.Presenter mPresenter;
    private int atk = 0;
    private String[] cardValues = {"b", "a", "q"};
    //环境类型的参数例如选卡、是否暴击、职阶相性、阵营相性、乱数补正
    private String cardType1, cardType2, cardType3;//1,2,3号位选卡
    private boolean ifEx = false;//是否ex卡（4号位卡）
    private boolean ifCr1, ifCr2, ifCr3;//1-3号位是否暴击
    private int weakType = 1;//职阶相性类型
    private double teamCor = 1.0, //阵营相性
            randomCor = 1.0;//平均乱数补正
    private int essenceAtk = 0;//礼装atk
    private int[] essenceAtks;
    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private ConditionAtk conAtk;


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
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) famLlCalc.getLayoutParams();
//        lp.bottomMargin = height;
//        famLlCalc.setLayoutParams(lp);
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servantItem");
//        buffsItem = (BuffsItem) data.getSerializable("buffsItem");
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        //声明一个简单simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(ctx, ToolCase.getCommandCards(), R.layout.item_card_type,
                new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
        essenceAtks = getResources().getIntArray(R.array.essence_atk);
        famSpCard1.setAdapter(simpleAdapter);
        famSpCard2.setAdapter(simpleAdapter);
        famSpCard3.setAdapter(simpleAdapter);
        famTvResult.setMovementMethod(new ScrollingMovementMethod());
        ToolCase.spInitDeep(ctx,essenceAtks,famSpEssence);
        setListener();
        if (servantItem != null) {
            setDefault();
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
                ToolCase.setViewValue(famEtAtk,String.valueOf(atk));
                essenceAtk = essenceAtks[position];
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
                    mPresenter.getReady(conAtk);
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

    @Override
    public void setResult(String result) {
        ToolCase.setViewValue(famTvResult, result);
        int offset = famTvResult.getLineCount() * famTvResult.getLineHeight();
        if (offset > famTvResult.getHeight()) {
            famTvResult.scrollTo(0, offset - famTvResult.getHeight());
        }
    }

    //检查数据
    private boolean validateData() {
        if (!ToolCase.notEmpty(famEtAtk)) {
            famRlCharacter.setVisibility(View.VISIBLE);
            famTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
            SharedPreferencesUtils.setParam(ctx, "ifLily", false);
            return false;
        }
        atk = Integer.valueOf(ToolCase.getViewValue(famEtAtk));
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        conAtk = mPresenter.getCondition(atk, cardType1, cardType2, cardType3,
                ifEx, ifCr1, ifCr2, ifCr3, weakType, teamCor, randomCor, servantItem, buffsItem);
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
