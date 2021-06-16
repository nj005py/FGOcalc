package org.phantancy.fgocalc.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jeremyliao.liveeventbus.LiveEventBus;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.AboutActy;
import org.phantancy.fgocalc.activity.GroupCalcActy;
import org.phantancy.fgocalc.adapter.BuffInputAdapter;
import org.phantancy.fgocalc.adapter.SettingAdapter;
import org.phantancy.fgocalc.character_factory.NetCharacter;
import org.phantancy.fgocalc.databinding.FragBuffBinding;
import org.phantancy.fgocalc.databinding.FragSettingBinding;
import org.phantancy.fgocalc.dialog.CharacterDialog;
import org.phantancy.fgocalc.entity.CharacterEntity;
import org.phantancy.fgocalc.entity.SettingEntity;
import org.phantancy.fgocalc.event.DatabaseEvent;
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SettingFragment extends BaseFragment {
    private MainViewModel vm;
    private FragSettingBinding binding;
    final static int FLUTTER_VIEW = 0X0;
    final static int RELOAD_DATABASE = 0X1;
    final static int JOIN_GROUP = 0X2;
    final static int FOLLOW = 0X3;
    final static int ABOUT = 0X4;
    //组队计算
    final static int GROUP_CALC = 0X5;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(mActy).get(MainViewModel.class);

        SettingAdapter adapter = new SettingAdapter();
        binding.rvSetting.setAdapter(adapter);
        binding.rvSetting.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx,5)));
        adapter.setSettingInterface(new SettingAdapter.SettingInterface() {
            @Override
            public void handleClick(int code) {
                switch (code) {
                    case ABOUT:
                        about();
                        break;
                    case RELOAD_DATABASE:
                        vm.reloadDatabase();
                        break;
                    case FOLLOW:
                        follow();
                        break;
                    case JOIN_GROUP:
                        joinGroup();
                        break;
                    case GROUP_CALC:
                        groupCalc();
                        break;
                }
            }
        });
        adapter.submitList(getSettings());
    }

    private List<SettingEntity> getSettings() {
        List<SettingEntity> list = new ArrayList<>();
        list.add(new SettingEntity(RELOAD_DATABASE, "重载数据库"));
        list.add(new SettingEntity(FOLLOW, "关注作者"));
        list.add(new SettingEntity(JOIN_GROUP, "加QQ群"));
        list.add(new SettingEntity(ABOUT, "关于"));
        list.add(new SettingEntity(GROUP_CALC, "编队计算"));
        return list;
    }

    //关注
    private void follow() {
        final CharacterDialog cd = new CharacterDialog(ctx);
        CharacterEntity e = new CharacterEntity("FGOcalc的作者，如果喜欢该app，欢迎去B站关注、硬币、收藏、点赞、充电......"
                ,R.drawable.altria_a);
        e.options = Stream.of(
                //选项1：关注b站
                new CharacterEntity.OptionEntity("去Bilibili关注TA",() -> {
                    if (!ToolCase.openBrowser(ctx,"https://space.bilibili.com/532252/#/")) {
                        cd.dismiss();
                        new NetCharacter(ctx).onError();
                    } else {
                        cd.dismiss();
                    }
                }),
                //选项2：加群
                new CharacterEntity.OptionEntity("加群",() -> {
                    cd.dismiss();
                    joinGroup();
                })
        ).collect(Collectors.toList());
        cd.setEntity(e);
        cd.show();
    }

    //加群
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void joinGroup() {
        if (ToolCase.copy2Clipboard("422969398")) {
            final CharacterDialog cd = new CharacterDialog(ctx);
            CharacterEntity en = new CharacterEntity("QQ群号已复制剪切板，快去加群吧！", R.drawable.altria_alter_a);
            //设置选项
            en.options = Stream.of(
                    //选项-好的
                    new CharacterEntity.OptionEntity("好的", () -> cd.dismiss())
            ).collect(Collectors.toList());
            //设置弹窗内容
            cd.setEntity(en);
            cd.show();
        }
    }

    //关于
    private void about(){
        Intent i = new Intent(mActy, AboutActy.class);
        startActivity(i);
    }

    private void groupCalc(){
        Intent i = new Intent(mActy, GroupCalcActy.class);
        startActivity(i);
    }

    private Bitmap editCharacter(int resId, double widthMultiplier, double heightMultiplier) {
        Bitmap charId = BitmapFactory.decodeResource(getResources(), resId, null);
        return Bitmap.createBitmap(charId, 0, 0, (int) (charId.getWidth() * widthMultiplier),
                (int) (charId.getHeight() * heightMultiplier));
    }
}
