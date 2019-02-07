package com.loksarkar.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.loksarkar.R;
import com.loksarkar.constants.AppConstants;

public class MediaBulletinActivity extends BaseActivity {


    private String category_socialMedia = "Social Media";
    private String category_mediaCoverage = "Media Coverage";
    private String category_video = "Video";
    private String category_news = "News";

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_bulletin2);

        final Intent intentMediaList = new Intent(MediaBulletinActivity.this, MediaBulletinListActivity.class);

        findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent intentMediaRegitration = new Intent(MediaBulletinActivity.this, SocialMediaActivity.class);
//                    startActivity(intentMediaRegitration);
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
                AppConstants.typeId = category_socialMedia;
                intentMediaList.putExtra("category_type", category_socialMedia);
                startActivity(intentMediaList);
            }
        });

        findViewById(R.id.view_click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstants.typeId = category_mediaCoverage;
                intentMediaList.putExtra("category_type", category_mediaCoverage);
                startActivity(intentMediaList);

                // showDialog();
            }
        });

        findViewById(R.id.view_click3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // showDialog();
                AppConstants.typeId = category_video;

                intentMediaList.putExtra("category_type", category_video);
                startActivity(intentMediaList);
            }
        });
        findViewById(R.id.view_click4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                Intent intentMediaRegitration = new Intent(MediaBulletinActivity.this, MediaRegistration.class);
                startActivity(intentMediaRegitration);
            }
        });

        findViewById(R.id.view_click5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                AppConstants.typeId = category_news;
                intentMediaList.putExtra("category_type", category_news);
                startActivity(intentMediaList);
            }
        });


    }

    private void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comming_soon);

        LinearLayout llOk = (LinearLayout) dialog.findViewById(R.id.LL_ok);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }
}
