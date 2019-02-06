package com.loksarkar.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Config;
import android.util.Patterns;
import android.widget.RemoteViews;

import com.loksarkar.R;
import com.loksarkar.constants.NotificationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.http.Url;

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    private static int notifyID = 1;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title,String type,String message, Intent intent) {
        showNotificationMessage(title,type,message,intent, null);
    }

    public void showNotificationMessage(final String title,String type,final String message,Intent intent, String imageUrl) {
        // Check for empty push message
        // notification icon
        final int icon = R.mipmap.ic_launcher;

        String link;
        if(type.equalsIgnoreCase("News")){
            try {

                String [] content  = title.split(Pattern.quote("|"));
                String description  = content[0];
                link = content[1];
                link = link.substring(0,link.length() - 1);

                URL urllink = new URL(link);


                Intent resultIntent = new Intent(Intent.ACTION_VIEW);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.setData(Uri.parse(urllink.toString()));

                final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(),0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + mContext.getPackageName() + "/raw/notification");

                if (!TextUtils.isEmpty(imageUrl)) {

                    if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                        Bitmap bitmap = getBitmapFromURL(imageUrl);

                        if (bitmap != null) {
                            // showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        } else {
                            showSmallNotification(mBuilder,type,icon,title, message,resultPendingIntent);
                        }
                    }
                } else {
                    showSmallNotification(mBuilder,type,icon,title,message,resultPendingIntent);
                    // playNotificationSound();
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + mContext.getPackageName() + "/raw/notification");

            if (!TextUtils.isEmpty(imageUrl)) {

                if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                    Bitmap bitmap = getBitmapFromURL(imageUrl);

                    if (bitmap != null) {
                        // showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    } else {
                        showSmallNotification(mBuilder,type,icon,title, message,resultPendingIntent);
                    }
                }
            } else {
                showSmallNotification(mBuilder,type,icon,title,message,resultPendingIntent);
                // playNotificationSound();
            }
        }



      //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder,String type,int icon,String title,String message,PendingIntent resultPendingIntent) {
        notifyID = (int) (System.currentTimeMillis() & 0xfffffff);

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//        inboxStyle.addLine(message);

        NotificationManager notificationManager;

        RemoteViews contentView = new RemoteViews(mContext.getPackageName(),R.layout.custom_notification_layout);
        contentView.setOnClickPendingIntent(R.layout.custom_notification_layout,resultPendingIntent);
        contentView.setImageViewResource(R.id.image,R.mipmap.ic_launcher);

        if(type.equalsIgnoreCase("News")){
            String [] content  = title.split(Pattern.quote("|"));
            String description  = content[0];
            String link  = content[1];
            contentView.setTextViewText(R.id.title,description);
            contentView.setTextViewText(R.id.text,message);
        }else{
            contentView.setTextViewText(R.id.title,title);
            contentView.setTextViewText(R.id.text,message);
        }


        if(type.equalsIgnoreCase("News")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, String.valueOf(notifyID));
            builder.setTicker(title)
                    //.setWhen(0)
                    .setContent(contentView)
                    .setAutoCancel(true).setContentIntent(resultPendingIntent)
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(title);


            Notification notification = builder.build();
            notification.contentView = contentView;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.contentIntent = resultPendingIntent;

            notification.contentView.setOnClickPendingIntent(R.layout.custom_notification_layout,resultPendingIntent);

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 16) {
                // Inflate and set the layout for the expanded notification view
//            RemoteViews expandedView =
//                    new RemoteViews(mContext.getPackageName(),R.layout.notification_expanded);
//            notification.bigContentView = expandedView;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(String.valueOf(notifyID), title, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(notifyID, notification);

        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, String.valueOf(notifyID));
            builder.setTicker(title)
                    //.setWhen(0)
                    .setContent(contentView)
                    .setAutoCancel(true).setContentIntent(resultPendingIntent)
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(title);


            Notification notification = builder.build();
            notification.contentView = contentView;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.contentIntent = resultPendingIntent;

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 16) {
                // Inflate and set the layout for the expanded notification view
//            RemoteViews expandedView =
//                    new RemoteViews(mContext.getPackageName(),R.layout.notification_expanded);
//            notification.bigContentView = expandedView;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(String.valueOf(notifyID), title, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(notifyID, notification);
        }



    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,String.valueOf(NotificationConfig.NOTIFICATION_ID));


        builder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)

                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher)

               // .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(title);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(NotificationConfig.NOTIFICATION_ID),title,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NotificationConfig.NOTIFICATION_ID, notification);


    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/notification");
//            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
//            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
