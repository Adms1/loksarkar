package com.loksarkar.base;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.loksarkar.R;
import com.loksarkar.activities.BaseActivity;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DeviceUtils;
import com.loksarkar.utils.InstallReferrerHelper;
import com.loksarkar.utils.PrefUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class BaseApp extends Application implements Application.ActivityLifecycleCallbacks, InstallReferrerHelper.InstallReferrerCallback {


    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("gu", "IN"),
            new Locale("hi", "IN"),
            new Locale("en", "US"));
    public ScheduledExecutorService scheduler;
    private InterstitialAd mInterstitialAd;
    private Handler mHandler;
    private Runnable displayAd;

    @Override
    public void onCreate() {
        super.onCreate();

//        Branch.setPlayStoreReferrerCheckTimeout(2000);
//        Branch.getAutoInstance(this);

        registerActivityLifecycleCallbacks(this);

        MultiDex.install(this);

        // AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());

        DeviceUtils.LogDeviceInfo(this);


        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

        LocaleChanger.initialize(getApplicationContext(), SUPPORTED_LOCALES);

        //  PrefUtils.getInstance(this).setIsFirstTime(false);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
        String token = pref.getString("regId", "");
        Log.d("fcmtoken", token);

        /// LocaleChanger.setLocale(SUPPORTED_LOCALES.get(2));
        MobileAds.initialize(this,"ca-app-pub-7793510975061206~8275052408");

        try {
            InstallReferrerHelper.fetchInstallReferrer(this, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7793510975061206/3828788355");
        //testing unit id
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mHandler = new Handler(Looper.getMainLooper());



        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                            //mInterstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }
                        loadAd();
                    }
                });
            }
        }, 0, 30, TimeUnit.SECONDS);



    }

    private void loadAd() {
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("918DB44296FAF473DC4180E450381834")
//                .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//        .addTestDevice(AppUtils.getDeviceId(this))
                .build();

        mInterstitialAd.loadAd(request);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //displayInterstitial();
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("AdError", String.valueOf(errorCode));
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.'
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                // loadAd();
            }
        });


    }

    private void sendDeviceIdWithToken(final String token, String referralCode) {
        // sending fcm token to server

        if (!AppUtils.isNetworkConnected(this)) {
            AppUtils.ping(getApplicationContext(), getResources().getString(R.string.internet_error));
            return;
        }

        ApiHandler.getApiService().registerDeviceNew(setDeviceDetail(token, referralCode), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel registrationModel, Response response) {
                AppUtils.dismissDialog();
                if (registrationModel == null) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess() == null) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("false")) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("True")) {
                    PrefUtils.getInstance(getApplicationContext()).setIsFirstTime(true);
                    return;
                }

            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setDeviceDetail(String token, String referralCode) {
        Map<String, String> map = new HashMap<>();
        map.put("DeviceToken", token);
        map.put("DeviceID", AppUtils.getDeviceId(this));

        if (referralCode.equals("utm_source=google-play&utm_medium=organic") || referralCode.contains("utm_source")) {
            map.put("ReferralCode", "");
        } else {
            map.put("ReferralCode", referralCode);

        }

        return map;
    }

    @Override
    public void onReceived(String installReferrer) {
        try {

            if (installReferrer != null) {

                if (!PrefUtils.getInstance(this).isFirstTime()) {

                    Log.d("referral_code", installReferrer);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
                    String token = pref.getString("regId", "");

                    if (token != null && !TextUtils.isEmpty(token)) {
                        sendDeviceIdWithToken(token, installReferrer);
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }

    //Call displayInterstitial() once you are ready to display the ad.
    public void displayInterstitial() {
        mHandler.postDelayed(displayAd, 1);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        try {
            if (scheduler != null) {
                scheduler.shutdownNow();
                scheduler = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        try {
            if (scheduler == null) {
                scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", " Interstitial not loaded");
                                }
                                loadAd();
                            }
                        });
                    }
                }, 30, 30, TimeUnit.SECONDS);


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {


    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        try {
//            if (scheduler != null) {
//                scheduler.shutdownNow();
//                scheduler = null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
