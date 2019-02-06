package com.loksarkar.activities;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.loksarkar.R;

public class ChromeTabActivity extends BaseActivity{

    private String url = "";

    private Uri WEB_URL = Uri.parse("http://www.google.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome_tab);

        try {
            url = getIntent().getStringExtra("url");
            WEB_URL = Uri.parse(url);
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        //builder.setSe(session);
        builder.setToolbarColor(Color.BLUE);
// Application exit animation, Chrome enter animation.
        builder.setStartAnimations(this,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
// vice versa
        builder.setExitAnimations(this,android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();


        CustomTabsClient.bindCustomTabsService(this,getPackageName(),new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                // mClient is now valid.
               // mClient = client;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // mClient is no longer valid. This also invalidates sessions.
                //mClient = null;
            }
        });

// With a valid mClient.
        //mClient.warmup(0);

// With a valid mClient.
//        CustomTabsSession session = mClient.newSession(new CustomTabsCallback());
//        session.mayLaunchUrl(Uri.parse("https://www.google.com"), null, null);


        customTabsIntent.launchUrl(ChromeTabActivity.this,Uri.parse(url));

    }



}
