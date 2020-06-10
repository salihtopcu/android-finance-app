package com.gelistirmen.finance.data.local;

import android.content.SharedPreferences;

import com.gelistirmen.finance.MyApplication;

import java.util.Set;

public final class Cache {
    private static SharedPreferences sharedPreferences;
    private static String SHARED_PREFERENCES_NAME = "UserInformation";
    private static String KEY_ACCESS_TOKEN = "AccessToken";
    private static String KEY_REFRESH_TOKEN = "RefreshToken";
    private static String KEY_USER_ID = "UserId";
    private static String KEY_REQUIRES_PASSWORD_CHANGE = "RequiresPasswordChange";
    private static String KEY_LANGUAGE = "Language";
    private static String KEY_SHOW_MOBILE_SIGNATURE_INFO = "ShowMobileSignatureInfo";
    private static String KEY_USER_PHONE_NUMBER = "UserPhoneNumber";

    private static  SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = MyApplication.instance.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        return sharedPreferences;
    }

    private static void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getStringValue(String key) {
        return getSharedPreferences().getString(key, "");
    }

    private static void setStringSetValue(String key, Set<String> set) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    private static Set<String> getStringSetValue(String key) {
        return getSharedPreferences().getStringSet(key, null);
    }

    private static void setIntegerValue(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getIntegerValue(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    private static void setBoolValue(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBoolValue(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static void setAccessToken(String value) {
        setStringValue(KEY_ACCESS_TOKEN, value);
    }

    public static String getAccessToken() {
        return getStringValue(KEY_ACCESS_TOKEN);
    }

    public static void setRefreshToken(String value) {
        setStringValue(KEY_REFRESH_TOKEN, value);
    }

    public static String getRefreshToken() {
        return getStringValue(KEY_REFRESH_TOKEN);
    }

    public static void setUserId(String value) {
        setStringValue(KEY_USER_ID, value);
    }

    public static String getUserId() {
        return getStringValue(KEY_USER_ID);
    }

    public static void setRequiresPasswordChange(boolean value) {
        setBoolValue(KEY_REQUIRES_PASSWORD_CHANGE, value);
    }

    public static boolean getRequiresPasswordChange() {
        return getBoolValue(KEY_REQUIRES_PASSWORD_CHANGE);
    }

    public static void setApplicationLanguage(String value) {
        setStringValue(KEY_LANGUAGE, value);
    }

    public static String getApplicationLanguage() {
        return getStringValue(KEY_LANGUAGE);
    }

    public static void setShowMobileSignatureInfo(boolean value) {
        setBoolValue(KEY_SHOW_MOBILE_SIGNATURE_INFO, value);
    }

    public static boolean getShowMobileSignatureInfo() {
        return getBoolValue(KEY_SHOW_MOBILE_SIGNATURE_INFO);
    }

    public static void setUserPhoneNumber(String value) {
        setStringValue(KEY_USER_PHONE_NUMBER, value);
    }

    public static String getUserPhoneNumber() {
        return getStringValue(KEY_USER_PHONE_NUMBER);
    }

}
