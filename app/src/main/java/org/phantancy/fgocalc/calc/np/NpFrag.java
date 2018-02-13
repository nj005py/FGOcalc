package org.phantancy.fgocalc.calc.np;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import org.phantancy.fgocalc.item.ConditionNp;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToolCase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by HATTER on 2017/11/7.
 */

public class NpFrag extends BaseFrag implements
        NpContract.View,
        View.OnClickListener {
    @BindView(R.id.fnm_btn_buff)
    Button fnmBtnBuff;
    @BindView(R.id.fnm_btn_clean)
    Button fnmBtnClean;
    @BindView(R.id.fnm_btn_calc)
    Button fnmBtnCalc;
    @BindView(R.id.fnm_sp_card1)
    Spinner fnmSpCard1;
    @BindView(R.id.fnm_sp_card2)
    Spinner fnmSpCard2;
    @BindView(R.id.fnm_sp_card3)
    Spinner fnmSpCard3;
    @BindView(R.id.fnm_cb_ok1)
    CheckBox fnmCbOk1;
    @BindView(R.id.fnm_cb_ok2)
    CheckBox fnmCbOk2;
    @BindView(R.id.fnm_cb_ok3)
    CheckBox fnmCbOk3;
    @BindView(R.id.fnm_cb_cr1)
    CheckBox fnmCbCr1;
    @BindView(R.id.fnm_cb_cr2)
    CheckBox fnmCbCr2;
    @BindView(R.id.fnm_cb_cr3)
    CheckBox fnmCbCr3;
    @BindView(R.id.fnm_rb_low)
    RadioButton fnmRbLow;
    @BindView(R.id.fnm_rb_high)
    RadioButton fnmRbHigh;
    @BindView(R.id.fnm_rb_middle)
    RadioButton fnmRbMiddle;
    @BindView(R.id.fnm_rb_ran)
    RadioButton fnmRbRan;
    @BindView(R.id.fnm_rg_ran)
    RadioGroup fnmRgRan;
    @BindView(R.id.fnm_ll_input)
    LinearLayout fnmLlInput;
    @BindView(R.id.fnm_tv_result)
    TextView fnmTvResult;
    @BindView(R.id.fnm_iv_character)
    ImageView fnmIvCharacter;
    @BindView(R.id.fnm_v_character)
    View fnmVCharacter;
    @BindView(R.id.fnm_tv_character)
    TextView fnmTvCharacter;
    @BindView(R.id.fnm_rl_character)
    RelativeLayout fnmRlCharacter;
    Unbinder unbinder;
    @BindView(R.id.fnm_sv_result)
    ScrollView fnmSvResult;
    @BindView(R.id.fnm_rb_enemy_one)
    RadioButton fnmRbEnemyOne;
    @BindView(R.id.fnm_rb_enemy_two)
    RadioButton fnmRbEnemyTwo;
    @BindView(R.id.fnm_rb_enemy_three)
    RadioButton fnmRbEnemyThree;
    @BindView(R.id.fnm_rg_enemy_amount)
    RadioGroup fnmRgEnemyAmount;
    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private String[] cardValues = {"b", "a", "q", "np"};
    private String cardType1, cardType2, cardType3;
    private boolean ifok1 = false, ifok2 = false, ifok3 = false,
            ifCr1, ifCr2, ifCr3;
    private double random = 1.015;//平均敌补正
    private int enemyAmount = 1;
    private ConditionNp conNp;
    private String trumpColor;

    @NonNull
    private NpContract.Presenter mPresenter;

    public NpFrag() {
    }

    public static NpFrag newInstance() {
        return new NpFrag();
    }

    @Override
    public void setPresenter(NpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_np_mvp, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servantItem");
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        trumpColor = servantItem.getTrump_color();
        //声明一个简单simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(ctx, ToolCase.getCommandNPCards(trumpColor), R.layout.item_card_type,
                new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
        fnmSpCard1.setAdapter(simpleAdapter);
        fnmSpCard2.setAdapter(simpleAdapter);
        fnmSpCard3.setAdapter(simpleAdapter);
        fnmTvResult.setMovementMethod(new ScrollingMovementMethod());
        setListener();
        boolean firstTimeUse = (Boolean) SharedPreferencesUtils.getParam(ctx, "firstTimeUse", true);
        if (firstTimeUse) {
            fnmRlCharacter.setVisibility(View.VISIBLE);
            fnmTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
            SharedPreferencesUtils.setParam(ctx, "firstTimeUse", false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    private void setListener() {
        fnmBtnBuff.setOnClickListener(this);
        fnmBtnCalc.setOnClickListener(this);
        fnmBtnClean.setOnClickListener(this);
        fnmRlCharacter.setOnClickListener(this);

        fnmSpCard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType1 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType1 = cardValues[0];
            }
        });
        fnmSpCard2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType2 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType2 = cardValues[0];
            }
        });
        fnmSpCard3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType3 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType3 = cardValues[0];
            }
        });

        fnmCbOk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok1 = true;
                } else {
                    ifok1 = false;
                }
            }
        });

        fnmCbOk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok2 = true;
                } else {
                    ifok2 = false;
                }
            }
        });

        fnmCbOk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok3 = true;
                } else {
                    ifok3 = false;
                }
            }
        });

        fnmCbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr1 = true;
                } else {
                    ifCr1 = false;
                }
            }
        });

        fnmCbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr2 = true;
                } else {
                    ifCr2 = false;
                }
            }
        });

        fnmCbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr3 = true;
                } else {
                    ifCr3 = false;
                }
            }
        });

        fnmRgRan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fnm_rb_low:
                        random = mPresenter.getRan(Constant.TYPE_MIN);
                        break;
                    case R.id.fnm_rb_high:
                        random = mPresenter.getRan(Constant.TYPE_MAX);
                        break;
                    case R.id.fnm_rb_middle:
                        random = mPresenter.getRan(Constant.TYPE_AVERAGE);
                        break;
                    case R.id.fnm_rb_ran:
                        random = mPresenter.getRan(Constant.TYPE_RANDOM);
                        break;
                }
            }
        });

        fnmTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolCase.copy2Clipboard(ctx, fnmTvResult);
                return false;
            }
        });

        fnmRgEnemyAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.fnm_rb_enemy_one:
                        enemyAmount = 1;
                        break;
                    case R.id.fnm_rb_enemy_two:
                        enemyAmount = 2;
                        break;
                    case R.id.fnm_rb_enemy_three:
                        enemyAmount = 3;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fnm_btn_buff:
                buffsItem = ((CalcActy) mActy).getBuffsItem();
                Intent i = new Intent(ctx, BuffActy.class);
                i.putExtra("servantItem", servantItem);
                i.putExtra("buffsItem", buffsItem);
                startActivityForResult(i, Constant.SET_BUFF);
                mActy.overridePendingTransition(R.anim.push_half_in, 0);
                break;
            case R.id.fnm_btn_calc:
                if (validateData()) {
                    mPresenter.getReady(conNp);
                }
                break;
            case R.id.fnm_btn_clean:
                mPresenter.clean();
                fnmTvResult.setText(getResources().getString(R.string.about_calc_result));
                break;
            case R.id.fnm_rl_character:
                fnmRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setResult(String result) {
        ToolCase.setViewValue(fnmTvResult, result);
//        int offset = fnmTvResult.getLineCount() * fnmTvResult.getLineHeight();
//        if (offset > fnmTvResult.getHeight()) {
//            fnmTvResult.scrollTo(0, offset - fnmTvResult.getHeight());
//        }
        fnmSvResult.post(new Runnable() {
            @Override
            public void run() {
                fnmSvResult.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private boolean validateData() {
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        conNp = mPresenter.getCondition(cardType1, cardType2, cardType3,
                ifCr1, ifCr2, ifCr3, ifok1, ifok2, ifok3,
                random, servantItem, buffsItem,enemyAmount);

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
