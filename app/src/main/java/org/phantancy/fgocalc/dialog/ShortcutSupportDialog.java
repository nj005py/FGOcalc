package org.phantancy.fgocalc.dialog;

import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.ShortcutSupportAdapter;
import org.phantancy.fgocalc.data.BuffData;
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
        List<ShortcutSupportEntity> list = new ArrayList<>();
        list.add(new ShortcutSupportEntity(R.drawable.image37, "30无法无天\n加攻、暴击、固伤\n精彩！！！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 30d);
            put(BuffData.CRITICAL_UP, 50d);
            put(BuffData.SELF_DAMAGE_UP, 500d);
        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image150, "30为所欲为\n加攻、红魔放、暴击\n打他丫的！！！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 20d);
            put(BuffData.BUSTER_UP, 50d);
            put(BuffData.CRITICAL_UP, 100d);
        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image62, "30狐作妃为\n蓝魔放、宝具威力\n咪咕！咪咕！", new HashMap<String, Double>() {{
            put(BuffData.NP_POWER_UP, 30d);
            put(BuffData.ARTS_UP, 50d);

        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image215, "30斯无忌惮\n绿魔放、绿卡暴击、减防\n谁在喊bba,cba?！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 30d);
            put(BuffData.QUICK_UP, 50d);
            put(BuffData.CRITICAL_QUICK_UP, 100d);

        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image284, "30槑上梅下\n蓝魔放、加攻、黄金率\n在座的各位都是垃圾啊！！！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 20d);
            put(BuffData.ARTS_UP, 50d);
            put(BuffData.NPC_UP, 30d);
        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image90, "30唔姆唔姆\n加攻、黄金率\n唔姆！唔姆！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 40d);
            put(BuffData.NPC_UP, 45d);
        }}));
        list.add(new ShortcutSupportEntity(R.drawable.image241, "30良树飞马\n怪力Ex\n哦尼酱！！！", new HashMap<String, Double>() {{
            put(BuffData.ATK_UP, 40d);
        }}));
       return list;
    }

}
