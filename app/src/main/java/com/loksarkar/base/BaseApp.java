package com.loksarkar.base;

import android.app.Application;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.multidex.MultiDex;

import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.utils.DeviceUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BaseApp extends Application {


    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("gu", "IN"),
            new Locale("hi", "IN"),
            new Locale("en", "US"));


    @Override
    public void onCreate(){
        super.onCreate();
        MultiDex.install(this);

        DeviceUtils.LogDeviceInfo(this);

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

        LocaleChanger.initialize(getApplicationContext(),SUPPORTED_LOCALES);
       /// LocaleChanger.setLocale(SUPPORTED_LOCALES.get(2));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }





}
