package org.phantancy.fgocalc.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.phantancy.fgocalc.item.UpdateItem;

import java.lang.reflect.Type;

/**
 * Created by HATTER on 2017/12/19.
 */

public class JsonUtils {

    public static UpdateItem getUpdateItem(String result){
        try {
            UpdateItem item = new UpdateItem();
            JSONObject jo = new JSONObject(result);
            String status = jo.optString("status");
            if (status.equals("Success")) {
                JSONObject jo2 = jo.optJSONObject("data");
                Gson gson = new Gson();
                Type type = new TypeToken<UpdateItem>(){}.getType();
                item = gson.fromJson(jo2.toString(),type);
            }
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
