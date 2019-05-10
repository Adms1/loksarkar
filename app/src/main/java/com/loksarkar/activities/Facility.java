package com.loksarkar.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;
import com.loksarkar.R;

public class Facility extends BaseActivity {

    private AdView mAdView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

//        mAdView = findViewById(R.id.adView);
//        mAdView.loadAd(PrefUtils.showads());
//        mAdView.setVisibility(View.VISIBLE);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable(){
//
//            @Override
//            public void run() {
//                mAdView.setVisibility(View.VISIBLE);
//
//            }
//        },3000);

        findViewById(R.id.iv_airbooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.iv_railbooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.iv_busbooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.iv_hotelbooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.iv_Other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog(){
        dialog = new Dialog(this,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comming_soon);

        LinearLayout llOk = dialog.findViewById(R.id.LL_ok);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }
}
