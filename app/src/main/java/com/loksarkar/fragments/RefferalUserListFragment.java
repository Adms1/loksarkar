package com.loksarkar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.activities.DashBoardActivity;
import com.loksarkar.activities.SigninActivity;
import com.loksarkar.adapters.RefferalUserListAdapter;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.api.AppConfiguration;
import com.loksarkar.models.LoginModel;
import com.loksarkar.models.RefferalUserListModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PermissionUtils;
import com.loksarkar.utils.PrefUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RefferalUserListFragment extends Fragment {

    private View rootView;
    private RecyclerView rvUserPointList,rvComplaintUserList;
    private RefferalUserListAdapter refferalUserListAdapter;
    private List<RefferalUserListModel.FinalArray>  dataList;
    private RotateLoaderDialog rotateLoaderDialog;
    private Context mContext;
    private AppCompatTextView tvNoRecords;

    public RefferalUserListFragment(){

    }

    public void onAttach(Context context){
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_top_userlist,container,false);
        rvUserPointList = (RecyclerView)rootView.findViewById(R.id.rv_users_list);
        rvComplaintUserList = (RecyclerView)rootView.findViewById(R.id.rv_complaint_users_list);

        tvNoRecords = (AppCompatTextView) rootView.findViewById(R.id.tv_no_records);
        mContext = getActivity().getApplicationContext();
        rotateLoaderDialog = new RotateLoaderDialog(getActivity());
        //Set Thread Policy


        callRefferalUserListApi();



        return rootView;
    }



    private void callRefferalUserListApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(mContext,getResources().getString(R.string.internet_error),getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().topRefferalUser(setParams(),new retrofit.Callback<RefferalUserListModel>() {
            @Override
            public void success(RefferalUserListModel refferalUserListModel, Response response) {
                AppUtils.dismissDialog();
                if (refferalUserListModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext,getString(R.string.something_wrong));

                    tvNoRecords.setVisibility(View.VISIBLE);
                    return;
                }
                if (refferalUserListModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext,getString(R.string.something_wrong));

                    tvNoRecords.setVisibility(View.VISIBLE);

                    return;
                }
                if (refferalUserListModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    rotateLoaderDialog.dismissLoader();


                    tvNoRecords.setVisibility(View.VISIBLE);
                    AppUtils.notify(mContext,"Error","No Records Found",R.color.error_color,ContextCompat.getDrawable(mContext,R.drawable.ic_error));

                    return;
                }
                if (refferalUserListModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    if(refferalUserListModel.getFinalArray() != null && refferalUserListModel.getFinalArray().size() > 0){

                        tvNoRecords.setVisibility(View.GONE);

                        refferalUserListAdapter = new RefferalUserListAdapter(mContext,refferalUserListModel.getFinalArray().get(0).getRegisterDevice(),1);
                        rvUserPointList.setLayoutManager(new LinearLayoutManager(mContext));
                        rvUserPointList.setAdapter(refferalUserListAdapter);


                        refferalUserListAdapter = new RefferalUserListAdapter(mContext,refferalUserListModel.getFinalArray().get(0).getRegisterComplaint(),2);
                        rvComplaintUserList.setLayoutManager(new LinearLayoutManager(mContext));
                        rvComplaintUserList.setAdapter(refferalUserListAdapter);

                    }
                }

            }
            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                tvNoRecords.setVisibility(View.VISIBLE);
                rotateLoaderDialog.dismissLoader();
                AppUtils.ping(mContext,error.getMessage());
            }
        });

    }

    private Map<String, String> setParams() {
        Map<String, String> map = new HashMap<>();
        return map;
    }



}
