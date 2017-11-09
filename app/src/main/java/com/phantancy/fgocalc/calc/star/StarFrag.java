package com.phantancy.fgocalc.calc.star;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.phantancy.fgocalc.item.ConditionStar;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.ToolCase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by HATTER on 2017/11/7.
 */

public class StarFrag extends BaseFrag implements
        StarContract.View,
        View.OnClickListener {
    @BindView(R.id.fsm_btn_buff)
    Button fsmBtnBuff;
    @BindView(R.id.fsm_btn_clean)
    Button fsmBtnClean;
    @BindView(R.id.fsm_btn_calc)
    Button fsmBtnCalc;
    @BindView(R.id.fsm_sp_card1)
    Spinner fsmSpCard1;
    @BindView(R.id.fsm_sp_card2)
    Spinner fsmSpCard2;
    @BindView(R.id.fsm_sp_card3)
    Spinner fsmSpCard3;
    @BindView(R.id.fsm_cb_ok1)
    CheckBox fsmCbOk1;
    @BindView(R.id.fsm_cb_ok2)
    CheckBox fsmCbOk2;
    @BindView(R.id.fsm_cb_ok3)
    CheckBox fsmCbOk3;
    @BindView(R.id.fsm_cb_cr1)
    CheckBox fsmCbCr1;
    @BindView(R.id.fsm_cb_cr2)
    CheckBox fsmCbCr2;
    @BindView(R.id.fsm_cb_cr3)
    CheckBox fsmCbCr3;
    @BindView(R.id.fsm_rb_low)
    RadioButton fsmRbLow;
    @BindView(R.id.fsm_rb_high)
    RadioButton fsmRbHigh;
    @BindView(R.id.fsm_rb_middle)
    RadioButton fsmRbMiddle;
    @BindView(R.id.fsm_rb_ran)
    RadioButton fsmRbRan;
    @BindView(R.id.fsm_rg_ran)
    RadioGroup fsmRgRan;
    @BindView(R.id.fsm_ll_input)
    LinearLayout fsmLlInput;
    @BindView(R.id.fsm_tv_result)
    TextView fsmTvResult;
    @BindView(R.id.fsm_iv_character)
    ImageView fsmIvCharacter;
    @BindView(R.id.fsm_v_character)
    View fsmVCharacter;
    @BindView(R.id.fsm_tv_character)
    TextView fsmTvCharacter;
    @BindView(R.id.fsm_rl_character)
    RelativeLayout fsmRlCharacter;
    Unbinder unbinder;

    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private String[] cardValues = {"b", "a", "q"};
    private String cardType1, cardType2, cardType3;
    private boolean ifok1 = false, ifok2 = false, ifok3 = false,
            ifCr1, ifCr2, ifCr3;
    private double random = 0.05;//平均敌补正
    private ConditionStar conS;
    private StarContract.Presenter mPresenter;

    public StarFrag() {

    }

    public static StarFrag newInstance() {
        return new StarFrag();
    }

    @Override
    public void setPresenter(StarContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_star_mvp, container, false);
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
        //声明一个简单simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(ctx, ToolCase.getCommandCards(), R.layout.item_card_type,
                new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
        fsmSpCard1.setAdapter(simpleAdapter);
        fsmSpCard2.setAdapter(simpleAdapter);
        fsmSpCard3.setAdapter(simpleAdapter);
        fsmTvResult.setMovementMethod(new ScrollingMovementMethod());
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void setListener() {
        fsmBtnBuff.setOnClickListener(this);
        fsmBtnCalc.setOnClickListener(this);
        fsmBtnClean.setOnClickListener(this);
        fsmRlCharacter.setOnClickListener(this);

        fsmSpCard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType1 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType1 = cardValues[0];
            }
        });
        fsmSpCard2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType2 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType2 = cardValues[0];
            }
        });
        fsmSpCard3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType3 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType3 = cardValues[0];
            }
        });

        fsmCbOk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok1 = true;
                } else {
                    ifok1 = false;
                }
            }
        });

        fsmCbOk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok2 = true;
                } else {
                    ifok2 = false;
                }
            }
        });

        fsmCbOk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok3 = true;
                } else {
                    ifok3 = false;
                }
            }
        });

        fsmCbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr1 = true;
                } else {
                    ifCr1 = false;
                }
            }
        });

        fsmCbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr2 = true;
                } else {
                    ifCr2 = false;
                }
            }
        });

        fsmCbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr3 = true;
                } else {
                    ifCr3 = false;
                }
            }
        });

        fsmRgRan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fsm_rb_low:
                        random = mPresenter.getRan(Constant.TYPE_MIN);
                        break;
                    case R.id.fsm_rb_high:
                        random = mPresenter.getRan(Constant.TYPE_MAX);
                        break;
                    case R.id.fsm_rb_middle:
                        random = mPresenter.getRan(Constant.TYPE_AVERAGE);
                        break;
                    case R.id.fsm_rb_ran:
                        random = mPresenter.getRan(Constant.TYPE_RANDOM);
                        break;
                }
            }
        });

        fsmTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToolCase.copy2Clipboard(ctx,fsmTvResult);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fsm_btn_buff:
                buffsItem = ((CalcActy) mActy).getBuffsItem();
                Intent i = new Intent(ctx, BuffActy.class);
                i.putExtra("servantItem", servantItem);
                i.putExtra("buffsItem", buffsItem);
                startActivityForResult(i, Constant.SET_BUFF);
                mActy.overridePendingTransition(R.anim.push_half_in, 0);
                break;
            case R.id.fsm_btn_calc:
                if (validateData()) {
                    mPresenter.getReady(conS);
                }
                break;
            case R.id.fsm_btn_clean:
                mPresenter.clear();
                fsmTvResult.setText(getResources().getString(R.string.about_calc_result));
                break;
            case R.id.fsm_rl_character:
                fsmRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    private boolean validateData(){
        buffsItem = ((CalcActy) mActy).getBuffsItem();
        conS = mPresenter.getCondition(cardType1,cardType2,cardType3,
                ifCr1,ifCr2,ifCr3,ifok1,ifok2,ifok3,
                random,servantItem,buffsItem);

        return  true;
    }

    @Override
    public void setResult(String result) {
        ToolCase.setViewValue(fsmTvResult, result);
        int offset = fsmTvResult.getLineCount() * fsmTvResult.getLineHeight();
        if(offset > fsmTvResult.getHeight()){
            fsmTvResult.scrollTo(0,offset - fsmTvResult.getHeight());
        }
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
