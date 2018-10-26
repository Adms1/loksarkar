package com.loksarkar.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.adapters.ComplaintsListAdapter;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.fragments.FilterBottomSheetFragment;
import com.loksarkar.listener.OnDragListener;
import com.loksarkar.listener.OnSwipeListener;
import com.loksarkar.models.ComplaintsModel;
import com.loksarkar.ui.bottomSheetView.BottomSheetLayout;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DateUtils;
import com.loksarkar.utils.PrefUtils;
import com.loksarkar.utils.RecyclerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class ComplaintsList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,FilterBottomSheetFragment.OnCompleteFilter{

    private RecyclerView rvComplaintlist;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String loksevakCode = "",fromDate = "",toDate ="",complaintType= "";
    protected BottomSheetLayout bottomSheetLayout;
    private ComplaintsListAdapter complaintsListAdapter;
    private Context mContext;
    private FloatingActionButton mFabSearch;
    private TextView mTvErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        mContext = this;

        rvComplaintlist = (RecyclerView)findViewById(R.id.rv_complaints_list);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mFabSearch = (FloatingActionButton)findViewById(R.id.action_search);
        mTvErrorView = (TextView)findViewById(R.id.tv_empty_view);
        swipeRefreshLayout.setOnRefreshListener(this);


        try {
            loksevakCode = PrefUtils.getInstance(ComplaintsList.this).getStringValue(PrefUtils.REFERRAL_ID_KEY,"");
          //  loksevakCode = "SRTC00006";
            fromDate = DateUtils.getCurrentDateMinusOneMonth();
            toDate = DateUtils.getCurrentDate();
            complaintType = "All";

            getComplaintList(loksevakCode,fromDate,toDate,complaintType);

        }catch (Exception ex){
            ex.getLocalizedMessage();
        }

        mFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterBottomSheetFragment().show(getSupportFragmentManager(), R.id.bottomsheet);
            }
        });

    }



    private void getComplaintList(String loksevakCode,String fromDate,String toDate,String ComplaintType) {

        if (!AppUtils.isNetworkConnected(ComplaintsList.this)) {
            AppUtils.notify(ComplaintsList.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }else{
            swipeRefreshLayout.setRefreshing(true);
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getComplaintsList(setParams(loksevakCode,fromDate,toDate,ComplaintType),new retrofit.Callback<ComplaintsModel>() {
            @Override
            public void success(ComplaintsModel complaintsModel, Response response) {

                AppUtils.dismissDialog();
                swipeRefreshLayout.setRefreshing(false);

                if (complaintsModel == null) {
                    //AppUtils.ping(mContext, getString(R.string.something_wrong));
                    swipeRefreshLayout.setRefreshing(false);
                    mTvErrorView.setVisibility(View.VISIBLE);
                    rvComplaintlist.setVisibility(View.GONE);
                    return;
                }
                if (complaintsModel.getSuccess() == null) {
                   // AppUtils.ping(mContext, getString(R.string.something_wrong));
                    swipeRefreshLayout.setRefreshing(false);
                    mTvErrorView.setVisibility(View.VISIBLE);
                    rvComplaintlist.setVisibility(View.GONE);
                    return;
                }
                if (complaintsModel.getSuccess().equalsIgnoreCase("false")) {
                    swipeRefreshLayout.setRefreshing(false);
                    mTvErrorView.setVisibility(View.VISIBLE);
                    rvComplaintlist.setVisibility(View.GONE);
                    return;
                }
                if (complaintsModel.getSuccess().equalsIgnoreCase("True")) {
                    swipeRefreshLayout.setRefreshing(false);

                    mTvErrorView.setVisibility(View.GONE);
                    rvComplaintlist.setVisibility(View.VISIBLE);

                    complaintsListAdapter = new ComplaintsListAdapter(ComplaintsList.this,complaintsModel.getFinalAry());
                    rvComplaintlist.setLayoutManager(new LinearLayoutManager(ComplaintsList.this));
                    rvComplaintlist.setAdapter(complaintsListAdapter);


                    RecyclerHelper touchHelper = new RecyclerHelper<ComplaintsModel.FinalAry>((ArrayList<ComplaintsModel.FinalAry>) complaintsModel.getFinalAry(),(RecyclerView.Adapter)complaintsListAdapter);
                    touchHelper.setRecyclerItemDragEnabled(true).setOnDragItemListener(new OnDragListener() {
                        @Override
                        public void onDragItemListener(int fromPosition, int toPosition) {
                        //  complaintsListAdapter.notifyDataSetChanged();
                        }
                    });
                    touchHelper.setRecyclerItemSwipeEnabled(true).setOnSwipeItemListener(new OnSwipeListener() {
                        @Override
                        public void onSwipeItemListener() {
                          //  complaintsListAdapter.notifyDataSetChanged();
                        }
                    });
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
                    itemTouchHelper.attachToRecyclerView(rvComplaintlist);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setParams(String loksevakCode,String fromDate,String toDate,String ComplaintType) {
        Map<String, String> map = new HashMap<>();
        map.put("LoksevakCode",loksevakCode);
        map.put("FromDate",fromDate);
        map.put("ToDate",toDate);
        map.put("ComplaintType",ComplaintType);
        return map;
    }


    @Override
    public void onRefresh() {

        try {
            loksevakCode = PrefUtils.getInstance(ComplaintsList.this).getStringValue(PrefUtils.REFERRAL_ID_KEY,"");
            getComplaintList(loksevakCode,fromDate,toDate,complaintType);

        }catch (Exception ex){
            ex.getLocalizedMessage();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompleted(String fromDate, String toDate, String status) {
        try {
            loksevakCode = PrefUtils.getInstance(ComplaintsList.this).getStringValue(PrefUtils.REFERRAL_ID_KEY,"");

            this.fromDate = fromDate;
            this.toDate = fromDate;
            this.complaintType = status;
            getComplaintList(loksevakCode,fromDate,toDate,status);

        }catch (Exception ex){
            ex.getLocalizedMessage();
        }
    }
}
