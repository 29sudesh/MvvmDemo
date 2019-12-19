package com.sb.ubergodriver.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sb.ubergodriver.R;
import com.sb.ubergodriver.apiConstants.Urls;
import com.sb.ubergodriver.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String IMAGEBASEURL= Urls.BASEIMAGEURL;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String class_name="Waiting";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        clearNotifications(this);
//        Log.e("remotemessage : ", remoteMessage.getNotification().toString());
        try {
            Log.e("remotedata : ", remoteMessage.getData().toString());
            if (remoteMessage.getData().size() > 0) {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                Log.e("jsonObject", jsonObject.toString());
                if (isAppIsInBackground(getApplicationContext())) {
                    displayCustomNotificationForOrders(jsonObject);
                }

                Log.e("CLASS NAME :  ", class_name);

            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void displayCustomNotificationForOrders(JSONObject jsonObject) {

        Log.e("DATA : ",jsonObject+"");
        try {
            if (jsonObject!=null) {
                if (notifManager == null) {
                    notifManager = (NotificationManager) getSystemService
                            (Context.NOTIFICATION_SERVICE);
                }

                String title=jsonObject.get("title").toString();
                String message=jsonObject.get("body").toString();
                String type=jsonObject.get("type").toString();
                int icon=R.mipmap.ic_launcher;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationCompat.Builder builder;
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        if (type.equals("51"))
                        {
                          JSONObject  myData = new JSONObject(jsonObject.get("notiData").toString());
                            intent.putExtra("fromNotification", myData.toString());
                        }
                    PendingIntent pendingIntent;
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    if (mChannel == null) {
                        mChannel = new NotificationChannel
                                (getPackageName(), title, importance);
                        mChannel.setDescription(message);
                        mChannel.enableVibration(true);
                        notifManager.createNotificationChannel(mChannel);
                    }
                    builder = new NotificationCompat.Builder(this, getPackageName());

                    pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
                    builder.setContentTitle(title)
                            .setSmallIcon(getNotificationIcon()) // required
                            .setContentText(message)  // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setLargeIcon(BitmapFactory.decodeResource
                                    (getResources(), icon))
                            .setBadgeIconType(icon)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri
                                    (RingtoneManager.TYPE_NOTIFICATION));
                    Notification notification = builder.build();
                    notifManager.notify(0, notification);
                } else {

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = null;

                    pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                            .setSound(defaultSoundUri)
                            .setSmallIcon(getNotificationIcon())
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(message));

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1251, notificationBuilder.build());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private int getNotificationIcon() {
        int icon=R.mipmap.ic_launcher;
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? icon : icon;
    }


    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            Log.e("Active process : ",activeProcess);
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                Log.e("componentInfo : ",componentInfo.toString());
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}