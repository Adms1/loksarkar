package com.loksarkar.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.loksarkar.R;
import com.loksarkar.adapters.MediaBulletinAdapter;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.constants.AppConstants;
import com.loksarkar.listener.CallbackHandler;
import com.loksarkar.listener.EndlessRecyclerViewScrollListener;
import com.loksarkar.models.MediaBulletinModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.MockServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MediaBulletinListActivity extends BaseActivity {

    public static final int PAGE_START = 1;
    private static int firstVisibleInListview;
    public ArrayList<MediaBulletinModel.FinalArray> finalArrays = new ArrayList<>();
    private ArrayList<String> pagecount = new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;
    int itemCount = 0;
    private RecyclerView mRvList;
    private String type = "";
    private MediaBulletinAdapter mediaBulletinAdapter;
    private MockServer server;
    private RotateLoaderDialog rotateLoaderDialog;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage, totalrow;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_bulletin_list);

        mRvList = findViewById(R.id.rv_list);

//        mRvList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(MediaBulletinListActivity.this);
        mRvList.setLayoutManager(layoutManager);

        try {
            type = getIntent().getStringExtra("category_type");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        rotateLoaderDialog = new RotateLoaderDialog(this);

        callGetMediaListApi(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    layoutManager.setInitialPrefetchItemCount(finalArrays.size());
                    Log.e("itemcount", "" + itemCount + " " + finalArrays.size());

                    if (dy > 0) {

                        if(itemCount == 0){
                            finalArrays.clear();
                            pagecount.clear();
                            mediaBulletinAdapter.notifyDataSetChanged();
                            callGetMediaListApi(true);
                        }else {
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == totalrow - 1) {
                                itemCount = 0;
                                Log.e("itemcount", "" + itemCount + " " + finalArrays.size());

                            } else {

                                // Scrolling up
                                if (itemCount < totalPage && layoutManager.findFirstCompletelyVisibleItemPosition() + layoutManager.getChildCount() >= layoutManager.getItemCount() && !isLoading) {
                                    // We have reached the end of the recycler view.

                                    Log.e("itemcount", "" + itemCount + " " + finalArrays.size());

                                    callGetMediaListApi(true);
                                    isLoading = true;
                                } else {
                                    // Scrolling down

                                    isLoading = false;
//                        mediaBulletinAdapter = new MediaBulletinAdapter(finalArrays, MediaBulletinListActivity.this, type);
//                        mRvList.setAdapter(mediaBulletinAdapter);
//                        mRvList.setItemAnimator(new DefaultItemAnimator());
                                }
                            }
                        }
                    }
                }
            });
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mRvList.addOnScrollListener(new EndlessScrollListener(layoutManager) {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                    Toast.makeText(MediaBulletinListActivity.this,"LAst",Toast.LENGTH_LONG).show();
//                }
//            });
//        }

    }

    private void callGetMediaListApi(final boolean update) {

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
//                    AppUtils.ping(mContext, getString(R.string.false_msg));
                    return;

                }
                if (mediaBulletinModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    if (mediaBulletinModel.getFinalArray() != null) {
                        if (mediaBulletinModel.getFinalArray().size() > 0) {

                                if(finalArrays.size() > 0) {
                                    if (!pagecount.contains(itemCount)) {
                                        finalArrays.addAll(mediaBulletinModel.getFinalArray());
                                    }
                                }else {
                                    finalArrays.addAll(mediaBulletinModel.getFinalArray());
                                }


                            mRvList.setVisibility(View.VISIBLE);
                            findViewById(R.id.tv_empty).setVisibility(View.GONE);

                            server = new MockServer(1, mediaBulletinModel.getFinalArray().size());

                            totalPage = mediaBulletinModel.getTotalcont();
                            totalrow = mediaBulletinModel.getTotalrow();

                            if (!update) {
                                mediaBulletinAdapter = new MediaBulletinAdapter(finalArrays, MediaBulletinListActivity.this, type);

//                            mRvList.findViewHolderForAdapterPosition(finalArrays.size() - 20);

                                mRvList.setAdapter(mediaBulletinAdapter);
                                mRvList.setItemAnimator(new DefaultItemAnimator());

                            } else {
                                mediaBulletinAdapter.notifyDataSetChanged();
                            }

                            mRvList.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mRvList.findViewHolderForAdapterPosition(finalArrays.size() - 20) != null) {

                                        mRvList.findViewHolderForAdapterPosition(finalArrays.size() - 20);
                                    }
                                }
                            }, 50);

//                            if(update) {
//                                mediaBulletinAdapter.notifyy(mRvList);
//                            }else {
//
//                            }
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
        map.put("PageNo", String.valueOf(++itemCount));

        pagecount.add(String.valueOf(itemCount));

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
