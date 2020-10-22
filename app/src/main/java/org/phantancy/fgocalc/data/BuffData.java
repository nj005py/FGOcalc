package org.phantancy.fgocalc.data;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.BuffInputEntity;

import java.util.ArrayList;
import java.util.List;

public class BuffData {
    public static List<BuffInputEntity> buildBuffs() {
        return new ArrayList<BuffInputEntity>(){
            {
                add(new BuffInputEntity("全局",2));
                //攻击类
                add(new BuffInputEntity(R.drawable.atk_up,"加攻",0,1));
                add(new BuffInputEntity(R.drawable.critical_up,"暴击",0,1));
                add(new BuffInputEntity(R.drawable.quick_up,"绿暴击",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝暴击",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红暴击",0,1));
                add(new BuffInputEntity(R.drawable.special_up,"特攻",0,1));
                add(new BuffInputEntity(R.drawable.weak_up,"固定伤害",0,0));

                add(new BuffInputEntity(R.drawable.quick_up,"绿魔放",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝魔放",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红魔放",0,1));
                //充能类
                add(new BuffInputEntity(R.drawable.npc_up,"黄金率",0,1));
                //打星类
                add(new BuffInputEntity(R.drawable.star_up,"星掉率",0,1));

                //宝具副效果类
                add(new BuffInputEntity("宝具效果：伤害前",2));
                //基本跟着宝具走
                add(new BuffInputEntity(R.drawable.np_special_up,"宝具特攻前",0,1));
                add(new BuffInputEntity(R.drawable.image66,"附加倍率前",0,1));
                //伤害类
                add(new BuffInputEntity(R.drawable.atk_up,"加攻前",0,1));
                add(new BuffInputEntity(R.drawable.critical_up,"暴击前",0,1));

                add(new BuffInputEntity(R.drawable.quick_up,"绿暴击前",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝暴击前",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红暴击前",0,1));

                add(new BuffInputEntity(R.drawable.quick_up,"绿魔放前",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝魔放前",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红魔放前",0,1));
                add(new BuffInputEntity(R.drawable.npc_up,"黄金率前",0,1));
                add(new BuffInputEntity(R.drawable.star_up,"星掉率前",0,1));
                //宝具伤害后
                add(new BuffInputEntity("宝具效果：伤害后",2));
                //伤害类
                add(new BuffInputEntity(R.drawable.atk_up,"加攻后",0,1));
                add(new BuffInputEntity(R.drawable.critical_up,"暴击后",0,1));
                add(new BuffInputEntity(R.drawable.quick_up,"绿暴击后",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝暴击后",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红暴击后",0,1));
                add(new BuffInputEntity(R.drawable.quick_up,"绿魔放后",0,1));
                add(new BuffInputEntity(R.drawable.arts_up,"蓝魔放后",0,1));
                add(new BuffInputEntity(R.drawable.buster_up,"红魔放后",0,1));
                add(new BuffInputEntity(R.drawable.npc_up,"黄金率后",0,1));
                add(new BuffInputEntity(R.drawable.star_up,"星掉率后",0,1));
            }
        };
    }
}