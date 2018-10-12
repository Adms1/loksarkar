package com.loksarkar.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loksarkar.R;
import com.loksarkar.base.BaseApp;
import com.loksarkar.fragments.LanguageSelection;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogFragment;
import com.loksarkar.utils.PrefUtils;

public class LocalizationAcitivity extends AppCompatActivity implements FullScreenDialogFragment.OnConfirmListener,
        FullScreenDialogFragment.OnDiscardListener,
        FullScreenDialogFragment.OnDiscardFromExtraActionListener {


    private RelativeLayout flowLayout;
    private TextView mTVGujarati,mTVHindi,mTVEnglish;
    private Context mContext;
    private RelativeLayout viewSkip;
    private FullScreenDialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            dialogFragment = (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) {
                dialogFragment.setOnConfirmListener(this);
                dialogFragment.setOnDiscardListener(this);
                dialogFragment.setOnDiscardFromExtraActionListener(this);
            }
        }

        try {
            if(!PrefUtils.getInstance(LocalizationAcitivity.this).isLanguageSelected()){
                final Bundle args = new Bundle();
                args.putString(LanguageSelection.EXTRA_NAME, "");


                dialogFragment = new FullScreenDialogFragment.Builder(LocalizationAcitivity.this)
                        .setTitle(R.string.choose_lang)
                        .setConfirmButton("")
                        .setOnConfirmListener(LocalizationAcitivity.this)
                        .setOnDiscardListener(LocalizationAcitivity.this)
                        .setContent(LanguageSelection.class, args)
                        .setOnDiscardFromActionListener(LocalizationAcitivity.this)
                        .build();

                dialogFragment.show(getSupportFragmentManager(),"");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }




    }
    @Override
    public void onBackPressed() {
        if (dialogFragment != null && dialogFragment.isAdded()) {
            dialogFragment.onBackPressed();
        }
    }


    @Override
    public void onConfirm(@Nullable Bundle result) {

    }

    @Override
    public void onDiscard() {

       redirectToDashBoard();
    }

    @Override
    public void onDiscardFromExtraAction(int actionId, @Nullable Bundle result) {

    }
    private void redirectToDashBoard(){
        Intent intentDashboard = new Intent(LocalizationAcitivity.this,DashBoardActivity.class);
        finishAffinity();
    }


}
