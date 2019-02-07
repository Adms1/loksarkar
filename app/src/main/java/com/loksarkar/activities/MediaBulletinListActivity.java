package com.loksarkar.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.adapters.MediaBulletinAdapter;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.constants.AppConstants;
import com.loksarkar.listener.CallbackHandler;
import com.loksarkar.models.MediaBulletinModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.MockServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MediaBulletinListActivity extends BaseActivity {

    private RecyclerView mRvList;
    private String type = "";
    private MediaBulletinAdapter mediaBulletinAdapter;
    private MockServer server;
    private RotateLoaderDialog rotateLoaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_bulletin_list);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);

        try {
            type = getIntent().getStringExtra("category_type");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        rotateLoaderDialog = new RotateLoaderDialog(this);
        callGetMediaListApi();

    }

    private void callGetMediaListApi() {

        if (!AppUtils.isNetworkConnected(MediaBulletinListActivity.this)) {
            AppUtils.notify(MediaBulletinListActivity.this, getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
        ApiHandler.getApiService().getMediaBulletin(setType(AppConstants.typeId), new retrofit.Callback<MediaBulletinModel>() {
            @Override
            public void success(MediaBulletinModel mediaBulletinModel, Response response) {
                AppUtils.dismissDialog();
                if (mediaBulletinModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    return;
                }
                if (mediaBulletinModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();

                    AppUtils.ping(mContext, getString(R.string.something_wrong));

                    return;
                }
                if (mediaBulletinModel.getSuccess().equalsIgnoreCase("false")) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.false_msg));
                    return;

                }
                if (mediaBulletinModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    if (mediaBulletinModel.getFinalArray() != null) {
                        if (mediaBulletinModel.getFinalArray().size() > 0) {

                            mRvList.setVisibility(View.VISIBLE);
                            findViewById(R.id.tv_empty).setVisibility(View.GONE);

                            server = new MockServer(1, mediaBulletinModel.getFinalArray().size());

                            mediaBulletinAdapter = new MediaBulletinAdapter(mediaBulletinModel.getFinalArray(), MediaBulletinListActivity.this, type);
                            mRvList.setLayoutManager(new LinearLayoutManager(MediaBulletinListActivity.this));
                            mRvList.setAdapter(mediaBulletinAdapter);
                            mRvList.setItemAnimator(new DefaultItemAnimator());
//                            mRvList.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
//                                @Override
//                                public void onScrolledToBottom() {
//                                    mediaBulletinAdapter.showLoadingIndicator();
//                                     loadPage();
//                                }
//
//                                @Override
//                                public boolean endlessScrollEnabled() {
//                                    return !mediaBulletinAdapter.isRefreshing() && !server.lastPageReached();
//                                }
//                            });
                        } else {
                            mRvList.setVisibility(View.GONE);
                            findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
                        }
                    } else {
                        mRvList.setVisibility(View.GONE);
                        findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                rotateLoaderDialog.dismissLoader();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setType(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("Type", type);
        return map;
    }


    private void loadPage() {
        server.getNextPage(new CallbackHandler() {
            @Override
            public void onNewPage(final List page) {
                mediaBulletinAdapter.addNewPage(page); //Call when the loading task finishes
            }
        });
    }


}
