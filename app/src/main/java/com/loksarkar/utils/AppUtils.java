package com.loksarkar.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loksarkar.R;
import com.loksarkar.ui.RotateLoaderDialog;
import com.tapadoo.alerter.Alerter;

public class AppUtils {


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

}
