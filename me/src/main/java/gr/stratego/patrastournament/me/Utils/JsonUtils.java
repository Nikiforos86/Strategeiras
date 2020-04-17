package gr.stratego.patrastournament.me.Utils;

import android.content.Intent;

import com.google.gson.Gson;

public class JsonUtils {

    public static Object convertToDesiredObject(String json, Class<?> objectType) {
        if (json == null) {
            return null;
        }
        Object returnObject = new Gson().fromJson(json, objectType);
        return returnObject;
    }

    public static Object convertToDesiredObject(Intent intent, String valueName, Class<?> objectType) {
        try {
            String json = intent.getStringExtra(valueName);
            if (json != null) {
                Object returnObject = new Gson().fromJson(json, objectType);
                return returnObject;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String convertJsonObjectToString(Object object) {
        try {
            String objectString = new Gson().toJson(object);
            return objectString;
        } catch (Exception e) {
            return "";
        }
    }

    public static Intent convertJsonObjectToString(Intent intent, String valueName, Object object) {
        try {
            String objectString = convertJsonObjectToString(object);
            if (!StringUtils.isNullOrEmpty(objectString)) {
                intent.putExtra(valueName, objectString);
            }
        } catch (Exception e) {
        }
        return intent;
    }
}