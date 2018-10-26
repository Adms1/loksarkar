package com.loksarkar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.listener.OnAlertNotificationClick;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogFragment;
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.Nullable;

public class DialogUtils {


    private static Dialog dialog;


    public static void showFullScreenDialog(Context context, String title, Class<? extends Fragment> contentClass,@Nullable  Bundle argumentsBundle) {
        new FullScreenDialogFragment.Builder(context)
                .setTitle(title)
                .setContent(contentClass,argumentsBundle)
                .build();
    }

    public static void showMessageDialog(Context context, @DrawableRes int id, @Nullable String title, @Nullable String message, final OnAlertNotificationClick onAlertNotificationClick){

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);

        AppCompatButton llOk = (AppCompatButton)dialog.findViewById(R.id.btn_ok);

        ImageView mImageView = (ImageView)dialog.findViewById(R.id.image_action);
        mImageView.setImageResource(id);

        AppCompatTextView tvTitle  = (AppCompatTextView)dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        AppCompatTextView tvmessage  = (AppCompatTextView)dialog.findViewById(R.id.tv_content);
        tvmessage.setText(message);


        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlertNotificationClick.OnAlertOkClick();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
