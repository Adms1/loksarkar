package com.loksarkar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loksarkar.R;
import com.loksarkar.adapters.DashboardMenuAdapter;
import com.loksarkar.api.DeviceVersionAsyncTask;
import com.loksarkar.models.DashBoardModel;
import com.loksarkar.models.DeviceVersionModel;
import com.loksarkar.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.MobileAds;

public class DashBoardActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private DashboardMenuAdapter dashboardMenuAdapter;
    private List<DashBoardModel> dataList = new ArrayList<DashBoardModel>();
    private static long back_pressed;
    private String[] menuNames;
    private int [] allColors;
    private String typeId = "",title = "";
    private int[] mMenuIcons = new int[]{R.drawable.complaint,R.drawable.registration,R.drawable.useful_information,R.drawable.facility,R.drawable.media_bulletin,R.drawable.people_voice,R.drawable.suggestion,R.drawable.join_congress};
    private String currentVersion;
    private DeviceVersionAsyncTask deviceVersionAsyncTask = null;
    private DeviceVersionModel deviceVersionModel;
    private boolean isVersionCodeUpdated = false;
    //    private InterstitialAd mInterstitialAd;
    private Handler mHandler;
    private Runnable displayAd;     // Code to execute to perform this operation
//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        setMenuClick(true);
        recyclerView = findViewById(R.id.rv_menu_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        mAdView = findViewById(R.id.adView);

        Handler handler = new Handler();

//        mAdView.loadAd(PrefUtils.showads());
//        handler.postDelayed(new Runnable(){
//
//            @Override
//            public void run() {
//                mAdView.setVisibility(View.VISIBLE);
//
//            }
//        },3000);

        try {
            typeId = getIntent().getStringExtra("blog_id");
            title = getIntent().getStringExtra("title");

            if (typeId != null && !TextUtils.isEmpty(typeId)) {

                if (typeId.equalsIgnoreCase("News")) {
                    try {
//                        title = title.substring(0,title.length() - 1);
//                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(title));
//                        intent.setData();
//                        String title = "Open Via";
//                        Intent chooser = Intent.createChooser(intent,title);
//                        startActivity(chooser);

                        String[] contentTitle = title.split(Pattern.quote("|"));

                        String description = contentTitle[0];
                        String link = contentTitle[1];

                        Intent intentDashboard = new Intent(DashBoardActivity.this, WebviewActivty.class);
                        intentDashboard.putExtra("url", link);
                        intentDashboard.putExtra("lang", "none");
                        intentDashboard.putExtra("size", "small");
                        startActivity(intentDashboard);

//
//                        Intent intentDashboard1 = new Intent(DashBoardActivity.this,ChromeTabActivity.class);
//                        intentDashboard1.putExtra("url", link);
//                        intentDashboard1.putExtra("lang","none");
//                        startActivity(intentDashboard1);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {

                    Intent intentDashboard = new Intent(DashBoardActivity.this, MediaDetailActivity.class);
                    intentDashboard.putExtra("blog_id", typeId);
                    startActivity(intentDashboard);
                }

            }
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }

        try {
            currentVersion = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));

        try {
            menuNames = getResources().getStringArray(R.array.dashboard_menu);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setData();


    }

    public void onResume(){
        super.onResume();
        appUpdateTask();
    }

    private void setData() {

        if (menuNames != null) {

            allColors = new int[menuNames.length];

            for (int count = 0; count < menuNames.length; count++) {

                DashBoardModel dashBoardModel = new DashBoardModel();
                dashBoardModel.setMenuIcon(mMenuIcons[count]);
                dashBoardModel.setMenuName(menuNames[count]);

                dataList.add(dashBoardModel);

            }

            for(int i=0; i<menuNames.length; i++) {
                //Getting the color resource id
                TypedArray ta = getResources().obtainTypedArray(R.array.dashboard_item_colors);

                int colorToUse = ta.getResourceId(i, 0);
                allColors[i] = colorToUse;
                ta.recycle();
            }

            dashboardMenuAdapter = new DashboardMenuAdapter(this, dataList, allColors);

            recyclerView.setAdapter(dashboardMenuAdapter);
        }

    }


    public void appUpdateTask(){
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        getVersionUpdateInfo();


    }

    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
                super.onBackPressed();
            }
            else{
                Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    public void getVersionUpdateInfo() {
        if (AppUtils.isNetworkConnected(mContext)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<String, String>();
//                        params.put("UserID", Utility.getPref(mContext, "studid"));
//                        params.put("VersionID", String.valueOf(versionCode));//String.valueOf(versionCode)
//                        params.put("UserType", "Student");
                        //=========new ========
                        params.put("VersionID",String.valueOf(currentVersion));//String.valueOf(versionCode)
                        params.put("DeviceType","Android");
                        deviceVersionAsyncTask = new DeviceVersionAsyncTask(params);
                        deviceVersionModel = deviceVersionAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (deviceVersionModel!=null) {
                                    if (deviceVersionModel.getSuccess().equalsIgnoreCase("True")) {
                                        isVersionCodeUpdated = true;

                                    } else {
                                        isVersionCodeUpdated = false;
                                        Log.d("false", "" + isVersionCodeUpdated);
                                        new android.app.AlertDialog.Builder(new android.view.ContextThemeWrapper(DashBoardActivity.this, R.style.AppCompatAlertDialogStyle))
                                                .setCancelable(false)
                                                .setTitle("Update App")
                                                .setMessage("Please update to a new version of the app.")
                                                .setIcon(R.mipmap.ic_launcher_round)
                                                .setPositiveButton("Update",new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName()));
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        mContext.startActivity(i);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        AppUtils.ping(mContext, "You wont be able to use other funcationality without updating to a newer version");
                                                    }
                                                })

                                                .show();

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
            AppUtils.ping(mContext, "Network not available");

        }
    }





}
