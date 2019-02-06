package com.loksarkar.base;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.AutoStartPermissionHelper;
import com.loksarkar.utils.DeviceUtils;
import com.loksarkar.utils.InstallReferrerHelper;
import com.loksarkar.utils.PrefUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import retrofit.RetrofitError;
import retrofit.client.Response;


public class BaseApp extends Application {


    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("gu", "IN"),
            new Locale("hi", "IN"),
            new Locale("en", "US"));

    private InterstitialAd mInterstitialAd;
    private Handler mHandler;
    private Runnable displayAd;

    @Override
    public void onCreate(){
        super.onCreate();

//        Branch.setPlayStoreReferrerCheckTimeout(2000);
//        Branch.getAutoInstance(this);


        MultiDex.install(this);

       // AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());

        DeviceUtils.LogDeviceInfo(this);


        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

        LocaleChanger.initialize(getApplicationContext(),SUPPORTED_LOCALES);

      //  PrefUtils.getInstance(this).setIsFirstTime(false);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
        String token = pref.getString("regId", "");
        Log.d("fcmtoken",token);

        /// LocaleChanger.setLocale(SUPPORTED_LOCALES.get(2));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }





}
