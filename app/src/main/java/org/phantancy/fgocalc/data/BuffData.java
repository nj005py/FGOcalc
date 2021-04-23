package org.phantancy.fgocalc.data;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.BuffInputEntity;

import java.util.ArrayList;
import java.util.List;

public class BuffData {
    public static String ATK_UP = "加攻";
    public static String CRITICAL_UP = "暴击";
    public static String CRITICAL_QUICK_UP = "绿暴击";
    public static String CRITICAL_ARTS_UP = "蓝暴击";
    public static String CRITICAL_BUSTER_UP = "红暴击";
    public static String SPECIAL_UP = "特攻";
    public static String SELF_DAMAGE_UP = "固定伤害";
    public static String QUICK_UP = "绿魔放";
    public static String ARTS_UP = "蓝魔放";
    public static String BUSTER_UP = "红魔放";
    public static String NPC_UP = "黄金率";
    public static String STAR_UP = "星掉率";

    public static String NP_SPECICAL_UP_BE = "宝具特攻前";
    public static String NP_MULTIPLIER_UP_BE = "附加倍率前";
    public static String ATK_UP_BE = "加攻前";
    public static String CRITICAL_UP_BE = "暴击前";
    public static String CRITICAL_QUICK_UP_BE = "绿暴击前";
    public static String CRITICAL_ARTS_UP_BE = "蓝暴击前";
    public static String CRITICAL_BUSTER_UP_BE = "红暴击前";
    public static String QUICK_UP_BE = "绿魔放前";
    public static String ARTS_UP_BE = "蓝魔放前";
    public static String BUSTER_UP_BE = "红魔放前";
    public static String NPC_UP_BE = "黄金率前";
    public static String STAR_UP_BE = "星掉率前";

    public static String ATK_UP_AF = "加攻后";
    public static String CRITICAL_UP_AF = "暴击后";
    public static String CRITICAL_QUICK_UP_AF = "绿暴击后";
    public static String CRITICAL_ARTS_UP_AF = "蓝暴击后";
    public static String CRITICAL_BUSTER_UP_AF = "红暴击后";
    public static String QUICK_UP_AF = "绿魔放后";
    public static String ARTS_UP_AF = "蓝魔放后";
    public static String BUSTER_UP_AF = "红魔放后";
    public static String NPC_UP_AF = "黄金率后";
    public static String STAR_UP_AF = "星掉率后";

    public static List<BuffInputEntity> buildBuffs() {
        return new ArrayList<BuffInputEntity>(){
            {
                add(new BuffInputEntity("全局",2));
                //攻击类
                add(new BuffInputEntity(R.drawable.atk_up,ATK_UP,0,1));
                add(new BuffInputEntity(R.drawable.critical_up,CRITICAL_UP,0,1));
                add(new BuffInputEntity(R.drawable.quick_up,CRITICAL_QUICK_UP,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,CRITICAL_ARTS_UP,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,CRITICAL_BUSTER_UP,0,1));
                add(new BuffInputEntity(R.drawable.special_up,SPECIAL_UP,0,1));
                add(new BuffInputEntity(R.drawable.weak_up,SELF_DAMAGE_UP,0,0));

                add(new BuffInputEntity(R.drawable.quick_up,QUICK_UP,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,ARTS_UP,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,BUSTER_UP,0,1));
                //充能类
                add(new BuffInputEntity(R.drawable.npc_up,NPC_UP,0,1));
                //打星类
                add(new BuffInputEntity(R.drawable.star_up,STAR_UP,0,1));

                //宝具副效果类
                add(new BuffInputEntity("宝具效果：宝具伤害前",2));
                //基本跟着宝具走
                add(new BuffInputEntity(R.drawable.np_special_up,NP_SPECICAL_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.image66,NP_MULTIPLIER_UP_BE,0,1));
                //伤害类
                add(new BuffInputEntity(R.drawable.atk_up,ATK_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.critical_up,CRITICAL_UP_BE,0,1));

                add(new BuffInputEntity(R.drawable.quick_up,CRITICAL_QUICK_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,CRITICAL_ARTS_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,CRITICAL_BUSTER_UP_BE,0,1));

                add(new BuffInputEntity(R.drawable.quick_up,QUICK_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,ARTS_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,BUSTER_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.npc_up,NPC_UP_BE,0,1));
                add(new BuffInputEntity(R.drawable.star_up,STAR_UP_BE,0,1));
                //宝具伤害后
                add(new BuffInputEntity("宝具效果：宝具伤害后",2));
                //伤害类
                add(new BuffInputEntity(R.drawable.atk_up,ATK_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.critical_up,CRITICAL_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.quick_up,CRITICAL_QUICK_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,CRITICAL_ARTS_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,CRITICAL_BUSTER_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.quick_up,QUICK_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.arts_up,ARTS_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.buster_up,BUSTER_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.npc_up,NPC_UP_AF,0,1));
                add(new BuffInputEntity(R.drawable.star_up,STAR_UP_AF,0,1));
            }
        };
    }
}