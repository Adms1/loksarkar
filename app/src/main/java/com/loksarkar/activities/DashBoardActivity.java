package com.loksarkar.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.loksarkar.adapters.DashboardMenuAdapter;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.base.BaseApp;
import com.loksarkar.fragments.LanguageSelection;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.models.DashBoardModel;
import com.loksarkar.R;
import com.loksarkar.models.LoginModel;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogFragment;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PrefUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class DashBoardActivity extends BaseActivity   {

    private RecyclerView recyclerView;
    private DashboardMenuAdapter dashboardMenuAdapter;
    private List<DashBoardModel> dataList = new ArrayList<DashBoardModel>();
    private static long back_pressed;
    private String[] menuNames;
    private int [] allColors;
    private String typeId = "";
    private int[] mMenuIcons = new int[]{R.drawable.complaint,R.drawable.registration,R.drawable.useful_information,R.drawable.facility,R.drawable.media_bulletin,R.drawable.people_voice,R.drawable.suggestion,R.drawable.join_congress};
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_dash_board);
        setMenuClick(true);
        recyclerView = (RecyclerView)findViewById(R.id.rv_menu_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        try{
            typeId = getIntent().getStringExtra("blog_id");

            if(typeId != null && !TextUtils.isEmpty(typeId)){
                //redirect to notification detail screen.
                Intent intentDashboard = new Intent(DashBoardActivity.this,MediaDetailActivity.class);
                intentDashboard.putExtra("blog_id",typeId);
                startActivity(intentDashboard);

            }

        }catch (Exception ex){
            ex.getLocalizedMessage();
        }


       // LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));

        try {
            menuNames = getResources().getStringArray(R.array.dashboard_menu);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        setData();

        checkRuntimePermission();

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

    private void checkRuntimePermission() {

        if (ActivityCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashBoardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }
    }


//
//
//        DashBoardModel dashBoardModel1 = new DashBoardModel();
//        dashBoardModel1.setMenuIcon(R.drawable.registration);
//        dashBoardModel1.setMenuName("Registration");
//
//        DashBoardModel dashBoardModel2 = new DashBoardModel();
//        dashBoardModel2.setMenuIcon(R.drawable.useful_information);
//        dashBoardModel2.setMenuName("Useful Information");
//
//        DashBoardModel dashBoardModel3 = new DashBoardModel();
//        dashBoardModel3.setMenuIcon(R.drawable.facility);
//        dashBoardModel3.setMenuName("Facilities");
//
//        DashBoardModel dashBoardModel4 = new DashBoardModel();
//        dashBoardModel4.setMenuIcon(R.drawable.media_bulletin);
//        dashBoardModel4.setMenuName("Media Builtin");
//
//
//        DashBoardModel dashBoardModel5 = new DashBoardModel();
//        dashBoardModel5.setMenuIcon(R.drawable.people_voice);
//        dashBoardModel5.setMenuName("Voice of People");
//
//        DashBoardModel dashBoardModel6 = new DashBoardModel();
//        dashBoardModel6.setMenuIcon(R.drawable.suggestion);
//        dashBoardModel6.setMenuName("Suggestion");
//
//        DashBoardModel dashBoardModel7 = new DashBoardModel();
//        dashBoardModel7.setMenuIcon(R.drawable.join_congress_1);
//        dashBoardModel7.setMenuName("More");


//        dataList.add(dashBoardModel1);
//        dataList.add(dashBoardModel2);
//        dataList.add(dashBoardModel3);
//        dataList.add(dashBoardModel4);
//        dataList.add(dashBoardModel5);
//        dataList.add(dashBoardModel6);
//        dataList.add(dashBoardModel7);
//       // dataList.add(dashBoardModel8);




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






}
