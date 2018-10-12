package com.loksarkar.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loksarkar.R;
import com.loksarkar.activities.MediaDetailActivity;
import com.loksarkar.activities.SplashActivity;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.constants.AppConstants;
import com.loksarkar.constants.NotificationConfig;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.NotificationUtils;
import com.loksarkar.utils.PrefUtils;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AppFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = AppFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.d("refreshtoken",refreshedToken);
        storeRegIdInPref(refreshedToken);
        sendRegistrationToServer(refreshedToken);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload:" + remoteMessage.getData().toString());

            try {
                handleDataMessage(remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
        editor.apply();
    }



    private void sendRegistrationToServer(final String token) {
        // sending fcm token to server

        if (!AppUtils.isNetworkConnected(this)) {
            AppUtils.ping(getApplicationContext(),getResources().getString(R.string.internet_error));
            return;
        }

        ApiHandler.getApiService().registerDevice(setToken(token),new retrofit.Callback<RegistrationModel>() {
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

        private Map<String, String> setToken(String token) {
            Map<String, String> map = new HashMap<>();
            map.put("DeviceID",token);
            return map;
        }





    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
             // notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(RemoteMessage json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            String dataMessage = json.getData().get("body");
            String type = json.getData().get("type");
            String imageUrl = "";

            String timestamp = "";
            Log.d(TAG, "push_message : " + dataMessage +" "+type);

            String[] split = dataMessage.split("-");
            String date = split[0];
            String title = split[1];



            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {


                Intent resultIntent = new Intent(getApplicationContext(),MediaDetailActivity.class);
                resultIntent.putExtra("blog_id", type);
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(),title, "", resultIntent);

                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, "", timestamp, resultIntent, imageUrl);
                }

            } else {
                // app is in background, show the notification in notification tray

                Intent resultIntent = new Intent(getApplicationContext(),SplashActivity.class);
                resultIntent.putExtra("blog_id", type);

                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(),title, "", resultIntent);

                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, "", timestamp, resultIntent, imageUrl);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }


    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message,intent, imageUrl);
    }
}
