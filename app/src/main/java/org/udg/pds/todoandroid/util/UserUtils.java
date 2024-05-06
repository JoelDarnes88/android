package org.udg.pds.todoandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.udg.pds.todoandroid.activity.Login;

public class UserUtils {
    public static Long getCurrentUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Login.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        String userIdStr = sharedPreferences.getString("id", null);
        if (userIdStr != null) {
            return Long.parseLong(userIdStr);
        }
        return null;
    }
}
