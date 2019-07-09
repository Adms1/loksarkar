package com.loksarkar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.api.GetReferralInfoTask;
import com.loksarkar.models.ReferralModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PrefUtils;

import java.util.HashMap;

public class ReferralActivity extends AppCompatActivity {

    private ReferralModel referralModel;
    private GetReferralInfoTask getReferralInfoTask;
    private AppCompatTextView mTvReferralDesc;
    private AppCompatButton mBtnReferralInvite;
    private RotateLoaderDialog rotateLoaderDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrer);

//        getSupportActionBar().setTitle("REFFERALS");
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rotateLoaderDialog = new RotateLoaderDialog(this);

        mTvReferralDesc = (AppCompatTextView) findViewById(R.id.tv_referal_content);
        mBtnReferralInvite = (AppCompatButton) findViewById(R.id.btn_invite);

        try {
            getRefferrarinfo(ReferralActivity.this);
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }


        findViewById(R.id.backView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBtnReferralInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(PrefUtils.getInstance(ReferralActivity.this).getUserLogin()) {
                    String loksevakCode = PrefUtils.getInstance(ReferralActivity.this).getStringValue(PrefUtils.REFERRAL_ID_KEY, "");

//                    LinkProperties linkProperties = new LinkProperties()
//                            .addTag("Tag1")
//                            .setChannel("Loksarkar_Channel")
//                            .setFeature("loksarkar_sharing_feature")
//                            .addControlParameter("$android_deeplink_path", "custom/path/*")
//                           // .addControlParameter("$ios_url", "http://example.com/ios")
//                            .setDuration(100);
                    AppUtils.sendReferral(ReferralActivity.this, loksevakCode, getString(R.string.share_app));

                } else {
                    Intent intentLogin = new Intent(ReferralActivity.this,SigninActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }



    public void getRefferrarinfo(final Context context) {
        if (AppUtils.isNetworkConnected(context)) {

            rotateLoaderDialog.showLoader();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<String, String>();


                        getReferralInfoTask = new GetReferralInfoTask();
                        referralModel = getReferralInfoTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rotateLoaderDialog.dismissLoader();

                                if (referralModel!=null) {
                                    if (referralModel.getSuccess().equalsIgnoreCase("True")) {
                                        mTvReferralDesc.setText(referralModel.getFinalArray().get(0).getDescription());

                                    } else {
                                        mTvReferralDesc.setText("");
                                    }
                                }else{

                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            AppUtils.ping(context, "Network not available");

        }
    }


}
