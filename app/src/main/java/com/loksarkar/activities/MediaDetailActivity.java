package com.loksarkar.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.ui.easyvideoplayer.EasyVideoCallback;
import com.loksarkar.ui.easyvideoplayer.EasyVideoPlayer;
import com.loksarkar.ui.easyvideoplayer.EasyVideoProgressCallback;
import com.loksarkar.models.NotificationDataModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PermissionUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MediaDetailActivity extends AppCompatActivity implements EasyVideoProgressCallback,EasyVideoCallback,PermissionUtils.ReqPermissionCallback {


    private RotateLoaderDialog rotateLoaderDialog;
    private Context mContext;
    private AppCompatImageView backImage;
    private String typeId = "";
    private Toolbar toolbar;
    private TextView tvContentTitle,tvContent,tvDate;
    private FloatingActionButton mSharebtn;
    private String Url = "";
    private EasyVideoPlayer easyVideoPlayer;
    private PermissionUtils.ReqPermissionCallback reqPermissionCallback;

  //  private TextureVideoView mVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_bulletin);
        rotateLoaderDialog = new RotateLoaderDialog(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mSharebtn = (FloatingActionButton)findViewById(R.id.action_share);

        easyVideoPlayer = (EasyVideoPlayer)findViewById(R.id.evp);
        easyVideoPlayer.showControls();

        easyVideoPlayer.setProgressCallback(this);
        reqPermissionCallback = (PermissionUtils.ReqPermissionCallback)(this);
        easyVideoPlayer.setCallback(this);
       //mVideoView = (TextureVideoView) findViewById(R.id.evp);

       mSharebtn.setVisibility(View.GONE);

       setSupportActionBar(toolbar);

       getSupportActionBar().setTitle("");
       getSupportActionBar().setHomeButtonEnabled(true);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backImage  = (AppCompatImageView)findViewById(R.id.backdrop);
        tvContent = (TextView)findViewById(R.id.reading_content);
        tvContentTitle = (TextView)findViewById(R.id.reading_content_title);
        tvDate = (TextView)findViewById(R.id.tv_date);
        mContext= this;

        try{
            typeId = getIntent().getStringExtra("blog_id");
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }

        backImage = (AppCompatImageView) findViewById(R.id.backdrop);

        callNotificationDetailApi();


        mSharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tvContent.getText().toString());
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,imageUrl);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                if(PermissionUtils.hasPermission(MediaDetailActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    downloadFile(Url);
                }else{
                    PermissionUtils.checkPermission(MediaDetailActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE,100,"Continue to download app needs to external storage permission","You can't download",reqPermissionCallback);
                }

            }
        });


    }


    private void initVideoView(String videoUrl) {

        easyVideoPlayer.setSource(Uri.parse(videoUrl));

//        mVideoView.setVideoPath(videoUrl);
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(final MediaPlayer mp) {
//                startVideoPlayback();
//                startVideoAnimation();
//            }
//        });
        easyVideoPlayer.showControls();

    }



    private long downloadFile(String url){

        Uri Download_Uri = Uri.parse(url);
        long downloadReference;
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading");
        request.setDescription("Downloading File");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + ".png");

        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(easyVideoPlayer != null) {
                if (easyVideoPlayer.getVisibility() == View.VISIBLE) {
                    easyVideoPlayer.reset();
                    easyVideoPlayer.stop();
                    easyVideoPlayer.release();
                }
            }else{
                easyVideoPlayer.stop();
                easyVideoPlayer.release();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


//        if (mVideoView != null) {
//            mVideoView.stopPlayback();
//            mVideoView = null;
//        }
    }



    private void callNotificationDetailApi() {

        if (!AppUtils.isNetworkConnected(MediaDetailActivity.this)) {
            AppUtils.notify(MediaDetailActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().notificationDetail(setDetails(),new retrofit.Callback<NotificationDataModel>() {
            @Override
            public void success(NotificationDataModel notificationDataModel, Response response) {
                AppUtils.dismissDialog();
                if (notificationDataModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (notificationDataModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (notificationDataModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    rotateLoaderDialog.dismissLoader();
                    return;
                }
                if (notificationDataModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();
                    mSharebtn.setVisibility(View.VISIBLE);


                    if(notificationDataModel.getFinalAry().get(0).getFileType().equals("Test") || notificationDataModel.getFinalAry().get(0).getFileType().equals("Text")){
                        backImage.setVisibility(View.GONE);
                       // mVideoView.setVisibility(View.GONE);
                        easyVideoPlayer.setVisibility(View.GONE);

                    }
                    else if(notificationDataModel.getFinalAry().get(0).getFileType().equals("Image")){
                        backImage.setVisibility(View.VISIBLE);
                        easyVideoPlayer.setVisibility(View.GONE);

                       // mVideoView.setVisibility(View.GONE);

                        Url = notificationDataModel.getFinalAry().get(0).getFilePath();

                        Glide.with(MediaDetailActivity.this).load(notificationDataModel.getFinalAry().get(0).getFilePath()).into(backImage);
                    }
                    else if(notificationDataModel.getFinalAry().get(0).getFileType().equals("Video")){
                        backImage.setVisibility(View.GONE);
                        easyVideoPlayer.setVisibility(View.VISIBLE);

                       // mVideoView.setVisibility(View.VISIBLE);

                        Url = notificationDataModel.getFinalAry().get(0).getFilePath();

                        initVideoView(notificationDataModel.getFinalAry().get(0).getFilePath());

                    }
                    tvContent.setText(notificationDataModel.getFinalAry().get(0).getDescription());
                    tvContentTitle.setText(notificationDataModel.getFinalAry().get(0).getTitle());
                    tvDate.setText(notificationDataModel.getFinalAry().get(0).getDate());


                }

            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                rotateLoaderDialog.dismissLoader();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }


    private Map<String, String> setDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("TypeID",typeId);
        return map;
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


    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {


    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onClickVideoFrame(EasyVideoPlayer player) {

    }

    @Override
    public void addToStory(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void saveStory(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onVideoProgressUpdate(int position, int duration) {

    }

    @Override
    public void onResult(boolean success) {
        try {
            if(success){
                downloadFile(Url);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
