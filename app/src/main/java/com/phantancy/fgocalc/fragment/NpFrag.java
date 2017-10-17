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
public class NpFrag extends BaseFrag {
    @BindView(R.id.fn_sp_card1)
    Spinner fnSpCard1;
    @BindView(R.id.fn_sp_card2)
    Spinner fnSpCard2;
    @BindView(R.id.fn_sp_card3)
    Spinner fnSpCard3;
    @BindView(R.id.fn_cb_ok1)
    CheckBox fnCbOk1;
    @BindView(R.id.fn_cb_ok2)
    CheckBox fnCbOk2;
    @BindView(R.id.fn_cb_ok3)
    CheckBox fnCbOk3;
    @BindView(R.id.fn_tv_result)
    TextView fnTvResult;
    @BindView(R.id.fn_ll_input)
    LinearLayout fnLlInput;
    @BindView(R.id.fn_btn_calc)
    Button fnBtnCalc;
    @BindView(R.id.fn_btn_clean)
    Button fnBtnClean;
    @BindView(R.id.fn_et_artsup)
    EditText fnEtArtsup;
    @BindView(R.id.fn_rb_low)
    RadioButton fnRbLow;
    @BindView(R.id.fn_rb_high)
    RadioButton fnRbHigh;
    @BindView(R.id.fn_rb_middle)
    RadioButton fnRbMiddle;
    @BindView(R.id.fn_rb_ran)
    RadioButton fnRbRan;
    @BindView(R.id.fn_rg_ran)
    RadioGroup fnRgRan;
    @BindView(R.id.fn_et_quickup)
    EditText fnEtQuickup;
    @BindView(R.id.fn_cb_cr1)
    CheckBox fnCbCr1;
    @BindView(R.id.fn_cb_cr2)
    CheckBox fnCbCr2;
    @BindView(R.id.fn_cb_cr3)
    CheckBox fnCbCr3;
    @BindView(R.id.fn_et_np_up)
    EditText fnEtNpUp;
    @BindView(R.id.fn_iv_character)
    ImageView fnIvCharacter;
    @BindView(R.id.fn_v_character)
    View fnVCharacter;
    @BindView(R.id.fn_tv_character)
    TextView fnTvCharacter;
    @BindView(R.id.fn_rl_character)
    RelativeLayout fnRlCharacter;
    @BindView(R.id.fn_tb_buff)
    ToggleButton fnTbBuff;
    @BindView(R.id.fn_ll_buff)
    LinearLayout fnLlBuff;
    @BindView(R.id.fn_ll_calc)
    LinearLayout fnLlCalc;
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private String result = "", cardType1, cardType2, cardType3;
    private String[] cardValues = {"b", "a", "q"};
    private double random = 0.8, bCardBuff = 0, aCardBuff = 0, qCardBuff = 0, npBuff = 0;
    int overAllNp;
    private boolean ifLily, ifEx = false, ifok1 = false, ifok2 = false, ifok3 = false, ifCr1, ifCr2, ifCr3;
    private ServantItem servantItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_np, container, false);
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
        Bundle data = getArguments();
        servantItem = (ServantItem) data.getSerializable("servants");
        //声明一个简单simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, getListData(), R.layout.item_card_type,
                new String[]{"img", "name"}, new int[]{R.id.ict_iv_card, R.id.ict_tv_card});
        fnSpCard1.setAdapter(simpleAdapter);
        fnSpCard2.setAdapter(simpleAdapter);
        fnSpCard3.setAdapter(simpleAdapter);
        setListener();
        boolean ifLongniang = (Boolean) SharedPreferencesUtils.getParam(mContext, "ifLongniang", true);
        if (ifLongniang) {
            fnRlCharacter.setVisibility(View.VISIBLE);
            fnTvCharacter.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
            SharedPreferencesUtils.setParam(mContext, "ifLongniang", false);
        }
    }

    private void setListener() {
        int height = BaseUtils.getNavigationHeight(getContext());
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)fnLlCalc.getLayoutParams();
        lp.bottomMargin = height;
        fnLlCalc.setLayoutParams(lp);
        fnBtnCalc.setOnClickListener(this);
        fnBtnClean.setOnClickListener(this);
        fnRlCharacter.setOnClickListener(this);
        //宝具设置开关
        fnTbBuff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fnLlBuff.setVisibility(View.VISIBLE);
                } else {
                    fnLlBuff.setVisibility(View.GONE);
                }
            }
        });
        fnSpCard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType1 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType1 = cardValues[0];
            }
        });
        fnSpCard2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType2 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType2 = cardValues[0];
            }
        });
        fnSpCard3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardType3 = cardValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardType3 = cardValues[0];
            }
        });

        fnCbOk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok1 = true;
                } else {
                    ifok1 = false;
                }
            }
        });

        fnCbOk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok2 = true;
                } else {
                    ifok2 = false;
                }
            }
        });

        fnCbOk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifok3 = true;
                } else {
                    ifok3 = false;
                }
            }
        });

        fnCbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr1 = true;
                } else {
                    ifCr1 = false;
                }
            }
        });

        fnCbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr2 = true;
                } else {
                    ifCr2 = false;
                }
            }
        });

        fnCbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifCr3 = true;
                } else {
                    ifCr3 = false;
                }
            }
        });

        fnRgRan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fn_rb_low:
                        random = 0.8;
                        break;
                    case R.id.fn_rb_high:
                        random = 1.23;
                        break;
                    case R.id.fn_rb_middle:
                        random = 1.015;
                        break;
                    case R.id.fn_rb_ran:
                        random = getRan();
                        break;
                }
            }
        });

        fnTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("fgocalc_np", fnTvResult.getText());
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
            case R.id.fn_btn_calc:
                //判断蓝魔放
                if (notEmpty(etValue(fnEtArtsup))) {
                    aCardBuff = Double.valueOf(etValue(fnEtArtsup));
                    aCardBuff = aCardBuff / 100;
                } else {
                    aCardBuff = 0;
                }
                //判断绿魔放
                if (notEmpty(etValue(fnEtQuickup))) {
                    qCardBuff = Double.valueOf(etValue(fnEtQuickup));
                    qCardBuff = qCardBuff / 100;
                } else {
                    qCardBuff = 0;
                }
                //判断np获得量
                if (notEmpty(etValue(fnEtNpUp))) {
                    npBuff = Double.valueOf(etValue(fnEtNpUp));
                    npBuff = npBuff / 100;
                } else {
                    npBuff = 0;
                }
                Log.d(TAG, "首卡:" + cardType1 + " 敌补正:" + random);
