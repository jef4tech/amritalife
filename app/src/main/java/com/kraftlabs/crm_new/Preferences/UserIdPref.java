package com.kraftlabs.crm_new.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * User: ashik
 * Date: 19/9/17
 * Time: 4:11 PM
 */

public class UserIdPref {

    private static UserIdPref userIdPref;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private UserIdPref(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "userIdPref";
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static UserIdPref getUserIdPref(Context context, String namePreferences, int mode) {

        if (userIdPref == null) {
            userIdPref = new UserIdPref(context,
                                                        namePreferences, mode
            );
        }

        return userIdPref;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
    }

    public void clearObject() {
        editor.clear();
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }

}
