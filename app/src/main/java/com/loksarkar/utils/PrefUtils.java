package com.loksarkar.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static PrefUtils mSharedPreferenceUtils;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    private String APP_PREF = "loksarkar";
    public static String KEY_USERID = "";
    public static final String LOGIN_KEY = "loksarkar.login";
    public static final String LANGUGAE_KEY = "loksarkar.language";
    public static final String REFERRAL_ID_KEY = "loksarkar.referralid";
    public static final String USERNAME_KEY = "loksarkar.username";
    public static final String EMAIL_KEY = "loksarkar.email";
    public static final String MOB_KEY = "loksarkar.mobile";
    public static final String ADDRESS_KEY = "loksarkar.address";
    public static final String SHARED_PREF = "loksarkar.fcmTokenId";
    public static final String NO_STRING_VALUE = "bnc_no_value";
    public static final String isFirstTimeKey = "isFirstTime";
    public static final String REWARD_POINTS = "rewardPoints";


    private PrefUtils(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    /**
     * Creates single instance of SharedPreferenceUtils
     *
     * @param context context of Activity or Service
     * @return Returns instance of SharedPreferenceUtils
     */
    public static synchronized PrefUtils getInstance(Context context) {

        if (mSharedPreferenceUtils == null) {
            mSharedPreferenceUtils = new PrefUtils(context.getApplicationContext());
        }
        return mSharedPreferenceUtils;
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    public void setUserLogin(){
        mSharedPreferencesEditor.putBoolean(LOGIN_KEY, true);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    public boolean getUserLogin(){
       return mSharedPreferences.getBoolean(LOGIN_KEY,false);
    }

    public void setUserLogout(){
        mSharedPreferencesEditor.putBoolean(LOGIN_KEY, false);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    public void setLangugae(){
        mSharedPreferencesEditor.putBoolean(LANGUGAE_KEY, true);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    public boolean isLanguageSelected(){
        return mSharedPreferences.getBoolean(LANGUGAE_KEY,false);
    }


    public void setIsFirstTime(boolean value){
        mSharedPreferencesEditor.putBoolean(isFirstTimeKey, value);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    public boolean isFirstTime(){
        return mSharedPreferences.getBoolean(isFirstTimeKey,false);
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    /**
     * Stores Double value in String format in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /**
     * Stores long value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, long value) {
        mSharedPreferencesEditor.putLong(key, value);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
        mSharedPreferencesEditor.apply();
    }

    /**
     * Retrieves String value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieves int value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    public int getIntValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieves long value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    public long getLongValue(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue);
    }




    /**
     * Removes key from preference
     *
     * @param key key of preference that is to be deleted
     */
    public void removeKey(String key) {
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.remove(key);
            mSharedPreferencesEditor.commit();
            mSharedPreferencesEditor.apply();
        }
    }



    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }


}
