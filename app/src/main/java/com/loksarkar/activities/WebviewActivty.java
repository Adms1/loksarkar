package com.loksarkar.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.constants.WebViewURLS;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.models.HtmlModel;
import com.loksarkar.ui.AdvancedWebView;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PermissionUtils;
import com.loksarkar.utils.PrefUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.media.MediaFormat.KEY_LANGUAGE;

public class WebviewActivty extends BaseActivity implements AdvancedWebView.Listener, PermissionUtils.ReqPermissionCallback {

    private static final String SP_LOCALE = "LocaleChanger.LocalePersistence";
    private static final String JS_INTERFACE = "Printer";
    private static final String CLOSE_POST_MESSAGE_NAME = "cp-dialog-on-close";
    private static SharedPreferences localeSharedPrefs;
    private AdvancedWebView mWebview;
    private ProgressBar progressBar;
    private RotateLoaderDialog rotateLoaderDialog;
    private String url = "", id = "", langParm = "", loksevakCode = "";
    private String lang = "";
    private FloatingActionButton fabDownload;
    private PermissionUtils.ReqPermissionCallback reqPermissionCallback;
    private Context primaryBaseActivity;
    private Intent intent;
    private String complaintNo = "";
    private AdvancedWebView previewWebview;
    private AdView mAdView, mAdView1;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_activty);
        fabDownload = findViewById(R.id.action_download);
        rotateLoaderDialog = new RotateLoaderDialog(this);
        rotateLoaderDialog.showLoader();
        intent = this.getIntent();

        mAdView = findViewById(R.id.adView);
        mAdView1 = findViewById(R.id.adView1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        if (intent.getStringExtra("size").equalsIgnoreCase("small")) {
            mAdView1.setVisibility(View.GONE);
            mAdView.loadAd(PrefUtils.showads());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mAdView.setVisibility(View.VISIBLE);

                }
            }, 5000);

        } else if (intent.getStringExtra("size").equalsIgnoreCase("large")) {
            mAdView.setVisibility(View.GONE);
            mAdView1.loadAd(PrefUtils.showads());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mAdView1.setVisibility(View.VISIBLE);

                }
            }, 5000);
        } else if (intent.getStringExtra("size").equalsIgnoreCase("")) {
            mAdView1.setVisibility(View.GONE);
            mAdView.setVisibility(View.GONE);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener(){
                public void onAdLoaded(){
                    mInterstitialAd.show();
                }
            });
        }

        mWebview = findViewById(R.id.webview);
        progressBar = findViewById(R.id.loader);
        previewWebview = findViewById(R.id.webview_preview);
        //scrollView = (ScrollView)findViewById(R.id.scroller);

        progressBar.setVisibility(View.GONE);


        reqPermissionCallback = this;

        try {
            url = getIntent().getStringExtra("url");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (url.equalsIgnoreCase(WebViewURLS.REGISTRATION)) {
            fabDownload.setVisibility(View.GONE);

        } else {
            fabDownload.setVisibility(View.GONE);
        }


        try {
            id = getIntent().getStringExtra("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            langParm = getIntent().getStringExtra("lang");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            loksevakCode = getIntent().getStringExtra("loksevak_code");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        localeSharedPrefs = getSharedPreferences(SP_LOCALE, Context.MODE_PRIVATE);
        try {
            lang = localeSharedPrefs.getString(KEY_LANGUAGE, "");

            if (lang.equalsIgnoreCase("gu")) {
                lang = "gujarati";
            } else if (lang.equalsIgnoreCase("hi")) {
                lang = "hindi";


            } else if (lang.equalsIgnoreCase("en")) {
                lang = "english";
            }

            if (langParm != null && langParm.equals("none")) {
                //  url = url+id;

            } else {
                url = url + lang;
            }


            if (loksevakCode != null) {
                if (!TextUtils.isEmpty(loksevakCode)) {

                    if (loksevakCode.equals("none")) {
                        loksevakCode = "";
                    } else {
                        loksevakCode = PrefUtils.getInstance(WebviewActivty.this).getStringValue(PrefUtils.REFERRAL_ID_KEY, "");
                        url = url + "&loksevakcode=" + loksevakCode;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mWebview.setListener(this, this);

        mWebview.clearHistory();
        mWebview.clearCache(true);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebChromeClient(new WebChromeClient());

        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains(WebViewURLS.GET_PARAM_URL)) {
                    Uri uri = Uri.parse(url);

                    String queryParam = uri.getQueryParameter("req");
                    Log.d("URL_PARAM", queryParam);
                    //fabDownload.setVisibility(View.VISIBLE);
                    callHtml(queryParam);
                } else {
                    fabDownload.setVisibility(View.GONE);
                }
            }

        });


        //webView.addJavascriptInterface(new PrintDialogJavaScriptInterface(), JS_INTERFACE);


        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
//        mWebview.setPadding(0, 0, 0, 0);
//        mWebview.setInitialScale(getScale(mWebview.getMeasuredWidth()));
//        mWebview.getSettings().setSupportMultipleWindows(true);
//        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebview.getSettings().setAllowFileAccess(true);


        try {
            URL url1 = new URL(url);
            mWebview.loadUrl(url1.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mInterstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                mInterstitialAd.show();
            }
        });

        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("Download file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    AppUtils.ping(getApplicationContext(), "Downloading..");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        fabDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                //  mWebview.loadUrl("https://www.sample-videos.com/pdf/Sample-pdf-5mb.pdf");
                //mWebview.loadUrl("javascript:window.HtmlViewer.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
//                        try {
//                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PDFTest/");
//                            final String fileName = "Test.jpg";
//                            new TakeScreenShotTask(WebviewActivty.this,mWebview,path.getAbsolutePath(),fileName).execute();
//
//                        }catch (Exception ex){
//                            ex.printStackTrace();
//                        }

                if (PermissionUtils.hasPermission(WebviewActivty.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    try {
                        if (previewWebview != null) {
                            createWebPrintJob(previewWebview);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    PermissionUtils.checkPermission(WebviewActivty.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 100, "Continue to download app needs to external storage permission", "You can't download", reqPermissionCallback);
                }

//                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PDFTest/");
//                final String fileName = "Test.jpg";


//                final ProgressDialog progressDialog=new ProgressDialog(WebviewActivty.this);
//                progressDialog.setMessage("Please wait");
//                progressDialog.show();
//                PdfView.createWebPrintJob(WebviewActivty.this,mWebview, path, fileName, new PdfView.Callback() {
//                    @Override
//                    public void success(String path) {
//                        progressDialog.dismiss();
//                        PdfView.openPdfFile(WebviewActivty.this,getString(R.string.app_name),"file saved at  "+path,path);
//                    }
//
//                    @Override
//                    public void failure() {
//                        progressDialog.dismiss();
//
//                    }
//                });

            }
        });

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
        if (previewWebview != null) {
            destroyWebView(previewWebview);
        }
        super.onDestroy();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebview.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (previewWebview != null) {
            if (previewWebview.getVisibility() == View.VISIBLE) {
                previewWebview.setVisibility(View.GONE);
                // destroyWebView(previewWebview);

            }
        }
        if (mWebview != null) {
            mWebview.setVisibility(View.VISIBLE);
        }
//        if (!mWebview.onBackPressed()) {
//            return;
//        }
        if (mWebview.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageFinished(WebView webView, String url) {
        progressBar.setVisibility(View.GONE);
        rotateLoaderDialog.dismissLoader();


//        webView.loadUrl("javascript:printDialog.setPrintDocument(" +
//                "printDialog.createPrintDocument(" +
//                "window." + JS_INTERFACE + ".getType()," +
//                "window." + JS_INTERFACE + ".getTitle()," +
//                "window." + JS_INTERFACE + ".getContent()," +
//                "window." + JS_INTERFACE + ".getEncoding()))");
//
//        // Add post messages listener.
//        webView.loadUrl("javascript:window.addEventListener('message'," +
//                "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)");
//        try {
//
//
//
//            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PDFTest/");
//            final String fileName = "Test.jpg";
//
//
//            new TakeScreenShotTask(WebviewActivty.this,mWebview,path.getAbsolutePath(),fileName).execute();
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        rotateLoaderDialog.dismissLoader();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Download file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            AppUtils.ping(getApplicationContext(), "Downloading");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    private int getScale(int width1) {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(width1);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    public void onResult(boolean success) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        primaryBaseActivity = newBase;
        super.attachBaseContext(LocaleChanger.configureBaseContext(newBase));
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(final AdvancedWebView webView) {
//        String jobName = getString(R.string.app_name) + "-Document";
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
//        PdfPrint pdfPrint = new PdfPrint(attributes);
//        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf");

        PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + "-Complaint-Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);


        //PrintJob printJob = printManager.print(jobName,new ViewPrintAdapter(this,findViewById(R.id.webview),jobName),null);
        printManager.print(jobName, printAdapter, attributes); //)
//        if(printJob.isCompleted()){
//            AppUtils.ping(WebviewActivty.this,getString(R.string.print_succes));
//        }else{
//            AppUtils.ping(WebviewActivty.this,getString(R.string.something_wrong));
//        }
        // mPrintJobs.add(printJob);


    }

    private void callHtml(String req) {

        if (!AppUtils.isNetworkConnected(WebviewActivty.this)) {
            AppUtils.notify(WebviewActivty.this, getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getHtmlContent(req, new retrofit.Callback<HtmlModel>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void success(HtmlModel loginypeModel, Response response) {
                AppUtils.dismissDialog();
                if (loginypeModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    fabDownload.setVisibility(View.GONE);
                    AppUtils.ping(mContext, getString(R.string.something_wrong));

                    return;
                }
                if (loginypeModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    fabDownload.setVisibility(View.GONE);
                    AppUtils.ping(mContext, getString(R.string.something_wrong));

                    return;
                }
                if (loginypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    rotateLoaderDialog.dismissLoader();
                    fabDownload.setVisibility(View.GONE);


                    return;
                }
                if (loginypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();


                    String htmlContent = loginypeModel.getDataContent();

                    String finalContent = "<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><body>" + htmlContent + "</body></html>";

//                    if(webView == null) {
//                        webView = new WebView(WebviewActivty.this);
//                    }
//                    webView.clearHistory();
//                    webView.clearCache(true);
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.setWebChromeClient(new WebChromeClient());
//                    webView.getSettings().setLoadWithOverviewMode(true);
//                    webView.getSettings().setUseWideViewPort(true);
//                    webView.getSettings().setSupportMultipleWindows(true);
//                    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                    webView.getSettings().setAllowFileAccess(true);
//                    webView.setVisibility(View.GONE);


                    //mWebview.clearHistory();
                    // mWebview.clearCache(true);

                    previewWebview.getSettings().setJavaScriptEnabled(true);
                    previewWebview.setWebChromeClient(new WebChromeClient());
                    previewWebview.getSettings().setLoadWithOverviewMode(true);
                    previewWebview.getSettings().setUseWideViewPort(true);

                    previewWebview.loadData(finalContent, "text/html", "UTF-8");
                    fabDownload.setVisibility(View.VISIBLE);
                    previewWebview.setVisibility(View.VISIBLE);
                    mWebview.setVisibility(View.GONE);

                    //  createWebPrintJob(webView);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                rotateLoaderDialog.dismissLoader();
                fabDownload.setVisibility(View.GONE);
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });


    }

    public void destroyWebView(WebView webView) {
        webView.clearHistory();
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.onPause();
        webView.removeAllViews();
        webView.destroyDrawingCache();
        webView.pauseTimers();
        webView.destroy();
        webView = null;
    }

    private final class PrintDialogWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {

//            if (!url.equals(url))
//                return;

            rotateLoaderDialog.dismissLoader();
            // Submit print document.
            view.loadUrl("javascript:printDialog.setPrintDocument(" +
                    "printDialog.createPrintDocument(" +
                    "window." + JS_INTERFACE + ".getType()," +
                    "window." + JS_INTERFACE + ".getTitle()," +
                    "window." + JS_INTERFACE + ".getContent()," +
                    "window." + JS_INTERFACE + ".getEncoding()))");

            // Add post messages listener.
            view.loadUrl("javascript:window.addEventListener('message'," +
                    "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)");
        }
    }

    final class PrintDialogJavaScriptInterface {

        @JavascriptInterface
        @SuppressWarnings("UnusedDeclaration")
        public String getType() {
            return intent.getType();
        }

        @JavascriptInterface
        @SuppressWarnings("UnusedDeclaration")
        public String getEncoding() {
            return "base64";
        }

        @JavascriptInterface
        @SuppressWarnings("UnusedDeclaration")
        public String getTitle() {
            return intent.getStringExtra(Intent.EXTRA_TITLE);
        }

        /**
         * @return Converted byte stream as base64 encoded string.
         */
        @JavascriptInterface
        @SuppressWarnings("UnusedDeclaration")
        public String getContent() {
            try {

                File file = new File(intent.getStringExtra(Intent.EXTRA_TEXT));
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();

                String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);

                return encoded;
            } catch (Exception e) {
                return "";
            }
        }

        /**
         * JS interface to get informed when the job is finished
         * and the view should be closed.
         *
         * @param message The fired event name
         */
        @JavascriptInterface
        @SuppressWarnings("UnusedDeclaration")
        public void onPostMessage(String message) {
            if (message.startsWith(CLOSE_POST_MESSAGE_NAME)) {
                finish();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public class ViewPrintAdapter extends PrintDocumentAdapter {

        private PrintedPdfDocument mDocument;
        private Context mContext;
        private View mView;
        private String documentName = "";
        private int pageheight, pagewidth;


        public ViewPrintAdapter(Context context, View view, String documentName) {
            mContext = context;
            mView = view;
            this.documentName = documentName;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle extras) {

            mDocument = new PrintedPdfDocument(mContext, newAttributes);

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            // int pages = mDocument.getPageCount(newAttributes);
            int pages = computePageCount(newAttributes);

            if (pages > 0) {
                // Return print information to print framework
                PrintDocumentInfo info = new PrintDocumentInfo
                        .Builder(documentName)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pages)
                        .build();
                pageheight = newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
                pagewidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
                // Content layout reflow is complete
                callback.onLayoutFinished(info, true);
            } else {
                // Otherwise report an error to the print framework
                callback.onLayoutFailed("Page count calculation failed.");
            }


//            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
//                    .Builder(documentName)
//                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                    .setPageCount(1);
//
//            PrintDocumentInfo info = builder.build();
//            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

            int totalPages = 2;
            for (int i = 0; i < totalPages; i++) {
                // Check to see if this page is in the output range.
                if (containsPage(pages, i)) {
                    PdfDocument.Page[] writtenPagesArray;
                    // If so, add it to writtenPagesArray. writtenPagesArray.size()
                    // is used to compute the next output page index.
                    //writtenPagesArray.append(writtenPagesArray.size(), i);
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pagewidth, pageheight, i).create();
                    PdfDocument.Page page = mDocument.startPage(newPage);


                    //   PdfDocument.Page page = mDocument.startPage(i);

                    // check for cancellation
                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        mDocument.close();
                        mDocument = null;
                        return;
                    }


                    // Create a bitmap and put it a canvas for the view to draw to. Make it the size of the view
                    Bitmap bitmap = Bitmap.createBitmap(mView.getMeasuredWidth(), mView.getMeasuredHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    mView.draw(canvas);

                    Rect src = new Rect(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
                    // get the page canvas and measure it.
                    Canvas pageCanvas = page.getCanvas();
                    float pageWidth = pageCanvas.getWidth();
                    float pageHeight = pageCanvas.getHeight();
                    // how can we fit the Rect src onto this page while maintaining aspect ratio?
                    float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());
                    float left = pageWidth / 2 - src.width() * scale / 2;
                    float top = pageHeight / 2 - src.height() * scale / 2;
                    float right = pageWidth / 2 + src.width() * scale / 2;
                    float bottom = pageHeight / 2 + src.height() * scale / 2;
                    RectF dst = new RectF(left, top, right, bottom);

                    pageCanvas.setDensity(30);
                    pageCanvas.drawBitmap(bitmap, src, dst, null);
                    mDocument.finishPage(page);

                    try {
                        mDocument.writeTo(new FileOutputStream(
                                destination.getFileDescriptor()));
                    } catch (IOException e) {
                        callback.onWriteFailed(e.toString());
                        return;
                    } finally {
                        mDocument.close();
                        mDocument = null;
                    }


                    // Draw page content for printing
                    //drawPage(page);

                    // Rendering is complete, so page can be finalized.
                    //mDocument.finishPage(page);
                }
            }
            callback.onWriteFinished(new PageRange[]{new PageRange(0, totalPages)});

//            // Start the page
//            // create a Rect with the view's dimensions.
//
//            // Create a bitmap and put it a canvas for the view to draw to. Make it the size of the view
//            Bitmap bitmap = Bitmap.createBitmap(mView.getMeasuredWidth(),mView.getMeasuredHeight(),
//                    Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            mView.draw(canvas);
//
//            Rect src = new Rect(0, 0, mView.getMeasuredWidth(),mView.getMeasuredHeight());
//            // get the page canvas and measure it.
//            Canvas pageCanvas = page.getCanvas();
//            float pageWidth = pageCanvas.getWidth();
//            float pageHeight = pageCanvas.getHeight();
//            // how can we fit the Rect src onto this page while maintaining aspect ratio?
//            float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());
//            float left = pageWidth / 2 - src.width() * scale / 2;
//            float top = pageHeight / 2 - src.height() * scale / 2;
//            float right = pageWidth / 2 + src.width() * scale / 2;
//            float bottom = pageHeight / 2 + src.height() * scale / 2;
//            RectF dst = new RectF(left, top, right, bottom);
//
//            pageCanvas.drawBitmap(bitmap, src, dst, null);
//            mDocument.finishPage(page);
//
//            try {
//                mDocument.writeTo(new FileOutputStream(
//                        destination.getFileDescriptor()));
//            } catch (IOException e) {
//                callback.onWriteFailed(e.toString());
//                return;
//            } finally {
//                mDocument.close();
//                mDocument = null;
//            }

        }

        private int computePageCount(PrintAttributes printAttributes) {
            int itemsPerPage = 2; // default item count for portrait mode
//
            PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
//            if (!pageSize.isPortrait()) {
//                // Six items per page in landscape orientation
//                itemsPerPage = 6;
//            }
//
//            // Determine number of print items
//            int printItemCount = getPrintItemCount();

            //return (int) Math.ceil(printItemCount / itemsPerPage);

            return itemsPerPage;
        }

        private int getPrintItemCount() {

            return 0;
        }

        private boolean containsPage(PageRange[] pages, int page) {
            // check if contains
            for (int i = 0; i < pages.length; i++) {
                if ((page >= pages[i].getEnd()))
                    return true;
            }
            return false;

        }
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
//            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html).setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();


        }

    }
}
