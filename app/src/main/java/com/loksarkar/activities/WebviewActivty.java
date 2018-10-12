package com.loksarkar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.loksarkar.R;
import com.loksarkar.ui.AdvancedWebView;
import com.loksarkar.ui.RotateLoaderDialog;

public class WebviewActivty extends BaseActivity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebview;
    private ProgressBar progressBar;
    private RotateLoaderDialog rotateLoaderDialog;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_activty);

        try {
            url = getIntent().getStringExtra("url");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        rotateLoaderDialog  = new RotateLoaderDialog(this);
        rotateLoaderDialog.showLoader();

        mWebview = (AdvancedWebView)findViewById(R.id.webview);
        progressBar = (ProgressBar)findViewById(R.id.loader);
        progressBar.setVisibility(View.GONE);

        mWebview.loadUrl(url);
        mWebview.setListener(this, this);
        mWebview.clearHistory();
        mWebview.clearCache(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.setWebViewClient(new WebViewClient());
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebview.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebview.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mWebview.onDestroy();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (!mWebview.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }
    @Override
    public void onPageFinished(String url) {
        progressBar.setVisibility(View.GONE);
        rotateLoaderDialog.dismissLoader();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        rotateLoaderDialog.dismissLoader();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

}
