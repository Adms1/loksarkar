package com.loksarkar.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.utils.AppUtils;

public class SocialMediaActivity extends BaseActivity {


    private String fb_url = "",twitter_url = "",whatsapp_num = "+918347476757",youtube_url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        fb_url = "https://www.facebook.com/LokSarkarOfficial/";
        twitter_url = "https://twitter.com/LokSarkarGuj_";
        youtube_url = "https://www.youtube.com/user/indiacongress";

        findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fb_url.startsWith("http://") && !fb_url.startsWith("https://")){
                    fb_url = "http://" + fb_url;

                  }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb_url));
                startActivity(browserIntent);

            }
        });

        findViewById(R.id.view_click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!twitter_url.startsWith("http://") && !twitter_url.startsWith("https://")){
                    twitter_url = "http://" + twitter_url;

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter_url));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.view_click3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!youtube_url.startsWith("http://") && !youtube_url.startsWith("https://")){
                    youtube_url = "http://" + youtube_url;

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url));
                startActivity(browserIntent);
            }
        });
        findViewById(R.id.view_click4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if(installed) {
                        openWhatsAppChat(whatsapp_num);
                    } else {
                        AppUtils.ping(SocialMediaActivity.this,"App is not currently installed on your phone");
                    }
                }catch (Exception ex){

                }
            }
        });


    }

    private void openWhatsAppChat(String phonenumber){
        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
        sendIntent.setType("text");
        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(phonenumber)+"@s.whatsapp.net"); //number without '+' prefix and without '0' after country code
        sendIntent.putExtra(Intent.EXTRA_TEXT,"hii,i want join loksarkar");
        startActivity(sendIntent);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
