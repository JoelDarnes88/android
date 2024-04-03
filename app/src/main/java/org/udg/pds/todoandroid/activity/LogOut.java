package org.udg.pds.todoandroid.activity;

import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;

import org.udg.pds.todoandroid.TodoApp;

public class LogOut {

    public void performLogout(Context context) {
        TodoApp app = (TodoApp) context.getApplicationContext();
        ClearableCookieJar cookieJar = app.getCookieJar();

        // Clear all cookies
        Log.d("LogOut", "Limpiando cookies...");
        cookieJar.clear();

        // Clear any saved user session or preferences
        app.clearSharedPreferences();

        // Redirect to Login Screen
        Intent intent = new Intent(context, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    // Add this method in TodoApp class


}
