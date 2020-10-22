package org.phantancy.fgocalc.dialog;

import android.content.Context;
import androidx.appcompat.app.AppCompatDialog;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.ShortcutMstEquipmentAdapter;
import org.phantancy.fgocalc.entity.IBuffShortCut;
import org.phantancy.fgocalc.databinding.DialogShortcutMstEquipmentBinding;
import org.phantancy.fgocalc.entity.ShortcutMstEquipmentEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShortcutMstEquipmentDialog extends AppCompatDialog {

    private DialogShortcutMstEquipmentBinding binding;
    public ShortcutMstEquipmentDialog(Context context, IBuffShortCut buffShortCut) {
        super(context);
        binding = DialogShortcutMstEquipmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ShortcutMstEquipmentAdapter adapter = new ShortcutMstEquipmentAdapter(buffShortCut);
        binding.rvMstEquipment.setAdapter(adapter);
        adapter.submitList(getData());
    }

    private List<ShortcutMstEquipmentEntity> getData() {
        return new ArrayList<ShortcutMstEquipmentEntity>(){{
            add(new ShortcutMstEquipmentEntity(R.drawable.mst001a,R.drawable.mst001b,"迦勒底制服\n50加攻",new HashMap<String,Double>(){{
                put("加攻",50d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst002a,R.drawable.mst002b,"战斗服\n30加攻",new HashMap<String,Double>(){{
                put("加攻",30d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst005a,R.drawable.mst005b,"金色庆典\n60红魔放",new HashMap<String,Double>(){{
                put("红魔放",60d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst006a,R.drawable.mst006b,"王室名牌\n50绿魔放",new HashMap<String,Double>(){{
                put("绿魔放",50d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst007a,R.drawable.mst007b,"闪耀夏日\n30绿魔放",new HashMap<String,Double>(){{
                put("绿魔放",30d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst008a,R.drawable.mst008b,"月海的记忆\n50蓝魔放",new HashMap<String,Double>(){{
                put("蓝魔放",50d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst009a,R.drawable.mst009b,"月海里侧的记忆\n30蓝魔放",new HashMap<String,Double>(){{
                put("蓝魔放",30d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst010a,R.drawable.mst010b,"2004年的断片\n50宝具威力 50黄金率",new HashMap<String,Double>(){{
                put("宝具威力",50d);
                put("黄金率",50d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst011a,R.drawable.mst011b,"极地服\n40加攻 20宝具威力",new HashMap<String,Double>(){{
                put("加攻",40d);
                put("宝具威力",20d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst012a,R.drawable.mst012b,"热带的夏季\n30蓝魔放 20宝具威力",new HashMap<String,Double>(){{
                put("蓝魔放",30d);
                put("宝具威力",20d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst013a,R.drawable.mst013b,"愉快的新年\n宝具威力35",new HashMap<String,Double>(){{
                put("宝具威力",35d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst013a,R.drawable.mst013b,"迦勒底队长\n宝具威力50暴击50",new HashMap<String,Double>(){{
                put("宝具威力",50d);
                put("暴击",50d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst014a,R.drawable.mst014b,"第五真实元素环境\n红魔放35 黄金率40",new HashMap<String,Double>(){{
                put("红魔放",35d);
                put("黄金率",40d);
            }}));
            add(new ShortcutMstEquipmentEntity(R.drawable.mst015a,R.drawable.mst015b,"开拓者\n绿魔放30 宝具威力20 暴击30",new HashMap<String,Double>(){{
                put("绿魔放",30d);
                put("宝具威力",20d);
                put("暴击",30d);
            }}));
        }};
    }
}
