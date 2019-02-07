package com.loksarkar.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.adapters.UserListTypeAdapter;
import com.loksarkar.models.UserListModel;

import java.util.ArrayList;
import java.util.List;

public class RegistrationListActivity extends BaseActivity {

    private View view;
    private RecyclerView recyclerView;
    private UserListTypeAdapter userListTypeAdapter;
    private List<UserListModel> dataList = new ArrayList<UserListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv_users_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        try {
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setData() throws Exception {


        UserListModel dashBoardModel = new UserListModel();
        dashBoardModel.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel.setTypeOfUser(getString(R.string.prachar_smiti));

        UserListModel dashBoardModel1 = new UserListModel();
        dashBoardModel1.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel1.setTypeOfUser(getString(R.string.lok_sevak));

        UserListModel dashBoardModel2 = new UserListModel();
        dashBoardModel2.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel2.setTypeOfUser(getString(R.string.reseach_team));

        UserListModel dashBoardModel3 = new UserListModel();
        dashBoardModel3.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel3.setTypeOfUser(getString(R.string.media));

        UserListModel dashBoardModel4 = new UserListModel();
        dashBoardModel4.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel4.setTypeOfUser(getString(R.string.ngo));


        UserListModel dashBoardModel5 = new UserListModel();
        dashBoardModel5.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel5.setTypeOfUser(getString(R.string.nrg));

        UserListModel dashBoardModel6 = new UserListModel();
        dashBoardModel6.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel6.setTypeOfUser(getString(R.string.rti));

        UserListModel dashBoardModel7 = new UserListModel();
        dashBoardModel7.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel7.setTypeOfUser(getString(R.string.samajik_dharmik));


        UserListModel dashBoardModel8 = new UserListModel();
        dashBoardModel8.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel8.setTypeOfUser(getString(R.string.vyavsaik_sanstha));


        UserListModel dashBoardModel9 = new UserListModel();
        dashBoardModel9.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel9.setTypeOfUser(getString(R.string.saishnik_sanstha));


        UserListModel dashBoardModel10 = new UserListModel();
        dashBoardModel10.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel10.setTypeOfUser(getString(R.string.aodhyogik_sanstha));


        UserListModel dashBoardModel11 = new UserListModel();
        dashBoardModel11.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel11.setTypeOfUser(getString(R.string.vyavsaik_kaushly_sanstha));


        UserListModel dashBoardModel12 = new UserListModel();
        dashBoardModel12.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel12.setTypeOfUser(getString(R.string.vakil));


        UserListModel dashBoardModel13 = new UserListModel();
        dashBoardModel13.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel13.setTypeOfUser(getString(R.string.udhyogpati));

        UserListModel dashBoardModel14 = new UserListModel();
        dashBoardModel14.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel14.setTypeOfUser(getString(R.string.doctors));


        UserListModel dashBoardModel15 = new UserListModel();
        dashBoardModel15.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel15.setTypeOfUser(getString(R.string.builders));


        UserListModel dashBoardModel16 = new UserListModel();
        dashBoardModel16.setIconOfUser(R.drawable.ic_registration);
        dashBoardModel16.setTypeOfUser(getString(R.string.khedut));


        dataList.add(dashBoardModel);
        dataList.add(dashBoardModel1);
        dataList.add(dashBoardModel2);
        dataList.add(dashBoardModel3);
        dataList.add(dashBoardModel4);
        dataList.add(dashBoardModel5);
        dataList.add(dashBoardModel6);
        dataList.add(dashBoardModel7);
        dataList.add(dashBoardModel8);
        dataList.add(dashBoardModel9);
        dataList.add(dashBoardModel10);
        dataList.add(dashBoardModel11);
        dataList.add(dashBoardModel12);
        dataList.add(dashBoardModel13);
        dataList.add(dashBoardModel14);
        dataList.add(dashBoardModel15);
        dataList.add(dashBoardModel16);


        userListTypeAdapter = new UserListTypeAdapter(this, dataList);
        recyclerView.setAdapter(userListTypeAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));

    }


}
