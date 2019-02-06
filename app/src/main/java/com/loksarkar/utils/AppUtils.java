package com.loksarkar.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loksarkar.R;
import com.loksarkar.activities.DashBoardActivity;
import com.loksarkar.listener.OnAlertNotificationClick;
import com.loksarkar.ui.RotateLoaderDialog;
import com.tapadoo.alerter.Alerter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {

    private static final String REFERRER_DATE = "REFERRER_DATE";
    private static final String REFERRER_DATA = "REFERRER_DATA";
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    private AppUtils() {
        throw new AssertionError("No Instances");
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void notify(Context context, String title, String message){
        if(!Alerter.isShowing()) {
            Alerter.create((Activity) context)
                    .setTitle(title)
                    .setText(message)
                    .enableSwipeToDismiss()
                    .setBackgroundColorRes(R.color.material_light_orange)
                    .addButton("Ok",R.style.AlertButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();
        }
        else{
            //Alerter.hide();


        }
    }

    @SuppressLint("ResourceType")
    public static void notify(Context context, String title, String message,@ColorRes int colorId, Drawable icon_drawable){
        if(!Alerter.isShowing()) {
            Alerter.create((Activity) context)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(colorId)
                    .setIcon(R.drawable.ic_error)
                    .enableSwipeToDismiss()
                    .setIcon(icon_drawable)
                    .addButton("Ok",R.style.AlertButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();
        }
        else{
            Alerter.hide();
        }
    }

    @SuppressLint("ResourceType")
    public static void notify(Context context, String title, String message, @ColorRes int colorId, Drawable icon_drawable, long milliSeconds){
        if(!Alerter.isShowing()) {
            Alerter.create((Activity) context)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(colorId)
                    .setIcon(R.drawable.ic_error)
                    .enableSwipeToDismiss()
                    .setIcon(icon_drawable)
                    .setDuration(milliSeconds)
                    .addButton("Ok",R.style.AlertButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();
        }
        else{
            Alerter.hide();
        }
    }

    @SuppressLint("ResourceType")
    public static void notify(Context context, String title, String message, @ColorRes int colorId, Drawable icon_drawable, long milliSeconds, final OnAlertNotificationClick onAlertNotificationClick){
        if(!Alerter.isShowing()) {
            Alerter.create((Activity) context)
                    .setTitle(title)
                    .setText(message)
                    .setBackgroundColorRes(colorId)
                   // .setIcon(R.drawable.ic_error)
                    .enableSwipeToDismiss()
                    .setIcon(icon_drawable)
                    .setDuration(milliSeconds)
                    .addButton("Ok",R.style.AlertButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onAlertNotificationClick.OnAlertOkClick();
                            Alerter.hide();
                        }
                    })
                    .show();
        }
        else{
            Alerter.hide();
        }
    }

    public static void ping(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static void dismissDialog(){

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getDimensionPixelSize(@NonNull final Context context, @AttrRes final int attribute) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attribute, typedValue, true);

        return context.getResources().getDimensionPixelSize(typedValue.resourceId);
    }

    public static int getColor(@NonNull final Context context, @AttrRes final int attribute) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attribute, typedValue, true);

        return ContextCompat.getColor(context, typedValue.resourceId);
    }


    public static void showMessage(@NonNull final View view, @StringRes final int message, final int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        ((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getColor(view.getContext(), R.attr.textColorInverse));

        snackbar.show();
    }

    public static void sendReferral(Context context,String refrealCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE,context.getString(R.string.share_app));
        sendIntent.putExtra(Intent.EXTRA_TEXT,getInvitationMessage(context,refrealCode));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.share_app));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,context.getResources().getText(R.string.invitation_extended_title)));
    }

    private static String getInvitationMessage(Context context,String referalCode){
        String playStoreLink = "https://play.google.com/store/apps/details?id="+context.getPackageName()+"&referrer="+referalCode;
        String betaTestingLink = "https://play.google.com/apps/testing/com.loksarkar&referrer="+referalCode;
        return playStoreLink;
    }

    public static String getDeviceId(Context context){
       String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
       return android_id == null ? "" :android_id;
    }


   public static int dp2px(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(px);
    }


    @TargetApi(21)
   public static class ShadowOutline extends ViewOutlineProvider {
        int width;
        int height;

        public ShadowOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, width, height);
        }
    }


}
