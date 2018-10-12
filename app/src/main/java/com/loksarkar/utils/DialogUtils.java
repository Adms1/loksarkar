package com.loksarkar.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogFragment;
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.Nullable;

public class DialogUtils {

    public static void showFullScreenDialog(Context context, String title, Class<? extends Fragment> contentClass,@Nullable  Bundle argumentsBundle) {
        new FullScreenDialogFragment.Builder(context)
                .setTitle(title)
                .setContent(contentClass,argumentsBundle)
                .build();
    }

}
