package com.kraftlabs.crm_new.Util;

/**
 * Created by ajith on 16/7/16.
 */

import android.content.Context;

import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.Preferences.ComplexPreferences;
import com.kraftlabs.crm_new.Preferences.UserIdPref;

public class PrefUtils {

    public static void setCurrentUser(User currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static User getCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        User currentUser = complexPreferences.getObject("current_user_value", User.class);
        return currentUser;
    }

    public static void clearCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void setCurrentUserId(User user, Context ctx) {
        UserIdPref userIdPref = UserIdPref.getUserIdPref(ctx, "user_id_prefs", 0);
        userIdPref.putObject("current_user_id", user);
        userIdPref.commit();
    }

    public static User getCurrentUserId(Context ctx) {
        UserIdPref user_id_prefs = UserIdPref.getUserIdPref(ctx, "user_id_prefs", 0);
        User currentUser = user_id_prefs.getObject("current_user_id", User.class);
        return currentUser;
    }



}
