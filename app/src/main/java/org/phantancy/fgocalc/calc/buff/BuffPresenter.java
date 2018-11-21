package org.phantancy.fgocalc.calc.buff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.phantancy.fgocalc.R;

import org.phantancy.fgocalc.item.BuffItem;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ServantItem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HATTER on 2017/11/6.
 */

public class BuffPresenter implements BuffContract.Presenter {

    @NonNull
    private final BuffContract.View mView;
    @NonNull
    private Context ctx;

    public BuffPresenter(@NonNull BuffContract.View mView,Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    //获取buff列表
    @Override
    public List<BuffItem> getBuffList(ServantItem sItem,BuffsItem bItem) {
        List<BuffItem> list = new ArrayList<>();
        String[] buffs = ctx.getResources().getStringArray(R.array.buffs);
        boolean isInit = false;
        if (bItem == null) {
            bItem = new BuffsItem();
            isInit = true;
        }
        for (int i = 0;i < buffs.length;i ++){
            BuffItem item = new BuffItem();
            Class cls = bItem.getClass();
            String[] buff = buffs[i].split(",");
            int resId = resId = getResIdByName(buff[0]);
            if (0 == resId) {
                resId = ctx.getResources().getIdentifier(buff[0],"mipmap",ctx.getPackageName());
            }
            String hint = buff[1];
            item.setImg(resId);
            item.setHint(hint);
            item.setBuffName(buff[3]);//buff名还是根据方法名来好，图片可能共用
            // 选择要包裹的代码块，然后按下ctrl + alt + t ，快速生成try catch等
            try {
                if (buff[2].equals("d")) {
                    item.setIfPercent(true);
                    Method method = cls.getDeclaredMethod("get" + buff[3]);
                    double value = (double)method.invoke(bItem);
                    if (buff[0].equals("special_up") && isInit) {
                        value += sItem.getSpecial_buff() * 100;
                    }
                    item.setDefaultDouble(value);
                }else{
                    item.setIfPercent(false);
                    Method method = cls.getDeclaredMethod("get" + buff[3]);
                    int value = (int)method.invoke(bItem);
                    item.setDefaultInt(value);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }

    //获取buff组实体
    @Override
    public BuffsItem getBuffsItem(List<BuffItem> list) {
        BuffsItem item = new BuffsItem();
        String[] buffs = ctx.getResources().getStringArray(R.array.buffs);
        Class cls = item.getClass();
        if (list != null) {
            for (int i = 0;i < buffs.length;i ++){
                String[] buff = buffs[i].split(",");
                try {
                    if (buff[2].equals("d")) {
                        Method method = cls.getDeclaredMethod("set" + buff[3],double.class);
                        method.invoke(item,list.get(i).getDefaultDouble());
                    }else{
                        Method method = cls.getDeclaredMethod("set" + buff[3],int.class);
                        method.invoke(item,list.get(i).getDefaultInt());
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return item;
    }

    @Override
    public List<BuffItem> cleanBuffs(List<BuffItem> list) {
        if (list != null) {
            for (int i = 0;i < list.size();i ++){
                list.get(i).setDefaultDouble(0);
                list.get(i).setDefaultInt(0);
            }
        }
        return list;
    }

    private int getResIdByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return 0;
        } else {
            try {
                Class c = R.drawable.class;
                Field field = c.getField(name);
                int resId = field.getInt(null);
                return resId;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
