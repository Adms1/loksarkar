package com.loksarkar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InstallReferrerHelper {


    private static final String KEY_REFERRER = "referrer";
    private static final String REFERRER_DATE = "REFERRER_DATE";
    private static final String REFERRER_DATA = "REFERRER_DATA";


    public static boolean isReferrerDetected(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).contains(REFERRER_DATE);
    }

    public static void setReferrerDate(Context context, long date) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.contains(REFERRER_DATE)) {
            sp.edit().putLong(REFERRER_DATE, date).apply();
        }
    }

    public static String getReferrerDate(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.contains(REFERRER_DATE)) {
            return "Undefined";
        }

        Date date = new Date(sp.getLong(REFERRER_DATE, new Date().getTime()));
        return DateFormat.getDateInstance().format(date) + " - " + new SimpleDateFormat("HH:mm:ss.SSS").format(date);
    }

    public static void setReferrerData(Context context, String data) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.contains(REFERRER_DATA)) {
            sp.edit().putString(REFERRER_DATA,data).apply();
        }
    }

    public static String getReferrerDataRaw(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (!sp.contains(REFERRER_DATA)) {
            return "Undefined";
        }
        return sp.getString(REFERRER_DATA, null);
    }

    public static String getReferrerDataDecoded(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String raw = sp.getString(REFERRER_DATA, null);

        if (raw == null) {
            return null;
        }

        try {
            String url = URLDecoder.decode(raw, "utf-8");
            try {
                String url2x = URLDecoder.decode(url, "utf-8");
                if (raw.equals(url2x)) {
                    return null;
                }
                return url2x;
            } catch (UnsupportedEncodingException uee) {
                // not URL 2x encoded but URL encoded
                if (raw.equals(url)) {
                    return null;
                }
                return url;
            }
        } catch (UnsupportedEncodingException uee) {
            // not URL encoded
        }
        return null;
    }

    public static boolean isInstallRefApiAvailable() {
        try {
            Class.forName("com.android.installreferrer.api.InstallReferrerStateListener");
            return true;
        } catch (Exception ignored) {}
        return false;
    }



    public static void setInstallReferrer(Context context, String referrer) {
        if (context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            if (!sp.contains(REFERRER_DATA)) {
                sp.edit().putString(REFERRER_DATA,referrer).apply();
            }
        }
    }



    public static void fetchInstallReferrer(final Context context, final InstallReferrerCallback callback) {
        if (isInstallRefApiAvailable()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        final InstallReferrerClient mReferrerClient = InstallReferrerClient.newBuilder(context).build();
                        InstallReferrerStateListener listener = new InstallReferrerStateListener() {
                            @Override
                            public void onInstallReferrerSetupFinished(int responseCode) {
                                switch (responseCode) {
                                    case InstallReferrerClient.InstallReferrerResponse.OK:
                                        try {
                                            ReferrerDetails response = mReferrerClient.getInstallReferrer();
                                            if (response != null) {
                                                callback.onReceived(response.getInstallReferrer());
                                                setInstallReferrer(context,response.getInstallReferrer());
                                                setReferrerDate(context,response.getInstallBeginTimestampSeconds());
                                            } else {
                                                callback.onFailed();
                                            }
                                            mReferrerClient.endConnection();
                                        } catch (RemoteException e) {
                                            Log.d("TAG","InstallReferrer Remote Exception, using InstallReferrer from intent");
                                            callback.onFailed();
                                        }
                                        break;
                                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                                        Log.d("TAG","InstallReferrer not supported, using InstallReferrer from intent");
                                        callback.onFailed();
                                        break;
                                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                                        Log.d("TAG","Unable to connect to InstallReferrer service, using InstallReferrer from intent");
                                        callback.onFailed();
                                        break;
                                    default:
                                        Log.d("TAG","InstallReferrer responseCode not found, using InstallReferrer from intent");
                                        callback.onFailed();
                                }
                            }

                            @Override
                            public void onInstallReferrerServiceDisconnected() {

                            }
                        };
                        mReferrerClient.startConnection(listener);
                    }
                    catch (Exception e) {
                        Log.e("TAG","Exception while fetching install referrer: " + e.getMessage());
                        callback.onFailed();
                    }
                }
            };
            try {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    new Thread(runnable).start();
                } else {
                    runnable.run();
                }
            } catch (Exception ignored) {
                callback.onFailed();
             }
        } else {
            callback.onFailed();
        }
    }

    public interface InstallReferrerCallback {
        void onReceived(String installReferrer);
        void onFailed();
    }
}
