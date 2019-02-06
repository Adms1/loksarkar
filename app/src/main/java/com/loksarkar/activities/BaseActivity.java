package com.loksarkar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.base.BaseApp;
import com.loksarkar.adapters.ExpandableMenuAdapter;
import com.loksarkar.constants.WebViewURLS;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.localeutils.utils.ActivityRecreationHelper;
import com.loksarkar.models.ChildListModel;
import com.loksarkar.models.ExpandabelModel;
import com.loksarkar.models.RefferalUserListModel;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.InstallReferrerHelper;
import com.loksarkar.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.media.MediaFormat.KEY_LANGUAGE;
import static com.loksarkar.utils.PrefUtils.REFERRAL_ID_KEY;

public class BaseActivity extends AppCompatActivity implements InstallReferrerHelper.InstallReferrerCallback {

    public DrawerLayout drawerLayout;
    private Toolbar toolbar;
     public  Context mContext;
    public ActionBarDrawerToggle drawerToggle;
    public NavigationView navigationView;
    private FrameLayout drawerIcon;
    private ExpandableMenuAdapter mMenuAdapter;
    private ExpandableListView expandableList;
    private List<ExpandabelModel> listDataHeader;
    HashMap<ExpandabelModel, List<ChildListModel>> listDataChild;
    private static SharedPreferences localeSharedPrefs;
    private static final String SP_LOCALE = "LocaleChanger.LocalePersistence";
    private LinearLayout viewLogout;
    private LinearLayout viewLogin;
    private String language = "";
    private ImageView mImageViewLogo;
    //private BranchUniversalObject branchUniversalObject;
    private Bundle activityArguments;
    private TextView textRewards;
    private InterstitialAd mInterstitialAd;
    private Handler mHandler;
    private Runnable displayAd;
    private ScheduledExecutorService scheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        setContentView(R.layout.activity_base);
        drawerIcon = (FrameLayout)findViewById(R.id.frm_drawer_icon);
        localeSharedPrefs = getSharedPreferences(SP_LOCALE, Context.MODE_PRIVATE);
        language = localeSharedPrefs.getString(KEY_LANGUAGE, "");
        navigationView = (NavigationView)findViewById(R.id.navigation);
        //branchUniversalObject = new BranchUniversalObject();
        //PrefUtils.getInstance(BaseActivity.this).setUserLogout();
        MobileAds.initialize(this, "ca-app-pub-7793510975061206~8275052408");