//                    CardNP card1 = new CardNP(cardType1, ana, bna, qna, ena, ahit, bhit, qhit, ehit,
//                            1, aCardBuff,qCardBuff, cardType1, npBuff, false, ifok1);
//                    CardNP card2 = new CardNP(cardType2, ana, bna, qna, ena, ahit, bhit, qhit, ehit,
//                            2, aCardBuff,qCardBuff, cardType1, npBuff, false, ifok2);
//                    CardNP card3 = new CardNP(cardType3, ana, bna, qna, ena, ahit, bhit, qhit, ehit,
//                            3, aCardBuff,qCardBuff, cardType1, npBuff, false, ifok3);
//                    CardNP card4 = new CardNP("ex", ana, bna, qna, ena, ahit, bhit, qhit, ehit,
//                            4, aCardBuff,qCardBuff, cardType1, npBuff, false, ifok3);
                //从者，位置，卡色，首卡，暴击，蓝魔放，绿魔放
                CardItem card1 = new CardItem(servantItem, 1, cardType1, cardType1, ifCr1, aCardBuff, qCardBuff, npBuff, ifok1);
                CardItem card2 = new CardItem(servantItem, 2, cardType2, cardType1, ifCr2, aCardBuff, qCardBuff, npBuff, ifok2);
                CardItem card3 = new CardItem(servantItem, 3, cardType3, cardType1, ifCr3, aCardBuff, qCardBuff, npBuff, ifok3);
                CardItem card4 = new CardItem(servantItem, 4, "ex", cardType1, false, aCardBuff, qCardBuff, npBuff, ifok3);
                npInFact(card1);
                npInFact(card2);
                npInFact(card3);
                npInFact(card4);
                fnTvResult.setText(result);
                break;
            case R.id.fn_btn_clean:
                result = "";
                fnTvResult.setText("");
                break;
            case R.id.fn_rl_character:
                fnRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 每Hit主动NP获得量×攻击Hit数×［卡牌NP倍率×位置加成×（1+卡牌Buff）＋首位加成］×
     * （1+NP Buff）×暴击补正×Overkill补正×敌补正
     */
    private void npInFact(CardItem card) {
        if (card.cardType.equals("ex")) {
            ifEx = true;
        }
//        double np = card.na * card.hits * (card.times * card.positionBuff * (1 + card.cardBuff) + card.firstCard) *
//                (1 + card.npBuff) * card.criticalCor * card.overkill * random;
        double np = card.na * 100 * card.hits * (card.npTimes * card.npPositionBuff * (1 + card.cardBuff) + card.npFirstCardBuff) *
                (1 + card.npBuff) * card.criticalCor * card.overkill * random;
        int npInt = (int) Math.rint(np);
        if (cardType1.equals("a") && npInt == 0) {
            npInt = 1;
        }
        if (result.length() < 1) {
            result = card.cardType + "卡在" + card.cardPosition + " 号位的np获取量为" + npInt;
            overAllNp = npInt;
        } else {
            result = result + "\n" + card.cardType + "卡在" + card.cardPosition + " 号位的np获取量为" + npInt;
            overAllNp += npInt;
            if (ifEx) {
                result = result + "\n" + "合计----->" + overAllNp;
                overAllNp = 0;
                ifEx = false;
            }
        }
    }

    //敌补正
    private double getRan() {
        return Math.random() * 0.52 + 0.8;
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
