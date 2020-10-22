package org.phantancy.fgocalc.dialog;

import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.ShortcutSupportAdapter;
import org.phantancy.fgocalc.entity.IBuffShortCut;
import org.phantancy.fgocalc.databinding.DialogShortcutSupportBinding;
import org.phantancy.fgocalc.entity.ShortcutSupportEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShortcutSupportDialog extends AppCompatDialog {

    private DialogShortcutSupportBinding binding;

    public ShortcutSupportDialog(Context context, IBuffShortCut buffShortCut) {
        super(context);
        binding = DialogShortcutSupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ShortcutSupportAdapter adapter = new ShortcutSupportAdapter(buffShortCut);
        binding.rvShortcutSupport.setAdapter(adapter);
        adapter.submitList(getData());
    }

    private List<ShortcutSupportEntity> getData() {
        return new ArrayList<ShortcutSupportEntity>() {{
            add(new ShortcutSupportEntity(R.drawable.image37, "30无法无天\n加攻、暴击、固伤\n精彩！！！", new HashMap<String, Double>() {{
                put("加攻", 30d);
                put("暴击", 50d);
                put("固定伤害", 500d);
            }}));
            add(new ShortcutSupportEntity(R.drawable.image150, "30为所欲为\n加攻、红魔放、暴击\n打他丫的！！！", new HashMap<String, Double>() {{
                put("加攻", 20d);
                put("红魔放", 50d);
                put("暴击", 100d);
            }}));
            add(new ShortcutSupportEntity(R.drawable.image62, "30狐作妃为\n蓝魔放、宝具威力\n咪咕！咪咕！", new HashMap<String, Double>() {{
                put("宝具威力", 30d);
                put("蓝魔放", 50d);

            }}));
            add(new ShortcutSupportEntity(R.drawable.image215, "30斯无忌惮\n绿魔放、绿卡暴击、减防\n谁在喊bba,cba?！", new HashMap<String, Double>() {{
                put("加攻", 30d);
                put("绿魔放", 50d);
                put("绿暴击", 100d);

            }}));
            add(new ShortcutSupportEntity(R.drawable.image284, "30槑上梅下\n蓝魔放、加攻、黄金率\n在座的各位都是垃圾啊！！！", new HashMap<String, Double>() {{
                put("加攻", 20d);
                put("蓝魔放", 50d);
                put("黄金率", 30d);
            }}));
            add(new ShortcutSupportEntity(R.drawable.image90, "30唔姆唔姆\n加攻、黄金率\n唔姆！唔姆！", new HashMap<String, Double>() {{
                put("加攻", 40d);
                put("黄金率", 45d);
            }}));
            add(new ShortcutSupportEntity(R.drawable.image241, "30良树飞马\n怪力Ex\n哦尼酱！！！", new HashMap<String, Double>() {{
                put("加攻", 40d);
            }}));
        }};
    }

}