        try {
            InstallReferrerHelper.fetchInstallReferrer(this,this);
        }catch (Exception ex){
            ex.printStackTrace();
        }



        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7793510975061206/3828788355");
        //testing unit id
         //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
         mHandler = new Handler(Looper.getMainLooper());

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                           // mInterstitialAd.show();
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                        }
                        loadAd();
                    }
                });
            }
        }, 30, 30, TimeUnit.SECONDS);


        displayAd = new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {

                        } else {
                            // Toast.makeText(DashBoardActivity.this,"Ad did not load",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        displayInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("AdError",String.valueOf(errorCode));
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                // loadAd();
            }
        });




    }




    private void loadAd() {
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("918DB44296FAF473DC4180E450381834")
//                .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(request);



    }

    //Call displayInterstitial() once you are ready to display the ad.
    public void displayInterstitial() {
        mHandler.postDelayed(displayAd, 1);
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.topPanel);
        setSupportActionBar(toolbar);
        viewLogout = (LinearLayout)findViewById(R.id.LL_logout);
        viewLogin = (LinearLayout)findViewById(R.id.LL_login);
        mImageViewLogo = (ImageView)findViewById(R.id.iv_app_logo);

        try {
            if (language.equalsIgnoreCase("gu")) {
                language = "gujarati";
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_round);
            } else if (language.equalsIgnoreCase("hi")) {
                language = "hindi";
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_hindi_round);
            } else if (language.equalsIgnoreCase("en")) {
                language = "english";
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_eng_round);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }



        if(PrefUtils.getInstance(BaseActivity.this).getUserLogin()){
            viewLogout.setVisibility(View.VISIBLE);
            viewLogin.setVisibility(View.GONE);
        }else{
            viewLogin.setVisibility(View.VISIBLE);
            viewLogout.setVisibility(View.GONE);
        }

        viewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BaseActivity.this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.logout_confrim_msg))
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int whichButton) {

                                PrefUtils.getInstance(BaseActivity.this).setUserLogout();
                                PrefUtils.getInstance(BaseActivity.this).clear();
                                //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));

                                //logout user from branch sdk.
                                //Branch.getInstance(getApplicationContext()).logout();

                                Intent intentHome = new Intent(BaseActivity.this,DashBoardActivity.class);
                                startActivity(intentHome);
                                finishAffinity();

                            }})

                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                startActivity(new Intent(BaseActivity.this,SigninActivity.class));
            }
        });
    }

    @Nullable
    public Toolbar getBaseToolbar() {
        return toolbar;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if(scheduler != null){
                scheduler.shutdownNow();
                scheduler = null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
    /**
     * @return the layout id associated to the layout used in the activity.
     */



    public Bundle getActivityArguments() {
        return activityArguments;
    }
    protected void setMenuClick(final boolean isClick){

        drawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isClick) {
                        if (drawerLayout.isDrawerOpen(Gravity.START)) {
                            drawerLayout.closeDrawer(Gravity.START);
                        } else {
                            drawerLayout.openDrawer(Gravity.START);
                        }
                    }else{

                    }
                }
            });

    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//

        return super.onOptionsItemSelected(item);

    }


      @Override
      public void onBackPressed(){
        super.onBackPressed();
      }



    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.navigation);



        View headerView  =  (View)navigationView.getHeaderView(0);

        TextView headerUserName = (TextView)headerView.findViewById(R.id.tv_header_username);
        TextView referralCode = (TextView)headerView.findViewById(R.id.tv_header_referal_code);
        TextView header = (TextView)headerView.findViewById(R.id.tv_header_name);
        RelativeLayout headerRewards = (RelativeLayout)headerView.findViewById(R.id.LL_rewards);
        textRewards = headerView.findViewById(R.id.tv_reward_points);
        try {
            if (PrefUtils.getInstance(BaseActivity.this).getUserLogin()) {

                headerUserName.setText(PrefUtils.getInstance(BaseActivity.this).getStringValue(PrefUtils.USERNAME_KEY, ""));
                referralCode.setText(PrefUtils.getInstance(BaseActivity.this).getStringValue(REFERRAL_ID_KEY, "") +  "  (Referral code)");
                headerUserName.setVisibility(View.VISIBLE);
                referralCode.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);

                String totalInstalltion = String.valueOf(PrefUtils.getInstance(this).getIntValue(PrefUtils.REWARD_POINTS, 0));

                textRewards.setText(totalInstalltion);
                headerRewards.setVisibility(View.VISIBLE);

                getUserPoints(PrefUtils.getInstance(BaseActivity.this).getStringValue(REFERRAL_ID_KEY, ""));


            }else{
                headerRewards.setVisibility(View.GONE);
                headerUserName.setVisibility(View.GONE);
                referralCode.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new ExpandableMenuAdapter(this,listDataHeader,listDataChild,expandableList);

        expandableList.setAdapter(mMenuAdapter);


        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
               // ClipData.Item item = (ClipData.Item) mMenuAdapter.getChild(groupPosition,childPosition);
                onHandleChildClick(childPosition);
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
               // ClipData.Item item = (ClipData.Item) mMenuAdapter.getGroup(groupPosition);

                onHandleGroupClick(groupPosition);
                return false;
            }
        });

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    private void onHandleGroupClick(int pos){

        if(language.equalsIgnoreCase("gu")){
            language = "gujarati";
        }else if(language.equalsIgnoreCase("hi")){
            language = "hindi";
        }else if(language.equalsIgnoreCase("en")){
            language = "english";

        }

        switch (pos){
            case 0:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(this,DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case 1:
                drawerLayout.closeDrawers();
                Intent intent1 = new Intent(this,WebviewActivty.class);
                intent1.putExtra("url", WebViewURLS.ABOUT_US);
               // intent1.putExtra("lang","none");
                startActivity(intent1);
                break;
//            case 2:
//                drawerLayout.closeDrawers();
//                Intent intent2 = new Intent(this,WebviewActivty.class);
//
//                intent2.putExtra("url",WebViewURLS.REGISTRATION);
//                startActivity(intent2);
//                break;

            case 2:
                drawerLayout.closeDrawers();
                if(PrefUtils.getInstance(BaseActivity.this).getUserLogin()){
                    Intent intent3 = new Intent(this,ComplaintsList.class);
                    startActivity(intent3);
                }else{
                    Intent intent3 = new Intent(this,SigninActivity.class);
                    startActivity(intent3);
                }
                break;

            case 3:
                drawerLayout.closeDrawers();
                Intent intent3 = new Intent(this,WebviewActivty.class);
                intent3.putExtra("url",WebViewURLS.TRACK_APPLICATION);
                intent3.putExtra("lang","none");
                startActivity(intent3);
                break;


//
//            case 4:
//                drawerLayout.closeDrawers();
//                new AlertDialog.Builder(this)
//                        .setTitle(getString(R.string.app_name))
//                        .setMessage("Do you really want to logout?")
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                PrefUtils.getInstance(BaseActivity.this).setUserLogout();
//                                PrefUtils.getInstance(BaseActivity.this).clear();
//                                Intent intentHome = new Intent(BaseActivity.this,SigninActivity.class);
//                                startActivity(intentHome);
//                                finishAffinity();
//
//                            }})
//
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//                break;
            case 5:
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                }

                Intent intentReferralCode = new Intent(BaseActivity.this,ReferralActivity.class);
                startActivity(intentReferralCode);

                break;

            case 6:
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                }

                Intent frgmentContainer = new Intent(BaseActivity.this,FragmentContainerActivity.class);
                startActivity(frgmentContainer);

                break;
        }


    }




    private void onHandleChildClick(int pos){

        switch (pos){
            case 0:
                //for gujarati.
                if(drawerLayout.isDrawerOpen(Gravity.START)){
                    drawerLayout.closeDrawer(Gravity.START);
                }
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_round);
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));
                restartApp();
                //ActivityRecreationHelper.recreate(BaseActivity.this, true);
                break;
            case 1:
                //for hindi.
                if(drawerLayout.isDrawerOpen(Gravity.START)){
                    drawerLayout.closeDrawer(Gravity.START);
                }
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_hindi_round);
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(1));
                restartApp();
                //ActivityRecreationHelper.recreate(BaseActivity.this, true);
                break;
            case 2:
                //for english.
                if(drawerLayout.isDrawerOpen(Gravity.START)){
                    drawerLayout.closeDrawer(Gravity.START);
                }
                mImageViewLogo.setImageResource(R.mipmap.ic_launcher_eng_round);
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                restartApp();
                //ActivityRecreationHelper.recreate(BaseActivity.this, true);
                break;
        }

    }


    private void restartApp(){
        Intent dashboardintent = new Intent(BaseActivity.this,DashBoardActivity.class);
        startActivity(dashboardintent);
        finishAffinity();
    }


    @Override
    protected void onResume(){
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandabelModel>();
        listDataChild = new HashMap<ExpandabelModel, List<ChildListModel>>();

        ExpandabelModel expandabelModel = new ExpandabelModel();
        expandabelModel.setName(getString(R.string.home));
        expandabelModel.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_home));

        ExpandabelModel expandabelModel1 = new ExpandabelModel();
        expandabelModel1.setName(getString(R.string.about));
        expandabelModel1.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_about_us));

        ExpandabelModel expandabelModel4 = new ExpandabelModel();
        expandabelModel4.setName(getString(R.string.my_complaints_requests));
        expandabelModel4.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_complaints));

        ExpandabelModel expandabelModel5 = new ExpandabelModel();
        expandabelModel5.setName(getString(R.string.track_app));
        expandabelModel5.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_track));

        ExpandabelModel expandabelModel6 = new ExpandabelModel();
        expandabelModel6.setName(getString(R.string.choose_language));
        expandabelModel6.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_language));

        ExpandabelModel expandabelModel7 = new ExpandabelModel();
        expandabelModel7.setName(getString(R.string.referral));
        expandabelModel7.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_share));

        ExpandabelModel expandabelModel8 = new ExpandabelModel();
        expandabelModel8.setName(getString(R.string.top_referral_users));
        expandabelModel8.setDrawable_id(ContextCompat.getDrawable(BaseActivity.this,R.drawable.ic_cup));

        listDataHeader.add(expandabelModel);
        listDataHeader.add(expandabelModel1);
       // listDataHeader.add(expandabelModel3);
        listDataHeader.add(expandabelModel4);
        listDataHeader.add(expandabelModel5);
        listDataHeader.add(expandabelModel6);
        listDataHeader.add(expandabelModel7);
        listDataHeader.add(expandabelModel8);


        String language = localeSharedPrefs.getString(KEY_LANGUAGE, "");


        List<ChildListModel> heading1= new ArrayList<ChildListModel>();

        ChildListModel childListModel = new ChildListModel();
        childListModel.setMenuName(getString(R.string.gujarati));
        childListModel.setSelected(false);

        ChildListModel childListModel1 = new ChildListModel();
        childListModel1.setMenuName(getString(R.string.hindi));
        childListModel1.setSelected(false);

        ChildListModel childListModel2 = new ChildListModel();
        childListModel2.setMenuName(getString(R.string.eng));
        childListModel2.setSelected(false);

        if(language.equalsIgnoreCase("gu")){
            childListModel.setSelected(true);
        }else if(language.equalsIgnoreCase("hi")){
            childListModel1.setSelected(true);
        }else if(language.equalsIgnoreCase("en")){
            childListModel2.setSelected(true);
        }

        heading1.add(childListModel);
        heading1.add(childListModel1);
        heading1.add(childListModel2);

        listDataChild.put(listDataHeader.get(4),heading1);

    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //   getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onReceived(String installReferrer) {
        try {

            if(installReferrer != null) {

                if (!PrefUtils.getInstance(this).isFirstTime()) {

                    Log.d("referral_code",installReferrer);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
                    String token = pref.getString("regId", "");

                    if (token != null && !TextUtils.isEmpty(token)) {
                        sendDeviceIdWithToken(token,installReferrer);
                    }
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailed() {

    }




    private void sendDeviceIdWithToken(final String token,String referralCode) {
        // sending fcm token to server

        if (!AppUtils.isNetworkConnected(this)) {
            AppUtils.ping(getApplicationContext(),getResources().getString(R.string.internet_error));
            return;
        }

        ApiHandler.getApiService().registerDeviceNew(setDeviceDetail(token,referralCode),new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel registrationModel, Response response) {
                AppUtils.dismissDialog();
                if (registrationModel == null) {
                    AppUtils.ping(getApplicationContext(),getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess() == null) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("false")) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("True")) {
                    PrefUtils.getInstance(BaseActivity.this).setIsFirstTime(true);
                    return;
                }

            }
            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setDeviceDetail(String token, String referralCode) {
        Map<String, String> map = new HashMap<>();
        map.put("DeviceToken",token);
        map.put("DeviceID",AppUtils.getDeviceId(this));

        if(referralCode.equals("utm_source=google-play&utm_medium=organic") || referralCode.contains("utm_source")){
            map.put("ReferralCode","");
        }else{
            map.put("ReferralCode",referralCode);

        }

        return map;
    }


    private void getUserPoints(String referralCode) {
        // sending fcm token to server

        if (!AppUtils.isNetworkConnected(this)) {
            AppUtils.ping(getApplicationContext(),getResources().getString(R.string.internet_error));
            return;
        }

        ApiHandler.getApiService().getUserPoint(setUserPointsParam(referralCode),new retrofit.Callback<RefferalUserListModel>() {
            @Override
            public void success(RefferalUserListModel registrationModel, Response response) {
                AppUtils.dismissDialog();
                if (registrationModel == null) {
                    AppUtils.ping(getApplicationContext(),getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess() == null) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("false")) {
                    AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
                    return;
                }

                if (registrationModel.getSuccess().equalsIgnoreCase("True")) {
                    textRewards.setText(String.valueOf(registrationModel.getFinalArray1().get(0).getTotalInstalltion()));
                    return;
                }

            }
            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                AppUtils.ping(getApplicationContext(), getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setUserPointsParam(String referralCode) {
        Map<String, String> map = new HashMap<>();
        map.put("ReferralID",referralCode);
        return map;
    }



}
