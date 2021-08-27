package org.phantancy.fgocalc.data;

import android.text.TextUtils;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.ServantEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServantAvatar {
    static String BASE_URL = "http://fgo-cdn.vgtime.com/media/fgo/servant/head/";
    public static String AVATAR_URL = "https://gitee.com/nj005py/fgocalc/raw/master/svt/";

    public static List<ServantEntity> setAvatars(List<ServantEntity> svt) {
        for (ServantEntity x : svt) {
            try {
                x.avatarRes = servantAvatar.get(x.id);
            } catch (Exception e) {
//                String id = String.format("%03d",x.id);
//                x.avatarUrl = new StringBuilder().append(BASE_URL).append(id).append(".jpg").toString();
            }
        }
        return svt;
    }

    public static Map<Integer, Integer> servantAvatar = new HashMap<Integer, Integer>();
    static {
        for (int id = 1; id <= 316; id++){
            int resId = getResIdByName("image" + id);
            servantAvatar.put(id,resId);
        }
    }

    public static int getServantAvatar(int id) {
        return servantAvatar.get(id);
    }

    private static int getResIdByName(String name) {
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
