package com.loksarkar.receivers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.InstallReferrerHelper;
import com.loksarkar.utils.PrefUtils;

import java.util.Date;
import java.util.logging.Logger;

public class InstallReferrerReceiver extends BroadcastReceiver {

    public static final String ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA";
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String KEY_REFERRER = "referrer";
    private static final String REFERRER_DATE = "REFERRER_DATE";
    private static final String REFERRER_DATA = "REFERRER_DATA";

    @Override
    public final void onReceive(final Context context, final Intent intent) {
        setInstallReferrer(context, intent);
    }

    static void setInstallReferrer(Context context, Intent intent) {
        if (context == null) {
            Log.e("TAG","ReferrerReceiver Context can not be null");
            return;
        }
        if (intent == null) {
            Log.e("TAG","ReferrerReceiver intent can not be null");
            return;
        }
        if (ACTION_INSTALL_REFERRER.equals(intent.getAction())) {
            String installReferrer = intent.getStringExtra(KEY_REFERRER);
            InstallReferrerHelper.setInstallReferrer(context, installReferrer);
            Log.i("REFERRER", "Referer is: " + installReferrer);
        }
    }


}
