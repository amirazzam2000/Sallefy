package edu.url.salle.amir.azzam.sallefy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static String LOGIN_COLLECTION = "userPreferences";
    private static String KEY_USER = "userLogin";
    private static String KEY_PASSWORD = "password";
    private static String KEY_TOKEN = "token";



    public PreferenceUtils() {
    }



    public static boolean saveUser (Context context, String login) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_USER, login);
        prefsEditor.apply();
        return true;
    }

    public static String getUser (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER, null);
    }

    public static boolean savePassword (Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_PASSWORD, userId);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, null);
    }

    public static boolean saveToken(Context context, String userToken){
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_TOKEN, userToken);
        prefsEditor.apply();
        return true;
    }

    public static String getToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void resetValues(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_COLLECTION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(KEY_PASSWORD, null);
        prefsEditor.putString(KEY_USER, null);
        prefsEditor.putString(KEY_TOKEN, null);
        prefsEditor.apply();
    }
}
