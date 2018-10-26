package com.loksarkar.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.loksarkar.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private String typeId = "";
    private boolean isLanguageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            typeId = getIntent().getStringExtra("blog_id");
        }catch (Exception ex){
            typeId = "";
            ex.getLocalizedMessage();
        }


        try {
            if (!PrefUtils.getInstance(SplashActivity.this).isLanguageSelected()) {
                Intent intent = new Intent(this, LocalizationAcitivity.class);
                intent.putExtra("blog_id", typeId);
                startActivity(intent);
                finish();

            }else{
                Intent intent = new Intent(this, DashBoardActivity.class);
                intent.putExtra("blog_id", typeId);
                startActivity(intent);
                finish();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
       }

    }

